package com.galih.matarakv2.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.galih.matarakv2.base.BaseActivity
import com.galih.matarakv2.data.model.User
import com.galih.matarakv2.databinding.ActivityEditProfileBinding
import com.galih.matarakv2.ui.main.MainActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_edit_profile.*
import android.widget.ArrayAdapter
import com.galih.matarakv2.R
import com.galih.matarakv2.utils.Utils.toGender
import com.galih.matarakv2.utils.Utils.toString
import java.util.*

@AndroidEntryPoint
class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityEditProfileBinding
        = ActivityEditProfileBinding::inflate
    private val viewModel: ProfileViewModel by viewModels()
    private var photoProfile = ""
    private var gender = false

    override fun setup() {
        setTitle("Edit Profil")
        setupBackButton()
        getDataFromBundle()
        setupDropdown()
        setupDatePicker()
        setupListener()
    }

    private fun getDataFromBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            val data = intent.getParcelableExtra<User>(EXTRA_DATA)
            populateData(data)
        }
    }

    private fun setupDropdown() {
        val genderAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this, R.layout.item_gender, GENDER
        )
        binding.inputGender.setAdapter(genderAdapter)
    }

    private fun setupDatePicker() {
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.etBirthdate.setText(cal.time.toString("dd/MM/yyyy"))
            }

        binding.etBirthdate.setOnClickListener {
            DatePickerDialog(this@EditProfileActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }


    private fun setupListener() {
        binding.inputGender.setOnItemClickListener { _, _, position, _ ->
            gender = (position == 0)
        }

        binding.btnChangePhoto.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 1)
        }

        binding.btnSaveChange.setOnClickListener {
            with(binding) {
                viewModel.saveProfile(etNama.text.toString(), etBirthdate.text.toString(),
                    etLocation.text.toString(), gender, photoProfile)
                    .observe(this@EditProfileActivity, setupSaveObserver())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageUri = data!!.data as Uri
            Picasso.get().load(imageUri).into(binding.imgProfile)
            viewModel.uploadImageProfile(imageUri).observe(this, setupUploadObserver())
        }
    }

    private fun setupUploadObserver() = setObserver<String?>(
        onSuccess = {
            binding.progressBar.gone()
            photoProfile = it.data ?: User.DEFAULT_PROFILE_PICTURE
        },
        onError = {
            binding.progressBar.gone()
            showToast(it.message.toString())
        },
        onLoading = { binding.progressBar.visible() }
    )

    private fun setupSaveObserver() = setObserver<Void?>(
        onSuccess = {
            binding.progressBar.gone()
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_DATA, MainActivity.ID_GO_TO_PROFILE)
            startActivity(intent)
        },
        onError = {
            binding.progressBar.gone()
            showToast(it.message.toString())
        },
        onLoading = { binding.progressBar.visible() }
    )

    private fun populateData(data: User?) {
        with(binding) {
            etNama.setText(data?.name)
            etBirthdate.setText(data?.birthDate)
            etEmail.setText(viewModel.getUserEmail())
            etLocation.setText(data?.address)
            inputGender.setText(data?.gender?.toGender())
            Picasso.get().load(data?.imageUrl).into(imgProfile)
        }

        photoProfile = data?.imageUrl ?: User.DEFAULT_PROFILE_PICTURE
        gender = data?.gender ?: false
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        val GENDER = listOf("Laki-laki", "Perempuan")
    }
}