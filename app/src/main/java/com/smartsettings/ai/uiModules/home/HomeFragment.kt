package com.smartsettings.ai.uiModules.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.smartsettings.ai.R
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.runner.MainForeGroundService
import com.smartsettings.ai.uiModules.SmartSettingViewProvider
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), HomeView {

    override fun showSmartSettings(smartSettings: List<SmartSetting<out Any>>) {
        parentLayout.removeAllViews()

        val ctx = context
        if (ctx != null) {
            for (smartSetting in smartSettings) {
                parentLayout.addView(SmartSettingViewProvider.getView(ctx, smartSetting, { changedSmartSetting ->
                    homePresenter?.smartSettingChangedFromUser(changedSmartSetting)
                }, {
                    homePresenter?.deleteSmartSetting(smartSetting)
                }))
            }
        }
    }

    private var homePresenter: HomePresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        homePresenter = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homePresenter?.setHomeView(this)
        homePresenter?.getSmartSettings(this)

        startMainService()

        return view
    }

    private fun startMainService() {
        val ctx = context
        if (ctx != null) {
            MainForeGroundService.startService(ctx)
        }
    }
}