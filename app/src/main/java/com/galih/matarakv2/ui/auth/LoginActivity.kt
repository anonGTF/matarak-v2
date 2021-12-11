package com.galih.matarakv2.ui.auth

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.afollestad.vvalidator.form
import com.galih.matarakv2.base.BaseActivity
import com.galih.matarakv2.databinding.ActivityLoginBinding
import com.galih.matarakv2.ui.main.MainActivity
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        = ActivityLoginBinding::inflate
    private val viewModel: AuthViewModel by viewModels()

    override fun setup() {
        if (viewModel.validateIsLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        form {
            useRealTimeValidation()
            input(binding.etEmail, name = null) {
                isNotEmpty().description("Email wajib diisi")
                isEmail().description("Silahkan masukan email yang valid!")
            }

            input(binding.etPassword, name = null) {
                isNotEmpty().description("Password wajib diisi")
            }

            submitWith(binding.btnLogin) {
                viewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                    .observe(this@LoginActivity, setLoginObserver())
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setLoginObserver() = setObserver<FirebaseUser?>(
        onSuccess = {
            binding.progressBar.gone()
            showToast("Login berhasil")
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