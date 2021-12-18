package com.galih.matarakv2.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.galih.matarakv2.base.BaseFragment
import com.galih.matarakv2.data.model.User
import com.galih.matarakv2.databinding.FragmentProfileBinding
import com.galih.matarakv2.ui.auth.LoginActivity
import com.galih.matarakv2.utils.Utils.toGender
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        = FragmentProfileBinding::inflate
    private val viewModel: ProfileViewModel by viewModels()

    override fun setup() {
        viewModel.getProfile().observe(viewLifecycleOwner, setProfileObserver())

        binding.btnEditProfil.setOnClickListener {
            startActivity(Intent(binding.root.context, EditProfileActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout().observe(viewLifecycleOwner, setLogoutObserver())
        }
    }

    private fun setProfileObserver() = setObserver<User?>(
        onSuccess = { response ->
            binding.progressBar.gone()
            populateData(response.data)
        },
        onError = { response ->
            binding.progressBar.gone()
            showToast(response.message.toString())
        },
        onLoading = { binding.progressBar.visible() }
    )

    private fun populateData(data: User?) {
        binding.apply {
            tvName.text = data?.name
            tvEmail.text = viewModel.getUserEmail()
            tvBirthdate.text = data?.birthDate
            tvGender.text = data?.gender?.toGender()
            tvLocation.text = data?.address

            Picasso.get().load(data?.imageUrl).into(ivPhotoProfile)
        }
    }

    private fun setLogoutObserver() = setObserver<Unit?>(
        onSuccess = {
            startActivity(Intent(binding.root.context, LoginActivity::class.java))
        },
        onError = { response ->
            showToast(response.message.toString())
        }
    )
}