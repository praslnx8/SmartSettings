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
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.SmartSettingSchemaListActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.lang.ref.WeakReference

class SmartSettingViewFragment : Fragment(), SmartSettingView {

    private val smartSettingViewPresenter: SmartSettingViewPresenter by lazy {
        ViewModelProviders.of(this).get(SmartSettingViewModel::class.java)
    }

    private var adapter = SmartSettingRecyclerViewAdapter({
        smartSettingViewPresenter.updateSmartSetting(it)
    }, {
        smartSettingViewPresenter.deleteSmartSetting(it)
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        smartSettingViewPresenter.setHomeView(WeakReference(this))
        smartSettingViewPresenter.getSmartSettings(this)


        context?.let { context ->
            MainForeGroundService.startService(context)
            view.recyclerView.layoutManager = LinearLayoutManager(context)
            view.recyclerView.adapter = adapter


            view.fab.setOnClickListener {
                SmartSettingSchemaListActivity.open(context)
            }
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