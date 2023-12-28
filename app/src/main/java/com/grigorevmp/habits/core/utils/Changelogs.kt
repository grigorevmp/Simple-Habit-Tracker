package com.grigorevmp.habits.core.utils

import android.content.Context
import com.grigorevmp.habits.R

object Changelogs {

    const val version = 1

    fun getVersions(context: Context) = arrayListOf(
        context.getString(R.string.changelog_v_1)
    )
}