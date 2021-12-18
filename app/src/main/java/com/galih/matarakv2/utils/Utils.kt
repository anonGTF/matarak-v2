package com.galih.matarakv2.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun Boolean.toGender(): String = if (this) "Laki-laki" else "Perempuan"

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}