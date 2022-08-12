package io.github.tiagonuneslx.pokeland.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.tiagonuneslx.pokeland.data.sample.PokemonSamples
import io.github.tiagonuneslx.pokeland.ui.screen.list.ListScreen
import io.github.tiagonuneslx.pokeland.ui.theme.GrassColor
import io.github.tiagonuneslx.pokeland.ui.theme.PokelandTheme
import io.github.tiagonuneslx.pokeland.util.assertBackgroundColor
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun listScreen() {
        composeTestRule.setContent {
            PokelandTheme {
                ListScreen(
                    flowOf(PagingData.from(PokemonSamples.PokemonList)).collectAsLazyPagingItems(),
                    true,
                    {},
                    {},
                )
            }
        }
        composeTestRule.onNodeWithText("PokeLand").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bulbasaur").assertBackgroundColor(GrassColor)
    }
}