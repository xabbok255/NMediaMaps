package com.xabbok.nmediamaps.utils

import android.content.Context
import android.content.res.Configuration

fun isNightMode(context: Context): Boolean {
    val nightModeFlags = context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
}