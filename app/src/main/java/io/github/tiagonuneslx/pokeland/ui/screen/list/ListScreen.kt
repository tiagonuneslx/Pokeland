package io.github.tiagonuneslx.pokeland.ui.screen.list

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import io.github.tiagonuneslx.pokeland.R
import io.github.tiagonuneslx.pokeland.data.model.Pokemon
import io.github.tiagonuneslx.pokeland.data.sample.PokemonSamples
import io.github.tiagonuneslx.pokeland.ui.screen.SharedViewModel
import io.github.tiagonuneslx.pokeland.ui.theme.Belligan
import io.github.tiagonuneslx.pokeland.ui.theme.PokelandTheme
import io.github.tiagonuneslx.pokeland.util.items
import io.github.tiagonuneslx.pokeland.util.thenIf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    listViewModel: ListViewModel = hiltViewModel(),
) {
    val allPokemon = listViewModel.pokemonPagedFlow.collectAsLazyPagingItems()
    val shouldShowStaticPlaceholders by listViewModel.shouldShowStaticPlaceholdersFlow.collectAsState()
    ListScreen(
        allPokemon,
        shouldShowStaticPlaceholders,
        stopShowingStaticPlaceholders = {
            listViewModel.stopShowingStaticPlaceholders()
        },
        selectPokemon = { pokemon ->
            sharedViewModel.selectPokemon(pokemon)
            if (navController.currentDestination?.route == "list") {
                navController.navigate("details")
            }
        },
    )
}

@Composable
fun ListScreen(
    allPokemon: LazyPagingItems<Pokemon>,
    shouldShowStaticPlaceholders: Boolean,
    stopShowingStaticPlaceholders: suspend () -> Unit,
    selectPokemon: suspend (Pokemon) -> Unit,
) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val isDarkMode = isSystemInDarkTheme()
    LaunchedEffect(window) {
        delay(500)
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkMode
    }
    LaunchedEffect(shouldShowStaticPlaceholders, allPokemon.itemCount) {
        if (shouldShowStaticPlaceholders && allPokemon.itemCount > 0) {
            stopShowingStaticPlaceholders()
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            Modifier
                .padding(top = 24.dp)
        ) {
            Header()
            Spacer(Modifier.height(8.dp))
            val coroutineScope = rememberCoroutineScope()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    top = 8.dp,
                    bottom = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (allPokemon.itemCount > 0) {
                    items(allPokemon, key = { it.id }) { pokemon ->
                        PokemonCard(pokemon) {
                            pokemon ?: return@PokemonCard
                            coroutineScope.launch {
                                selectPokemon(pokemon)
                            }
                        }
                    }
                } else if (shouldShowStaticPlaceholders) {
                    items(30) {
                        PokemonCard(null) {}
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonCard(
    pokemon: Pokemon?,
    onClick: (String) -> Unit,
) {
    val cardShape = RoundedCornerShape(8.dp)
    Box(
        Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clip(cardShape)
    ) {
        if (pokemon != null) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-2).dp)
                    .zIndex(1f)
            )
        }
        val typeColor by remember(pokemon?.types) {
            derivedStateOf {
                pokemon?.types?.firstOrNull()?.color ?: Pokemon.Type.Unknown.color
            }
        }
        Surface(
            color = typeColor,
            contentColor = Color.Black,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .thenIf(pokemon != null) { Modifier.clickable { onClick(pokemon!!.id) } }
                .placeholder(visible = pokemon == null, highlight = PlaceholderHighlight.fade())
        ) {
            if (pokemon != null) {
                Box {
                    Text(
                        text = pokemon.name,
                        fontFamily = Belligan,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Image(
            painterResource(id = R.drawable.ic_pokeball),
            null,
            modifier = Modifier.size(34.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(stringResource(R.string.app_name), fontFamily = Belligan, fontSize = 30.sp)
    }
}

@Preview
@Composable
fun ListScreenPreview() {
    PokelandTheme {
        ListScreen(
            flowOf(PagingData.from(PokemonSamples.PokemonList)).collectAsLazyPagingItems(),
            true,
            {},
            {},
        )
    }
}
