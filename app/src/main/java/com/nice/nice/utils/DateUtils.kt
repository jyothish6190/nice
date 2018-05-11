package com.nice.nice.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        fun getDateTime(): String {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val date = Date()
            return dateFormat.format(date)
        }
    }
}