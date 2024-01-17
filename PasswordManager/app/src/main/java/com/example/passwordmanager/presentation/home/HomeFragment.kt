package com.example.passwordmanager.presentation.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordmanager.R
import com.example.passwordmanager.databinding.FragmentHomeBinding
import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.util.ActionState
import dagger.hilt.android.AndroidEntryPoint

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

        vm.state.observe(viewLifecycleOwner, Observer {
            Log.d("HOME", "list count: ${it.data?.size}")
            Log.d("HOME", "state: ${it.state}")
            adapter.list = emptyList()

            when (it.state) {
                ActionState.SUCCESS -> {
                    adapter.list = it.data ?: emptyList()
                    binding.loader.visibility = View.GONE

                    if (!it.data.isNullOrEmpty()) {
                        binding.noPasswordsTextView.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.visibility = View.GONE
                        binding.noPasswordsTextView.visibility = View.VISIBLE
                    }
                }

                ActionState.FAIL -> {
                    Toast.makeText(activity, it.error, Toast.LENGTH_SHORT).show()
                }

                ActionState.PENDING -> {
                    binding.loader.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }

                else -> {}
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createFragment)
        }

        return binding.root
    }

    override fun showDetails(password: Password) {
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
    }

    override fun copyToClipboard(password: String) {
        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("password", password)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(activity, "Copied!", Toast.LENGTH_SHORT).show()
    }

}