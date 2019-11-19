package com.smartsettings.ai.uiModules.smartSettingSchemaChooserView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartsettings.ai.R
import kotlinx.android.synthetic.main.activity_smart_setting_schema_list.*
import java.lang.ref.WeakReference


class SmartSettingSchemaListActivity : AppCompatActivity(), SmartSettingSchemaView {


    companion object {
        fun open(context: Context) {
            val intent = Intent(context, SmartSettingSchemaListActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val smartSettingSchemaPresenter: SmartSettingSchemaPresenter by lazy {
        ViewModelProviders.of(this).get(SmartSettingSchemaListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_setting_schema_list)
        smartSettingSchemaPresenter.setView(WeakReference(this))
    }

    override fun showSmartSettingSchemas(smartSettingSchemas: List<SmartSettingSchemaViewData>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            SmartSettingCreatorRecyclerViewAdapter(smartSettingSchemas) { item ->
                //TODO
            }
        recyclerView.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    override fun showLoading() {
        recyclerView.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun showUnableToFetchInfo() {
        recyclerView.visibility = View.GONE
        emptyLayout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (!supportFragmentManager.popBackStackImmediate()) {
            super.onBackPressed()
        }
    }

    override fun close() {
        finish()
    }
}