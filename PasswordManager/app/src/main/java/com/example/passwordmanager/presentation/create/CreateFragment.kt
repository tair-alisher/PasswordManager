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
//
//        vm.formData.observe(viewLifecycleOwner, Observer {formData ->
//            with (binding) {
//                titleEdit.setText(formData.title)
//                linkEdit.setText(formData.link)
//                loginEdit.setText(formData.login)
//                passwordEdit.setText(formData.password)
//            }
//        })

        with (binding) {
            titleEdit.afterTextChanged { vm.updateTitle(it) }
            linkEdit.afterTextChanged { vm.updateLink(it) }
            loginEdit.afterTextChanged { vm.updateLogin(it) }
            passwordEdit.afterTextChanged { vm.updatePassword(it) }
        }

        binding.addPasswordBtn.setOnClickListener {
            vm.addPassword()
        }

        vm.state.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                ActionState.SUCCESS -> {
                    findNavController().navigateUp()
                }
                ActionState.FAIL -> {
                    if (it.data?.error == FieldError.EMPTY) {
                        when (it.data.field) {
                            Field.NAME -> binding.titleEdit.error = it.error
                            Field.LOGIN -> binding.loginEdit.error = it.error
                            Field.PASSWORD -> binding.passwordEdit.error = it.error
                            else -> Toast.makeText(activity, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else -> { }
            }

        })

        return binding.root
    }

}