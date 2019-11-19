package com.smartsettings.ai.uiModules.smartSettingView

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartsettings.ai.R
import com.smartsettings.ai.utils.InputDialogUtils

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
    private val moreBtn: ImageView = view.findViewById(R.id.moreButton)
    private val enableButton: ViewGroup = view.findViewById(R.id.enableButton)
    private val runningLayout: ViewGroup = view.findViewById(R.id.runningLayout)
    private val appliedLayout: ViewGroup = view.findViewById(R.id.appliedLayout)
    private val appliedText: TextView = view.findViewById(R.id.appliedText)
    private var onBind = false

    fun setData(smartSettingViewData: SmartSettingViewData) {

        onBind = true

        nameText.text = smartSettingViewData.name

        appliedLayout.visibility = View.GONE
        enableButton.visibility = View.GONE
        runningLayout.visibility = View.GONE

        if (smartSettingViewData.isEnabled) {
            if (smartSettingViewData.lastAppliedTime > 0L) {
                smartSettingLayout.background =
                    ActivityCompat.getDrawable(view.context, R.color.successGreen)
                appliedLayout.visibility = View.VISIBLE
                appliedText.text = "Changes Applied : " + DateUtils.getRelativeTimeSpanString(smartSettingViewData.lastAppliedTime)
            } else {
                smartSettingLayout.background = ActivityCompat.getDrawable(view.context, R.color.enabled)
                runningLayout.visibility = View.VISIBLE
            }
        } else {
            smartSettingLayout.background = ActivityCompat.getDrawable(view.context, R.color.disabled)
            enableButton.visibility = View.INVISIBLE
        }

        enableButton.setOnClickListener {
            if (!onBind) {
                smartSettingViewData.isEnabled = true
                onChangesCallback(smartSettingViewData)
            }
        }

        moreBtn.setOnClickListener {
            val popupMenu = PopupMenu(moreBtn.context, moreBtn)
            if(smartSettingViewData.isEnabled) {
                popupMenu.menu.add(0,0,0,"Disable")
            }
            popupMenu.menu.add(1,1,1,"Delete")
            popupMenu.setOnMenuItemClickListener {
                if(it.groupId == 0) {
                    smartSettingViewData.isEnabled = false
                    onChangesCallback(smartSettingViewData)
                } else {
                    onDeleteCallback(smartSettingViewData)
                }
                true
            }
        }

        smartSettingLayout.setOnLongClickListener {
            InputDialogUtils.askConfirmation(view.context, "Delete setting", "Delete the setting!") { isPositive, _ ->
                if (isPositive) {
                    onDeleteCallback(smartSettingViewData)
                }
                true
            }.show()

            true
        }

        onBind = false
    }
}

class SmartSettingDiffCallback(
    private val viewList: List<SmartSettingViewData>,
    private val newList: List<SmartSettingViewData>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return viewList[oldItemPosition].id == newList[newItemPosition].id
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

        if ((oldItem.id != newItem.id) || (oldItem.isEnabled != newItem.isEnabled) || (oldItem.isRunning != newItem.isRunning) ||
            (oldItem.lastAppliedTime != newItem.lastAppliedTime) || (oldItem.conjunctionLogic != newItem.conjunctionLogic) ||
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

            if ((oldSettingChanger.serializedActionData != newSettingChanger.serializedActionData)) {
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

            if ((oldSettingChanger.serializedCriteriaData != newSettingChanger.serializedCriteriaData)) {
                return false
            }
        }

        return true
    }
}

