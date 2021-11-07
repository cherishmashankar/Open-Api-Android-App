package com.example.android.open_api_android_app.openapi.ui

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.example.android.open_api_android_app.R


fun Context.displayToast(@StringRes message: Int){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.displayToast(message: String ){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.displaySuccessDialog(message: String){
    MaterialDialog(this).show{
        title(R.string.text_success)
        message(text = message)
        positiveButton(R.string.text_ok)
    }
}

fun Context.displayErrorDialog(message: String ){
    MaterialDialog(this).show{
        title(R.string.text_error)
        message(text = message)
        positiveButton(R.string.text_ok)
    }
}
