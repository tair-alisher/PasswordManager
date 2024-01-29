package com.example.passwordmanager.presentation.details

import android.app.AlertDialog
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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.passwordmanager.R
import com.example.passwordmanager.databinding.FragmentDetailsBinding
import com.example.passwordmanager.domain.util.ActionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    private val vm: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val passwordId = arguments?.getInt("passwordId")

        vm.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                ActionState.PENDING -> {
                    binding.loader.visibility = View.VISIBLE
                }
                ActionState.FAIL -> {
                    binding.loader.visibility = View.GONE
                    Toast.makeText(activity, "Failed to get password information", Toast.LENGTH_SHORT).show()
                }
                ActionState.SUCCESS -> {
                    binding.loader.visibility = View.GONE
                }
                else -> {}
            }
        })

        vm.password.observe(viewLifecycleOwner, Observer {
            binding.header.text = it.title
            binding.linkValue.text = it.link
            binding.loginValue.text = it.login
        })

        vm.showPasswordState.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.showPasswordImageView.setImageResource(R.drawable.ic_eye_close)
                binding.passwordValue.text = vm.password.value?.password
            } else {
                binding.showPasswordImageView.setImageResource(R.drawable.ic_eye_open)
                binding.passwordValue.text = "********"
            }
        })

        if (passwordId == null || passwordId == 0) {
            Toast.makeText(activity, "Failed to retrieve password", Toast.LENGTH_SHORT).show()
        } else {
            vm.loadPassword(passwordId)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.showPasswordImageView.setOnClickListener {
            vm.toggleShowPasswordState()
        }

        binding.copyPasswordImageView.setOnClickListener {
            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("password", vm.password.value?.password)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(activity, "Copied!", Toast.LENGTH_SHORT).show()
        }

        vm.deleteState.observe(viewLifecycleOwner, Observer {
            if (it == DeleteState.SUCCESS) {
                findNavController().navigateUp()
            } else if (it == DeleteState.FAIL) {
                Toast.makeText(activity, "Failed to delete the record", Toast.LENGTH_SHORT).show()
            }
        })

        binding.updateBtn.setOnClickListener {
            val id = vm.password.value?.id
            if (id == null) {
                Toast.makeText(activity, "Failed to get password identifier", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            findNavController().navigate(
                DetailsFragmentDirections.actionDetailsFragmentToUpdateFragment(
                    id
                )
            )
        }

        binding.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Are you sure you want to delete the record?")
                .setCancelable(true)
                .setPositiveButton("Yes") { _, _ ->
                    vm.deletePassword()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }

            val alert = builder.create()
            alert.show()
        }

        return binding.root
    }

}