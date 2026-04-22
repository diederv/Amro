package com.amro.feature.settings

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import java.util.Locale

enum class AppLanguage(val tag: String, val displayName: String) {
    ENGLISH("en", "English"),
    DUTCH("nl", "Nederlands"),
    GERMAN("de", "Deutsch"),
    PORTUGUESE("pt", "Português"),
    SPANISH("es", "Español");

    companion object {
        fun fromTag(tag: String?): AppLanguage? =
            entries.firstOrNull { it.tag.equals(tag, ignoreCase = true) }
    }
}

object LocaleManager {
    private const val PREFS = "app_locale_prefs"
    private const val KEY_LANGUAGE_TAG = "language_tag"

    fun initialSelection(context: Context): AppLanguage {
        stored(context)?.let { return it }
        val systemTag = Resources.getSystem().configuration.locales[0]?.language
        return AppLanguage.fromTag(systemTag) ?: AppLanguage.ENGLISH
    }

    fun apply(activity: Activity, language: AppLanguage) {
        persist(activity, language)
        activity.recreate()
    }

    fun wrap(base: Context): Context {
        val language = stored(base) ?: return base
        val locale = Locale.forLanguageTag(language.tag)
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        config.setLocales(LocaleList(locale))
        return base.createConfigurationContext(config)
    }

    private fun stored(context: Context): AppLanguage? {
        val tag = context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE_TAG, null)
        return AppLanguage.fromTag(tag)
    }

    private fun persist(context: Context, language: AppLanguage) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE_TAG, language.tag)
            .apply()
    }
}
