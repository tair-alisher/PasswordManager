package com.example.passwordmanager.presentation.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), PasswordClickListener {

    private lateinit var binding: FragmentHomeBinding

    private val vm: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

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

}