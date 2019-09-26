package com.smartsettings.ai.uiModules.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.smartsettings.ai.MainForeGroundService
import com.smartsettings.ai.R
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        homeViewModel.getSmartSettings().observeForever {

            view.parentLayout.removeAllViews()

            var isEnabled = false
            for (smartSetting in it) {
                if (smartSetting.second) {
                    isEnabled = true
                }
                val ctx = context
                if (ctx != null) {
                    view.parentLayout.addView(smartSetting.first.getView(ctx))
                }
            }

            val ctx = context
            if (ctx != null) {
                if (isEnabled) {
                    MainForeGroundService.startService(ctx)
                } else {
                    MainForeGroundService.stopService(ctx)
                }
            }
        }

        return view
    }
}