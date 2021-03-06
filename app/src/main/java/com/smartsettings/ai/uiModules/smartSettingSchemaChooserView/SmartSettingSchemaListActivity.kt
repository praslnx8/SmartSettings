package com.smartsettings.ai.uiModules.smartSettingSchemaChooserView

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartsettings.ai.R
import com.smartsettings.ai.uiModules.smartSettingCreatorView.SmartSettingCreatorActivity
import kotlinx.android.synthetic.main.activity_smart_setting_schema_list.*
import kotlinx.android.synthetic.main.item_toolbar.*
import java.lang.ref.WeakReference


class SmartSettingSchemaListActivity : AppCompatActivity(), SmartSettingSchemaView {

    private val reqForCreateSetting = 21

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
        smartSettingSchemaPresenter.getSmartSettingSchemas()

        titleText.text = "Choose Smart Setting"
        backArrowButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun showSmartSettingSchemas(smartSettingSchemas: List<SmartSettingSchemaViewData>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            SmartSettingCreatorRecyclerViewAdapter(smartSettingSchemas) { item ->
                SmartSettingCreatorActivity.open(this, item, reqForCreateSetting)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqForCreateSetting) {
            if (resultCode == Activity.RESULT_OK) {
                finish()
            } else {
                Toast.makeText(this, "Unable to create setting", Toast.LENGTH_LONG).show()
            }
        }
    }
}