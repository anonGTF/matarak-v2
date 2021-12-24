package com.galih.matarakv2.ui.detection

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.canhub.cropper.CropImage
import com.galih.matarakv2.base.BaseActivity
import com.galih.matarakv2.data.model.DetectionResult
import com.galih.matarakv2.databinding.ActivityResultBinding
import com.galih.matarakv2.ui.main.MainActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class ResultActivity : BaseActivity<ActivityResultBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityResultBinding
        = ActivityResultBinding::inflate
    private val viewModel: DetectionViewModel by viewModels()

    override fun setup() {
        setTitle("Hasil Deteksi")
        setupBackButton()
        getDataFromBundle()
        binding.btnGoesToHospital.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_DATA, MainActivity.ID_GO_TO_MAPS)
            startActivity(intent)
            finish()
        }
    }

    private fun getDataFromBundle() {
        val extras = intent.extras
        extras?.let {
            val result = extras.getParcelable<DetectionResult>(EXTRA_DATA)
            val imageResult = extras.getParcelable<CropImage.ActivityResult>(EXTRA_IMAGE)
            if (result != null) {
                populateItem(result)
            }
            if (imageResult != null) {
                uploadImage(imageResult)
            }
        }
    }

    private fun uploadImage(imageResult: CropImage.ActivityResult) {
        val (byteArr, bitmap) = setupByteArray(imageResult)
        viewModel.uploadImage(byteArr).observe(this, setupUploadObserver(bitmap))
    }

    private fun setupUploadObserver(bitmap: Bitmap) = setObserver<String?>(
        onSuccess = {
            binding.cvProgress.gone()
            val resultData = viewModel.detectImage(bitmap, it.data ?: IMAGE_PLACEHOLDER)
            populateItem(resultData)
            saveResultToDatabase(resultData)
        },
        onError = {
            binding.cvProgress.gone()
        },
        onLoading = {
            binding.cvProgress.visible()
        }
    )

    private fun setupByteArray(imageResult: CropImage.ActivityResult): Pair<ByteArray, Bitmap> {
        val file = File(imageResult.getUriFilePath(this@ResultActivity)!!)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return Pair(baos.toByteArray(), bitmap)
    }

    private fun saveResultToDatabase(result: DetectionResult) {
        viewModel.saveImage(result)
    }

    private fun populateItem(result: DetectionResult) {
        with(binding) {
            tvMessage.text = getMessage(result.title)
            tvConfidence.text = String.format("%.2f", result.confidence)
            tvResult.text = result.title
            Picasso.get().load(result.imageUrl).into(imgInput)
        }
    }

    private fun getMessage(title: String): CharSequence =
        if (title == CATARACT) {
            "Mohon maaf, Anda"
        } else {
            "Selamat! Anda"
        }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_IMAGE = "extra_image"
        const val CATARACT = "Katarak"
        const val IMAGE_PLACEHOLDER = "https://firebasestorage.googleapis.com/v0/b/matarak-v2.appspot.com/o/image_placeholder.png?alt=media&token=31e66f75-e616-4287-8f02-e51900314b1c"
    }
}