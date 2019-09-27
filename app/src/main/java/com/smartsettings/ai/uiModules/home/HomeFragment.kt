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

    private var homeViewModel: HomeViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        homeViewModel?.smartSettingLiveData?.observeForever {
            view.parentLayout.removeAllViews()

            val ctx = context
            if (ctx != null) {
                for (smartSetting in it) {
                    view.parentLayout.addView(smartSetting.getView(ctx))
                }

                startMainService()
            }
        }

        val ctx = context
        if (ctx != null) {
            homeViewModel?.getSmartSettings()
        }

        return view
    }

    private fun startMainService() {
        val ctx = context
        if (ctx != null) {
            MainForeGroundService.startService(ctx)
        }
    }
}