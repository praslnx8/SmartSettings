package com.smartsettings.ai.uiModules.smartSettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.smartsettings.ai.R

class SmartSettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val smartSettingsViewModel = ViewModelProviders.of(this).get(SmartSettingsViewModel::class.java)



        return view
    }
}