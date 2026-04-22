package com.amro.feature.settings

import androidx.activity.ComponentActivity

/**
 * Test host that swallows [recreate] so persistence + re-entry can be
 * exercised without tearing down the compose-rule activity. In production,
 * [LocaleManager.apply] recreates the activity so the new locale takes effect.
 */
class NoRecreateTestActivity : ComponentActivity() {
    override fun recreate() {
        // intentionally no-op
    }
}
