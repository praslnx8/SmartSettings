package com.smartsettings.ai.uiModules.smartSettingCreatorView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartsettings.ai.R

class SmartSettingCreatorRecyclerViewAdapter(
    private val smartSettingSchemaViewDataList: List<SmartSettingSchemaViewData>,
    val itemSelectedCallback: (SmartSettingSchemaViewData, Boolean) -> Unit
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

class SmartSettingCreatorViewHolder(view: View, val itemSelectedCallback: (SmartSettingSchemaViewData, Boolean) -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val smartSettingLayout: CardView = view.findViewById(R.id.smartSettingLayout)
    private val nameText: TextView = view.findViewById(R.id.nameText)
    private val descText: TextView = view.findViewById(R.id.descText)
    private val activateButton: Button = view.findViewById(R.id.activateButton)

    fun setData(smartSettingSchemaViewData: SmartSettingSchemaViewData) {
        nameText.text = smartSettingSchemaViewData.name
        if(smartSettingSchemaViewData.description?.isNotBlank() == true) {
            descText.text = smartSettingSchemaViewData.description
            descText.visibility = View.VISIBLE
        } else {
            descText.visibility = View.GONE
        }

        activateButton.setOnClickListener {
            itemSelectedCallback(smartSettingSchemaViewData, true)
        }

        smartSettingLayout.setOnClickListener {
            itemSelectedCallback(smartSettingSchemaViewData, false)
        }
    }
}