package com.example.passwordmanager.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.passwordmanager.R
import com.example.passwordmanager.data.shared_pref.SharedPrefStorage
import com.example.passwordmanager.databinding.FragmentOnBoardingBinding
import com.example.passwordmanager.presentation.onboarding.pages.FirstPageFragment
import com.example.passwordmanager.presentation.onboarding.pages.SecondPageFragment
import com.example.passwordmanager.presentation.onboarding.pages.ThirdPageFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding

    @Inject
    lateinit var sharedPrefStorage: SharedPrefStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        val fragments = arrayListOf<Fragment>(
            FirstPageFragment(),
            SecondPageFragment(),
            ThirdPageFragment()
        )

        val adapter = OnBoardingAdapter(
            fragments,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                markAllNumbersAsSecondary()

                if (position < 2) {
                    binding.nextTextView.text = "Skip"
                } else {
                    binding.nextTextView.text = "Finish"
                }

                when (position) {
                    0 -> {
                        markAsSelected(binding.one)

                    }
                    1 -> {
                        markAsSelected(binding.two)
                    }
                    2 -> {
                        markAsSelected(binding.three)
                    }
                }

            }
        })

        binding.one.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_pink))
        binding.one.textSize = 24f

        binding.nextTextView.setOnClickListener {
            markOnBoardingAsDoneAndGoToHomePage()
        }

        return binding.root
    }

    private fun markOnBoardingAsDoneAndGoToHomePage() {
        sharedPrefStorage.updateOnBoardingState(true)
        findNavController().navigate(R.id.action_onBoardingFragment_to_homeFragment)
    }

    private fun markAsSelected(number: TextView) {
        number.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_pink))
        number.textSize = 24f
    }

    private fun markAllNumbersAsSecondary() {
        with (binding) {
            markAsSecondary(one)
            markAsSecondary(two)
            markAsSecondary(three)
        }
    }

    private fun markAsSecondary(number: TextView) {
        number.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
        number.textSize = 18f
    }

}