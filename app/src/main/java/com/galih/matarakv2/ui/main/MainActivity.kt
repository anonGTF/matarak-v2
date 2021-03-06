package com.galih.matarakv2.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImage
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.galih.matarakv2.R
import com.galih.matarakv2.base.BaseActivity
import com.galih.matarakv2.databinding.ActivityMainBinding
import com.galih.matarakv2.ui.detection.DetectionHistoryFragment
import com.galih.matarakv2.ui.detection.ResultActivity
import com.galih.matarakv2.ui.home.HomeFragment
import com.galih.matarakv2.ui.maps.NearestHospitalFragment
import com.galih.matarakv2.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        = ActivityMainBinding::inflate
    private var shouldGoTo: Int = 0
    private val homeFragment = HomeFragment()
    private val hospitalFragment = NearestHospitalFragment()
    private val historyFragment = DetectionHistoryFragment()
    private val profileFragment = ProfileFragment()

    override fun setup() {
        getDataFromBundle()
        setInitialFragment()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> setCurrentFragment(homeFragment)
                R.id.miHospital -> setCurrentFragment(hospitalFragment)
                R.id.miHistory -> setCurrentFragment(historyFragment)
                R.id.miProfile -> setCurrentFragment(profileFragment)
            }
            true
        }

        binding.fab.setOnClickListener {
            askPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                onAccepted = {
                    Pix.start(this@MainActivity,
                        Options.init()
                            .setRequestCode(CAMERA_REQUEST_CODE)
                            .setMode(Options.Mode.Picture))
                }
            )
        }
    }

    private fun setInitialFragment() {
        when (shouldGoTo) {
            ID_GO_TO_MAPS -> {
                setCurrentFragment(hospitalFragment)
                binding.bottomNavigationView.selectedItemId = R.id.miHospital
            }
            ID_GO_TO_PROFILE -> {
                setCurrentFragment(profileFragment)
                binding.bottomNavigationView.selectedItemId = R.id.miProfile
            }
            else -> setCurrentFragment(homeFragment)
        }
    }

    private fun getDataFromBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            shouldGoTo = intent.getIntExtra(EXTRA_DATA, 0)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                val returnValue: ArrayList<String> = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS) as ArrayList<String>
                CropImage.activity(Uri.fromFile(File(returnValue[0])))
                    .start(this)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (result != null) {
                    goToResultActivity(result)
                }
            }
        }else if(resultCode == Activity.RESULT_OK && requestCode == 666){
            Toast.makeText(applicationContext, "pindah", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToResultActivity(imageResult:  CropImage.ActivityResult) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE, imageResult)
        startActivity(intent)
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 2
        const val EXTRA_DATA = "extra_data"
        const val ID_GO_TO_MAPS = 1
        const val ID_GO_TO_PROFILE = 2
    }
}