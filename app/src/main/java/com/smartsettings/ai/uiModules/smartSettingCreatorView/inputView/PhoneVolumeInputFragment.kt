package com.smartsettings.ai.uiModules.smartSettingCreatorView.inputView

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartsettings.ai.R
import com.smartsettings.ai.data.actionData.VolumeActionData
import kotlinx.android.synthetic.main.fragment_volume_action_input.*

class PhoneVolumeInputFragment(val volumeActionData: VolumeActionData?) : Fragment(), SmartSettingInputView<VolumeActionData> {

    override fun getInput(): VolumeActionData {
        val input = inputText.text.toString()
        return VolumeActionData(Integer.parseInt(input))
    }

    override fun validate(): Boolean {
        val input = inputText.text.toString()
        if(TextUtils.isEmpty(input)) {
            //TODO show error dialog.
            return false
        } else if(Integer.valueOf(input) < 0 || Integer.valueOf(input) > 100) {
            //TODO show error dialog.
            return false
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_volume_action_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        volumeActionData?.let {
            inputText.setText(it.volumeToBeSet.toString())
        }
    }
}