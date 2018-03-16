package com.nice.nice.utils

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast



/**
 * Created by jyothish on 3/17/18.
 */
object AppUtils {

    fun showToast(context: Context, @StringRes text: Int, isLong: Boolean) {
        showToast(context, context.getString(text), isLong)
    }

    fun showToast(context: Context, text: String, isLong: Boolean) {
        Toast.makeText(context, text, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }
}