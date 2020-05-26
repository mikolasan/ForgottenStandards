package io.github.mikolasan.imperialrussia

import android.os.Build
import android.widget.TextView

fun TextView.setTextColorId(res: Int) {
    val colorInt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // >= (API 23) Android 6.0 Marshmallow
        val theme = null
        context.resources.getColor(res, theme)
    } else {
        @Suppress("DEPRECATION")
        context.resources.getColor(res)
    }
    this.setTextColor(colorInt)
}