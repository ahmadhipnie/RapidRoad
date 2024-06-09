package com.example.rapidroad.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.rapidroad.R
import com.example.rapidroad.databinding.FragmentProfileBinding
import com.example.rapidroad.view.ChangePasswordActivity
import com.example.rapidroad.view.FaqActivity
import com.example.rapidroad.view.LoginActivity
import com.example.rapidroad.viewmodel.LoginViewModel
import com.example.rapidroad.viewmodel.ProfileViewModel
import com.example.rapidroad.viewmodel.ViewModelFactory


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFaqProfile.setOnClickListener {
            Intent(requireContext(), FaqActivity::class.java).apply {
                startActivity(this)
            }
        }

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvUserNameProfile.text = user.userName
            binding.tvUserEmailProfile.text = user.userEmail
        }


        binding.btnUbahPasswordProfile.setOnClickListener {
            Intent(requireContext(), ChangePasswordActivity::class.java).apply {
                startActivity(this)
            }

        }

        binding.btnLogoutProfile.setOnClickListener {
            profileViewModel.logout()
            Intent(requireContext(), LoginActivity::class.java).apply {
                startActivity(this)
                requireActivity().finish()
            }

        }
    }


}