package com.example.passwordmanager.presentation.update

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
import com.example.passwordmanager.databinding.FragmentUpdateBinding
import com.example.passwordmanager.domain.util.ActionState
import com.example.passwordmanager.presentation.utils.Field
import com.example.passwordmanager.presentation.utils.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding

    private val vm: UpdateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(inflater, container ,false)

        val passwordId = arguments?.getInt("passwordId")

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.generateBtn.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_generateFragment)
        }

        vm.state.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                ActionState.PENDING -> {
                    binding.loader.visibility = View.VISIBLE
                }
                ActionState.FAIL -> {
                    binding.loader.visibility = View.GONE
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
                ActionState.SUCCESS -> {
                    binding.loader.visibility = View.GONE
                }
                ActionState.MOVE_NEXT -> {
                    findNavController().navigateUp()
                }
                else -> {}
            }
        })

        vm.password.observe(viewLifecycleOwner, Observer {
            with (binding) {
                titleEdit.setText(it.title)
                linkEdit.setText(it.link)
                loginEdit.setText(it.login)
                passwordEdit.setText(it.password)
            }
        })

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

        if (passwordId == null || passwordId == 0) {
            Toast.makeText(activity, "Failed to retrieve password", Toast.LENGTH_SHORT).show()
        } else {
            vm.loadPassword(passwordId)
        }

        with (binding) {
            titleEdit.afterTextChanged { vm.updateFormData(Field.NAME, it) }
            linkEdit.afterTextChanged { vm.updateFormData(Field.LINK, it) }
            loginEdit.afterTextChanged { vm.updateFormData(Field.LOGIN, it) }
            passwordEdit.afterTextChanged { vm.updateFormData(Field.PASSWORD, it) }
        }

        binding.saveChangesBtn.setOnClickListener {
            vm.saveChanges()
        }

        return binding.root
    }

}