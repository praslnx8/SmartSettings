package com.smartsettings.ai.uiModules.smartSettingCreatorView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartsettings.ai.R
import kotlinx.android.synthetic.main.fragment_smart_setting_creator_list.*

class SmartSettingCreatorListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_smart_setting_creator_list, container, false)
    }

    fun showSmartSettingSchemas(
        smartSettingSchemas: List<SmartSettingSchemaViewData>,
        itemSelectedCallback: (SmartSettingSchemaViewData, Boolean) -> Unit
    ) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SmartSettingCreatorRecyclerViewAdapter(smartSettingSchemas) { item, isActivate ->
            itemSelectedCallback(item, isActivate)
        }
        recyclerView.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    fun showLoading() {
        recyclerView.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun showUnableToFetchInfo() {
        recyclerView.visibility = View.GONE
        emptyLayout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }
}

class SmartSettingSchemaDetailFragment : Fragment()