package com.example.passwordmanager.presentation.home

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordmanager.R
import com.example.passwordmanager.databinding.FragmentHomeBinding
import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.util.ActionState
import com.example.passwordmanager.presentation.utils.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), PasswordClickListener {

    private lateinit var binding: FragmentHomeBinding

    private val vm: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.loader.visibility = View.VISIBLE

        adapter = HomeAdapter(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.passwords.collect { items -> adapter.submitList(items) }
            }
        }

        vm.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                ActionState.PENDING -> {
                    binding.loader.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.noPasswordsTextView.visibility = View.GONE
                }
                ActionState.SUCCESS -> {
                    binding.loader.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noPasswordsTextView.visibility = View.GONE
                }
                ActionState.FAIL -> {
                    binding.loader.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noPasswordsTextView.visibility = View.GONE

                    Toast.makeText(activity, vm.error.value, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        })

        vm.loadPasswords()

        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createFragment)
        }

        binding.searchField.afterTextChanged {
            if (it.isNotEmpty()) {
                binding.searchField
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.ic_delete, 0
                    )
            }
            else {
                binding.searchField
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, 0, 0
                    )
            }
        }

        binding.searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchVal = binding.searchField.text.toString()

                if (searchVal.isEmpty()) {
                    vm.loadPasswords()
                } else {
                    vm.searchPasswords(searchVal)
                }
            }
            true
        }

        binding.searchField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Check if touch is on the drawableEnd
                if (event.rawX >= (binding.searchField.right -
                            (binding.searchField.compoundDrawables[DRAWABLE_RIGHT]?.bounds?.width() ?: 0) - 20)) {
                    // Handle click on the drawableEnd
                    binding.searchField.text.clear()
                    binding.searchField.clearFocus()
                    vm.loadPasswords()

                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        return binding.root
    }

    override fun showDetails(password: Password) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(password.id)
        )
    }

    override fun copyToClipboard(password: String) {
        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("password", password)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(activity, "Copied!", Toast.LENGTH_SHORT).show()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.searchField.isFocused) {
                binding.searchField.clearFocus()
            } else {
                requireActivity().finishAndRemoveTask()
            }
        }
    }

    companion object {
        private const val DRAWABLE_RIGHT: Int = 2
    }

}