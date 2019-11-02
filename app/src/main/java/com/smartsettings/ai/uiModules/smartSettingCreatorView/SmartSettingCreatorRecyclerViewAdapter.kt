package com.smartsettings.ai.uiModules.smartSettingCreatorView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartsettings.ai.R

class SmartSettingCreatorRecyclerViewAdapter(
    private val smartSettingSchemaViewDataList: List<SmartSettingSchemaViewData>,
    val itemSelectedCallback: (SmartSettingSchemaViewData) -> Unit
) : RecyclerView.Adapter<SmartSettingCreatorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartSettingCreatorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_smart_setting_schema, parent, false)
        return SmartSettingCreatorViewHolder(view, itemSelectedCallback)
    }

    override fun getItemCount(): Int {
        return smartSettingSchemaViewDataList.size
    }

    override fun onBindViewHolder(holder: SmartSettingCreatorViewHolder, position: Int) {
        holder.setData(smartSettingSchemaViewDataList[position])
    }
}

class SmartSettingCreatorViewHolder(view: View, val itemSelectedCallback: (SmartSettingSchemaViewData) -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val smartSettingLayout: CardView = view.findViewById(R.id.smartSettingLayout)
    private val nameText: TextView = view.findViewById(R.id.nameText)

    fun setData(smartSettingSchemaViewData: SmartSettingSchemaViewData) {
        nameText.text = smartSettingSchemaViewData.name

        smartSettingLayout.setOnClickListener {
            itemSelectedCallback(smartSettingSchemaViewData)
        }
    }
}