package com.example.koursework.ui.outbox

import android.content.Context
import android.content.SharedPreferences

object AppState {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_ROLE = "role"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_EMAIL = "email"

    var isDarkTheme: Boolean = false

    /** Вызывается при успешном логине. */
    fun logIn(context: Context, userId: Long, role: String, email: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_ROLE, role)
            .putString(KEY_EMAIL, email)           // ← сохраняем email
            .apply()
    }

    /** Вызывается при выходе: очищаем данные. */
    fun logOut(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    /** Проверка, залогинен ли пользователь. */
    fun isLoggedIn(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }


    fun getRole(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ROLE, null)

    /** Получить email текущего пользователя. */
    fun getEmail(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_EMAIL, null)

    /** Получить ID текущего пользователя. */
    fun getUserId(context: Context): Long? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return if (prefs.contains(KEY_USER_ID)) prefs.getLong(KEY_USER_ID, -1L) else null
    }

    /** Переключение темы. */
    fun switchTheme() {
        isDarkTheme = !isDarkTheme
    }
}
