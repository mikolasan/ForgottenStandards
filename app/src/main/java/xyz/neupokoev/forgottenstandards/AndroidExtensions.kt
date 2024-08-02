package xyz.neupokoev.forgottenstandards

import android.os.Build
import android.text.Html
import android.view.View
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

fun TextView.setHtml(s: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(s, Html.FROM_HTML_MODE_COMPACT);
    } else {
        this.text = Html.fromHtml(s);
    }
}

fun View.getColor(resourceId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // >= (API 23) Android 6.0 Marshmallow
        val theme = null
        context.resources.getColor(resourceId, theme)
    } else {
        @Suppress("DEPRECATION")
        context.resources.getColor(resourceId)
    }
}