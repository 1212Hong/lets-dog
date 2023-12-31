package com.dog.ui.components

import android.content.Context
import android.widget.Toast

fun showCustomToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}
