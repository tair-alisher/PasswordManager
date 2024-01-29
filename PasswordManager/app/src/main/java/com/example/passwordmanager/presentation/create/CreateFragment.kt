package com.example.passwordmanager.presentation.create

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
import com.example.passwordmanager.databinding.FragmentCreateBinding
import com.example.passwordmanager.domain.util.ActionState
import com.example.passwordmanager.presentation.utils.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFragment : Fragment() {

    private lateinit var binding: FragmentCreateBinding

    private val vm: CreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.generateBtn.setOnClickListener {
            findNavController().navigate(R.id.action_createFragment_to_generateFragment)
        }

        with (binding) {
            titleEdit.afterTextChanged { vm.updateTitle(it) }
            linkEdit.afterTextChanged { vm.updateLink(it) }
            loginEdit.afterTextChanged { vm.updateLogin(it) }
            passwordEdit.afterTextChanged { vm.updatePassword(it) }
        }

        vm.formErrors.observe(viewLifecycleOwner, Observer {
            if (it.nameError.isNotEmpty()) {
                binding.titleEdit.error = it.nameError
            }
            if (it.loginError.isNotEmpty()) {
                binding.loginEdit.error = it.loginError
            }
            if (it.passwordError.isNotEmpty()) {
                binding.passwordEdit.error = it.passwordError
            }
        })

        binding.addPasswordBtn.setOnClickListener {
            vm.addPassword()
        }

        vm.state.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                ActionState.SUCCESS -> {
                    findNavController().navigateUp()
                }
                ActionState.FAIL -> {
                    Toast.makeText(activity, it.error, Toast.LENGTH_SHORT).show()
                }
                else -> { }
            }

        })

        return binding.root
    }

}