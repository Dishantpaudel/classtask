package com.example.classtask.repository

import android.app.Activity
import android.content.Context

private const val PREFERENCE_DARK_THEME = "dark_theme"
private const val PREFERENCE_DARK_THEME_DEFAULT = -1

class AppPreferences(context: Activity) {

    private val preferences = context.getSharedPreferences("unsplash_prefs", Context.MODE_PRIVATE)

    fun setDarkTheme(isDarkThemeOn: Boolean) {
        with(preferences.edit()) {
            putInt(
                PREFERENCE_DARK_THEME,
                if (isDarkThemeOn) 1 else 0
            )
            apply()
        }
    }

    fun isDarkTheme(): Int {
        return preferences.getInt(
            PREFERENCE_DARK_THEME,
            PREFERENCE_DARK_THEME_DEFAULT
        )
    }
}
