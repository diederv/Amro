package com.amro.feature.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.amro.core.designsystem.theme.AmroTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsLanguagePersistenceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<NoRecreateTestActivity>()

    @Before
    fun seedEnglishAsPersistedLocale() {
        // Force a known starting point so the initial selection is deterministic
        // regardless of the device locale.
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("app_locale_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("language_tag", "en")
            .commit()
    }

    @Test
    fun selectedLanguagePersistsAcrossReentry() {
        var settingsVisible by mutableStateOf(true)

        composeTestRule.setContent {
            AmroTheme(darkTheme = true) {
                if (settingsVisible) {
                    SettingsScreen(onBack = { settingsVisible = false })
                }
            }
        }

        // Baseline: English is the currently selected language.
        composeTestRule.onNode(isSelectable() and hasText("English"))
            .assertIsSelected()

        // Select a different language — this persists the choice via LocaleManager.
        composeTestRule.onNodeWithText("Nederlands").performClick()

        // Press back: remove the screen from composition.
        composeTestRule.runOnUiThread { settingsVisible = false }
        composeTestRule.waitForIdle()

        // Re-open settings: a fresh composition reads the persisted selection.
        composeTestRule.runOnUiThread { settingsVisible = true }
        composeTestRule.waitForIdle()

        // Nederlands is now the selected language.
        composeTestRule.onNode(isSelectable() and hasText("Nederlands"))
            .assertIsSelected()
    }
}
