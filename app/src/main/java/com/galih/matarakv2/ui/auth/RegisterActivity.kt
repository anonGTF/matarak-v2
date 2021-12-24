package com.galih.matarakv2.ui.auth

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.afollestad.vvalidator.form
import com.galih.matarakv2.base.BaseActivity
import com.galih.matarakv2.databinding.ActivityRegisterBinding
import com.galih.matarakv2.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityRegisterBinding
        = ActivityRegisterBinding::inflate
    private val viewModel: AuthViewModel by viewModels()

    override fun setup() {
        setTitle("Register")
        form {
            useRealTimeValidation()
            input(binding.etName, name = null) {
                isNotEmpty().description("Nama wajib diisi")
            }

            input(binding.etEmail, name = null) {
                isNotEmpty().description("Email wajib diisi")
                isEmail().description("Silahkan masukan email yang valid!")
            }

            input(binding.etPassword, name = null) {
                isNotEmpty().description("Password wajib diisi")
            }

            submitWith(binding.btnRegister) {
                viewModel.register(binding.etName.text.toString(),
                    binding.etEmail.text.toString(), binding.etPassword.text.toString())
                    .observe(this@RegisterActivity, setRegisterObserver())
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setRegisterObserver() = setObserver<Void?>(
        onSuccess = {
            binding.progressBar.gone()
            showToast("Registrasi berhasil")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },
        onError = {
            binding.progressBar.gone()
            showToast(it.message.toString())
        },
        onLoading = { binding.progressBar.visible() }
    )
}