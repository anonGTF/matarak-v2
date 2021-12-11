package com.galih.matarakv2.ui.main

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.galih.matarakv2.R
import com.galih.matarakv2.base.BaseActivity
import com.galih.matarakv2.databinding.ActivityMainBinding
import com.galih.matarakv2.ui.detection.DetectionHistoryFragment
import com.galih.matarakv2.ui.home.HomeFragment
import com.galih.matarakv2.ui.maps.NearestHospitalFragment
import com.galih.matarakv2.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        = ActivityMainBinding::inflate

    override fun setup() {
        val homeFragment = HomeFragment()
        val hospitalFragment = NearestHospitalFragment()
        val historyFragment = DetectionHistoryFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> setCurrentFragment(homeFragment)
                R.id.miHospital -> setCurrentFragment(hospitalFragment)
                R.id.miHistory -> setCurrentFragment(historyFragment)
                R.id.miProfile -> setCurrentFragment(profileFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}