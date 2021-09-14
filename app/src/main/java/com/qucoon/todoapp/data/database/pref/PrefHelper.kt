package com.qucoon.todoapp.data.database.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

interface PrefHelper {
    fun isFirstRun(): Boolean
    fun setFirstRun(boolean: Boolean)
}
class AppPrefs @Inject constructor(
    private val mContext: Context,
): PrefHelper{
    private val sharedPreferences: SharedPreferences = mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)

    override fun isFirstRun(): Boolean {
        return sharedPreferences.getBoolean(FIRST_RUN, true)
    }

    override fun setFirstRun(boolean: Boolean) {
        sharedPreferences.edit { putBoolean(FIRST_RUN, boolean) }
    }

    companion object {
        private const val FIRST_RUN = "first_run"
    }
}