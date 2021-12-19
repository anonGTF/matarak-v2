package com.galih.matarakv2.ui.profile

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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
    private val viewModel: ProfileViewModel by activityViewModels()
    private var data: User? = null

    override fun setup() {
        viewModel.getProfile().observe(viewLifecycleOwner, setProfileObserver())

        binding.btnEditProfil.setOnClickListener {
            val intent = Intent(binding.root.context, EditProfileActivity::class.java)
            intent.putExtra(EditProfileActivity.EXTRA_DATA, data)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout().observe(viewLifecycleOwner, setLogoutObserver())
        }
        Log.d("coba", "setup: $viewModel")
    }

    private fun setProfileObserver() = setObserver<User?>(
        onSuccess = { response ->
            binding.progressBar.gone()
            data = response.data
            populateData()
        },
        onError = { response ->
            Log.d("coba", "setProfileObserver: ${response.data}")
            binding.progressBar.gone()
            showToast(response.message.toString())
        },
        onLoading = { binding.progressBar.visible() }
    )

    private fun populateData() {
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