package com.smartsettings.ai.modules.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        homeViewModel.getSmartSettings().observeForever {

        }
    }
}