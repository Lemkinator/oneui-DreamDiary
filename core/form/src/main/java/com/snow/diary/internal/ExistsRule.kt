package com.snow.diary.internal;

import android.content.Context
import com.snow.diary.form.R
import com.snow.diary.form.Rule

object ExistsRule : Rule {
    override fun isValid(str: String?): Boolean =
        !str.isNullOrBlank()

    override fun error(context: Context): String =
        context
            .resources
            .getString(R.string.form_lenght_error)
}