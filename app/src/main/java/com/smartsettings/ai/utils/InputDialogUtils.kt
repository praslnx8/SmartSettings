package com.smartsettings.ai.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import com.smartsettings.ai.R


object InputDialogUtils {

    fun askConfirmation(
        context: Context,
        title: String,
        message: String,
        callBack: (isPositive: Boolean, value: Array<String>) -> Boolean
    ): Dialog {
        return ask(context, title, message, "Yes", "No", null, callBack)
    }

    fun askWithMessage(
        context: Context,
        message: String?,
        positiveBtnText: String,
        hintText: Array<String>,
        callBack: (isPositive: Boolean, value: Array<String>) -> Boolean
    ): Dialog {
        return ask(context, null, message, positiveBtnText, null, hintText, callBack)
    }

    fun ask(
        context: Context,
        title: String,
        positiveBtnText: String,
        hintText: Array<String>,
        callBack: (isPositive: Boolean, value: Array<String>) -> Boolean
    ): Dialog {
        return ask(context, title, null, positiveBtnText, null, hintText, callBack)
    }

    private fun ask(
        context: Context,
        title: String?,
        message: String?,
        positiveBtnText: String,
        negativeBtnText: String?,
        hintText: Array<String>?,
        callBack: (isPositive: Boolean, value: Array<String>) -> Boolean
    ): Dialog {

        var dialog: Dialog? = null

        val dialogBuilder = AlertDialog.Builder(context)
        title?.let {
            dialogBuilder.setTitle(it)
        }
        message?.let {
            dialogBuilder.setMessage(it)
        }

        val layout = LinearLayout(context)
        if (hintText != null) {

            layout.orientation = LinearLayout.VERTICAL
            for (hint in hintText) {
                val editText = EditText(context)
                editText.background = ActivityCompat.getDrawable(context, R.drawable.edit_text)
                editText.hint = hint
                layout.addView(editText)
            }

            dialogBuilder.setView(layout)
        }

        dialogBuilder.setPositiveButton(positiveBtnText) { _, _ ->
            val valueArrayList = arrayListOf<String>()
            for (i in 0 until layout.childCount) {
                val editText = layout.getChildAt(i) as EditText
                valueArrayList.add(editText.text.toString())
            }

            sendCallback(true, valueArrayList.toTypedArray(), callBack, dialog)
        }

        negativeBtnText?.let {

            dialogBuilder.setCancelable(false)
            dialogBuilder.setNegativeButton(negativeBtnText) { _, _ ->
                sendCallback(false, arrayOf(), callBack, dialog)
            }
        }

        dialogBuilder.setOnCancelListener {
            sendCallback(false, arrayOf(), callBack, dialog)
        }

        dialog = dialogBuilder.create()

        return dialog
    }

    private fun sendCallback(
        isPositive: Boolean,
        values : Array<String>,
        callBack: (isPositive: Boolean, value: Array<String>) -> Boolean,
        dialog: Dialog?
    ) {
        try {
            if (callBack(isPositive, values)) {
                dialog?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dialog?.dismiss()
        }
    }
}