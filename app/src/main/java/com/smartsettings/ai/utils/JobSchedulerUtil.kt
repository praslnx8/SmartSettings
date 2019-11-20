package com.smartsettings.ai.utils

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.smartsettings.ai.cron.SchemaSyncService
import com.smartsettings.ai.di.DependencyProvider
import java.util.concurrent.TimeUnit

object JobSchedulerUtil {

    fun scheduleSyncJob() {
        val jobScheduler = DependencyProvider.getContext.getSystemService(Context.JOB_SCHEDULER_SERVICE)  as JobScheduler
        val jobInfo = JobInfo.Builder(1, ComponentName(DependencyProvider.getContext, SchemaSyncService::class.java))
            .setPeriodic(TimeUnit.HOURS.toMillis(4))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).build()
        jobScheduler.schedule(jobInfo)
    }
}