package com.smartsettings.ai.uiModules.smartSettingView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartsettings.ai.R
import com.smartsettings.ai.uiModules.uiModels.ContextListenerViewData
import com.smartsettings.ai.uiModules.uiModels.SettingChangerViewData
import com.smartsettings.ai.uiModules.uiModels.SmartSettingViewData

class SmartSettingRecyclerViewAdapter(
    private val onChangesCallback: (SmartSettingViewData) -> Unit,
    private val onDeleteCallback: (SmartSettingViewData) -> Unit
) : RecyclerView.Adapter<SmartSettingViewHolder>() {

    private val smartSettingViewDataList = ArrayList<SmartSettingViewData>()

    fun setData(smartSettingViewDataList: List<SmartSettingViewData>) {
        val diffResult =
            DiffUtil.calculateDiff(SmartSettingDiffCallback(this.smartSettingViewDataList, smartSettingViewDataList))
        this.smartSettingViewDataList.clear()
        this.smartSettingViewDataList.addAll(smartSettingViewDataList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartSettingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_smart_setting, parent, false)
        return SmartSettingViewHolder(view, onChangesCallback, onDeleteCallback)
    }

    override fun getItemCount(): Int {
        return smartSettingViewDataList.size
    }

    override fun onBindViewHolder(holder: SmartSettingViewHolder, position: Int) {
        holder.setData(smartSettingViewDataList[position])
    }
}


class SmartSettingViewHolder(
    private val view: View,
    private val onChangesCallback: (SmartSettingViewData) -> Unit,
    private val onDeleteCallback: (SmartSettingViewData) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val smartSettingLayout: CardView = view.findViewById(R.id.smartSettingLayout)
    private val nameText: TextView = view.findViewById(R.id.nameText)
    private val switchView: Switch = view.findViewById(R.id.switchView)
    private val statusIcon: ImageView = view.findViewById(R.id.statusIcon)

    fun setData(smartSettingViewData: SmartSettingViewData) {
        nameText.text = smartSettingViewData.name
        switchView.isChecked = smartSettingViewData.isEnabled


        if (smartSettingViewData.isEnabled) {
            if (smartSettingViewData.isChangesApplied) {
                smartSettingLayout.background =
                    ActivityCompat.getDrawable(view.context, R.drawable.border_success_green)
            } else {
                smartSettingLayout.background = ActivityCompat.getDrawable(view.context, R.drawable.border_enabled)
            }
        } else {
            smartSettingLayout.background = ActivityCompat.getDrawable(view.context, R.drawable.border_disabled)
        }

        switchView.setOnCheckedChangeListener { _, isChecked ->
            smartSettingViewData.isEnabled = isChecked
            onChangesCallback(smartSettingViewData)
        }
    }
}

class SmartSettingDiffCallback(
    private val viewList: List<SmartSettingViewData>,
    private val newList: List<SmartSettingViewData>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

    override fun getOldListSize(): Int {
        return viewList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = viewList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if ((oldItem.key != newItem.key) || (oldItem.isEnabled != newItem.isEnabled) || (oldItem.isRunning != newItem.isRunning) ||
            (oldItem.isChangesApplied != newItem.isChangesApplied) || (oldItem.conjunctionLogic != newItem.conjunctionLogic) ||
            (oldItem.name != newItem.name) || (!isSameSettingChangers(
                oldItem.settingChangers,
                newItem.settingChangers
            )) ||
            (!isSameContextListeners(oldItem.contextListeners, newItem.contextListeners))
        ) {
            return false
        }

        return true
    }

    private fun isSameSettingChangers(
        oldSettingChangers: List<SettingChangerViewData>,
        newSettingChangers: List<SettingChangerViewData>
    ): Boolean {

        if (oldSettingChangers.size != newSettingChangers.size) {
            return false
        }

        for (i in oldSettingChangers.indices) {
            val oldSettingChanger = oldSettingChangers[i]
            val newSettingChanger = newSettingChangers[i]

            if ((oldSettingChanger.serializedData != newSettingChanger.serializedData)) {
                return false
            }
        }

        return true
    }

    private fun isSameContextListeners(
        oldContextListeners: List<ContextListenerViewData>,
        newContextListeners: List<ContextListenerViewData>
    ): Boolean {

        if (oldContextListeners.size != newContextListeners.size) {
            return false
        }

        for (i in oldContextListeners.indices) {
            val oldSettingChanger = oldContextListeners[i]
            val newSettingChanger = newContextListeners[i]

            if ((oldSettingChanger.serializedData != newSettingChanger.serializedData)) {
                return false
            }
        }

        return true
    }
}

