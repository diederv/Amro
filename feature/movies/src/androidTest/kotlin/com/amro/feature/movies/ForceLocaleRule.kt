package com.amro.feature.movies

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.Locale

class ForceLocaleRule(val locale: Locale) : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                val originalLocale = Locale.getDefault()
                Locale.setDefault(locale)

                val config = InstrumentationRegistry.getInstrumentation()
                    .targetContext.resources.configuration
                config.setLocale(locale)

                InstrumentationRegistry.getInstrumentation()
                    .targetContext.resources.updateConfiguration(
                        config,
                        InstrumentationRegistry.getInstrumentation()
                            .targetContext.resources.displayMetrics
                    )

                try {
                    base.evaluate()
                } finally {
                    // Restore original locale after test
                    Locale.setDefault(originalLocale)
                }
            }
        }
    }
}