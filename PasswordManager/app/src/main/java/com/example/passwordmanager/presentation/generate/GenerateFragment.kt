package com.example.passwordmanager.presentation.generate

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
import com.example.passwordmanager.databinding.FragmentGenerateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenerateFragment : Fragment() {

    private lateinit var binding: FragmentGenerateBinding

    private val vm: GenerateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenerateBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        vm.password.observe(viewLifecycleOwner, Observer {
            binding.passwordEdit.setText(it)
        })

        binding.generateBtn.setOnClickListener {

            val lengthValue = binding.lengthEdit.text.toString()
            if (lengthValue.isEmpty()) {
                binding.lengthEdit.error = "Specify the required length!"
                return@setOnClickListener
            }

            val length: Int = lengthValue.toInt()
            val includeSymbols = binding.includeSymbols.isChecked
            val includeDigits = binding.includeDigits.isChecked

            vm.generate(length, includeSymbols, includeDigits)
        }

        binding.copyBtn.setOnClickListener {
            if (vm.password.value.isNullOrEmpty()) {
                Toast.makeText(activity, "Generate password first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("password", vm.password.value)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(activity, "Copied", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }


}