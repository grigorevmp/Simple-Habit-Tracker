package com.grigorevmp.habits.core.utils

import android.content.Context
import com.grigorevmp.habits.R

object Changelogs {

    const val version = 2

    fun getVersions(context: Context) = arrayListOf(
        context.getString(R.string.changelog_v_1),
        context.getString(R.string.changelog_v_2),
    )
}