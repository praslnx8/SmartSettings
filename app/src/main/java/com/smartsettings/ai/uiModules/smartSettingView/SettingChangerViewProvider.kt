package com.smartsettings.ai.uiModules.smartSettingView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import com.smartsettings.ai.R
import com.smartsettings.ai.uiModules.uiModels.SettingChangerViewData

object SettingChangerViewProvider {

    fun getView(
        parent: ViewGroup,
        settingChangerViewData: SettingChangerViewData,
        isRunning: Boolean
    ): View {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_changer, parent, false)

        val settingChangerLayout: LinearLayout = view.findViewById(R.id.settingChangerLayout)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        val drawable = if (settingChangerViewData.isChangesApplied) {
            ActivityCompat.getDrawable(view.context, R.drawable.border_success_green)
        } else if (!settingChangerViewData.isPermissionGranted) {
            ActivityCompat.getDrawable(view.context, R.drawable.border_failure_red)
        } else if (isRunning) {
            ActivityCompat.getDrawable(view.context, R.drawable.border_enabled)
        } else {
            ActivityCompat.getDrawable(view.context, R.drawable.border_disabled)
        }

        settingChangerLayout.background = drawable
        imageView.background = ActivityCompat.getDrawable(view.context, R.drawable.ic_volume)

        return view
    }
}