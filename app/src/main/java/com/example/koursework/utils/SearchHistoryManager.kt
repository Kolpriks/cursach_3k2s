package com.example.koursework.utils

import android.content.Context

object SearchHistoryManager {
    private const val PREFS_NAME = "search_prefs"
    private const val KEY_HISTORY = "search_history"
    private const val DELIMITER = "|;|"
    private const val MAX_SIZE = 10

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getHistory(context: Context): List<String> {
        val raw = prefs(context).getString(KEY_HISTORY, "") ?: ""
        if (raw.isBlank()) return emptyList()
        return raw.split(DELIMITER)
    }

    fun addQuery(context: Context, query: String) {
        if (query.isBlank()) return
        val list = getHistory(context).toMutableList()
        list.remove(query)
        list.add(0, query)
        if (list.size > MAX_SIZE) list.subList(MAX_SIZE, list.size).clear()
        prefs(context).edit()
            .putString(KEY_HISTORY, list.joinToString(DELIMITER))
            .apply()
    }

    fun clearHistory(context: Context) {
        prefs(context).edit()
            .remove(KEY_HISTORY)
            .apply()
    }
}
