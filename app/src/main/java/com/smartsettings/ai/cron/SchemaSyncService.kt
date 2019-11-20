package com.smartsettings.ai.cron

import android.app.job.JobParameters
import android.app.job.JobService
import com.smartsettings.ai.core.smartSettingSchemaProvider.SmartSettingSchemaProvider

class SchemaSyncService : JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        SmartSettingSchemaProvider.syncSmartSettingSchema()
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        return false
    }

}