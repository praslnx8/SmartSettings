package com.smartsettings.ai.uiModules.smartSettingView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartsettings.ai.R
import com.smartsettings.ai.runner.MainForeGroundService
import com.smartsettings.ai.uiModules.uiModels.SmartSettingViewData
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class SmartSettingViewFragment : Fragment(), SmartSettingView {

    private var smartSettingViewPresenter: SmartSettingViewPresenter? = null

    private var adapter = SmartSettingRecyclerViewAdapter({
        smartSettingViewPresenter?.smartSettingChangedFromUser(it)
    }, {
        smartSettingViewPresenter?.deleteSmartSetting(it)
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        smartSettingViewPresenter = ViewModelProviders.of(this).get(SmartSettingViewModel::class.java)
        smartSettingViewPresenter?.setHomeView(this)
        smartSettingViewPresenter?.getSmartSettings(this)


        context?.let {
            MainForeGroundService.startService(it)
            view.recyclerView.layoutManager = LinearLayoutManager(it)
            view.recyclerView.adapter = adapter
        }
        return view
    }

    override fun showSmartSettings(smartSettingViewDataList: List<SmartSettingViewData>) {
        adapter.setData(smartSettingViewDataList)
        emptyLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun showEmptyView() {
        emptyLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
}