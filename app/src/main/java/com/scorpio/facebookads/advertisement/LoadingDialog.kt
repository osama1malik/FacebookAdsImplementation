package com.scorpio.facebookads.advertisement

import android.app.Dialog
import android.content.Context
import com.scorpio.facebookads.R

object LoadingDialog {

    var dialog: Dialog? = null

    fun showLoadingDialog(context: Context) {
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.dialog_loading)

        dialog?.show()
    }

    fun hideLoadingDialog() {
        dialog?.dismiss()
    }

}