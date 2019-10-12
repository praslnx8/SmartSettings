package com.smartsettings.ai.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout


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

    fun ask(
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

            if (callBack(true, valueArrayList.toTypedArray())) {
                dialog?.dismiss()
            }
        }

        negativeBtnText?.let {

            dialogBuilder.setCancelable(false)
            dialogBuilder.setNegativeButton(negativeBtnText) { _, _ ->
                if (callBack(false, arrayOf())) {
                    dialog?.dismiss()
                }
            }
        }

        dialog = dialogBuilder.create()

        return dialog
    }
}