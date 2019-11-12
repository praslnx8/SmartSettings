package com.smartsettings.ai.cron

import android.app.job.JobParameters
import android.app.job.JobService
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingSchemaRepo

class SchemaSyncService : JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        SmartSettingSchemaRepo().syncSchemaCompletely()
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        return false
    }

}