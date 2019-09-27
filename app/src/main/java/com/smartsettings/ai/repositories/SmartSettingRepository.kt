package com.smartsettings.ai.repositories

import android.content.Context
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.resources.db.SmartSettingDBModel
import com.smartsettings.ai.resources.db.SmartSettingDao
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.inject.Inject


class SmartSettingRepository {

    @Inject
    lateinit var smartSettingDao: SmartSettingDao

    @Inject
    lateinit var context: Context

    init {
        SmartApp.appComponent.inject(this)
    }

    fun getSmartSettings(smartSettingsCallBack: (List<SmartSetting<out Any>>) -> Unit) {

        doAsync {

            val smartSettings = ArrayList<SmartSetting<out Any>>()

            val smartSettingsFromDb = smartSettingDao.getSmartSettings()

            for (smartSettingDbData in smartSettingsFromDb) {
                if (smartSettingDbData.id != null) {
                    if (smartSettingDbData.type == "LOC") {
                        val locationBasedVolumeSetting =
                            getSmartSettingFromPersist<LocationBasedVolumeSetting>(smartSettingDbData.serializedObjectFileName)
                        if (locationBasedVolumeSetting != null) {
                            smartSettings.add(locationBasedVolumeSetting)
                        }
                    }
                }
            }

            uiThread {
                smartSettingsCallBack(smartSettings)
            }
        }
    }

    fun addSmartSetting(smartSetting: SmartSetting<out Any>) {

        val smartSettingDBModel = SmartSettingDBModel(1, "LOC", persistSmartSetting(smartSetting), 1)

        doAsync {
            smartSettingDao.insertSmartSetting(smartSettingDBModel)
        }
    }

    private fun persistSmartSetting(smartSetting: SmartSetting<out Any>): String {
        val fos = context.openFileOutput(smartSetting.id.toString(), Context.MODE_PRIVATE)
        val os = ObjectOutputStream(fos)
        os.writeObject(smartSetting)
        os.close()
        fos.close()

        return smartSetting.id.toString()
    }

    private fun <T> getSmartSettingFromPersist(fileName: String): T? {
        try {
            val fis = context.openFileInput(fileName)
            val `is` = ObjectInputStream(fis)
            val simpleClass = `is`.readObject() as T
            `is`.close()
            fis.close()

            return simpleClass
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}