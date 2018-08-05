package com.morozione.azotova.ui.dialog

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog

import com.morozione.azotova.R

object DialogFactory {
    fun createDialogSelectMakingImage(
            context: Context, listCallback: MaterialDialog.ListCallback): MaterialDialog.Builder {

        return MaterialDialog.Builder(context)
                .items(context.getString(R.string.gallery), context.getString(R.string.camera))
                .itemsCallback(listCallback)
    }
}
