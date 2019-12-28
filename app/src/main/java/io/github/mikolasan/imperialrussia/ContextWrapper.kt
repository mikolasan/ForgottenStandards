package io.github.mikolasan.imperialrussia

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.*

class ImperialContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, newLocale: Locale): ContextWrapper {
            var ctx = context
            val res = ctx.resources
            val configuration = res.configuration

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 24
                configuration.setLocale(newLocale)

                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)

                ctx = ctx.createConfigurationContext(configuration)

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // 17
                configuration.setLocale(newLocale)
                ctx = ctx.createConfigurationContext(configuration)

            } else {
                @Suppress("DEPRECATION")
                configuration.locale = newLocale
                @Suppress("DEPRECATION")
                res.updateConfiguration(configuration, res.displayMetrics)
            }

            return ContextWrapper(ctx)
        }
    }
}