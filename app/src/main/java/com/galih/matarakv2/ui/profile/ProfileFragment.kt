package com.galih.matarakv2.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.galih.matarakv2.base.BaseFragment
import com.galih.matarakv2.databinding.FragmentProfileBinding
import com.galih.matarakv2.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        = FragmentProfileBinding::inflate
    private val viewModel: ProfileViewModel by viewModels()

    override fun setup() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout().observe(viewLifecycleOwner, setLogoutObserver())
        }
    }

    private fun setLogoutObserver() = setObserver<Unit?>(
        onSuccess = {
            startActivity(Intent(binding.root.context, LoginActivity::class.java))
        }
    )
}