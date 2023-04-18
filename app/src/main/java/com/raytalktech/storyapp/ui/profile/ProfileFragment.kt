package com.raytalktech.storyapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.raytalktech.storyapp.databinding.FragmentProfileBinding
import com.raytalktech.storyapp.model.UserModel
import com.raytalktech.storyapp.ui.login.LoginActivity
import com.raytalktech.storyapp.utils.ViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var _binding: FragmentProfileBinding
    private val binding get() = _binding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

            viewModel.getUser().observe(viewLifecycleOwner, setupViewModel)
        }
    }

    private val setupViewModel = Observer<UserModel> { user ->
        user.let {
            binding.apply {
                tvNameProfile.text = it.name
                tvEmailProfile.text = it.email

                btnLogout.setOnClickListener {
                    btnLogout.isEnabled = false
                    pbView.isVisible = true

                    viewModel.logout()

                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }
}