package io.github.tiagonuneslx.pokeland.ui.screen.details

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import io.github.tiagonuneslx.pokeland.data.model.Pokemon
import io.github.tiagonuneslx.pokeland.data.sample.PokemonSamples
import io.github.tiagonuneslx.pokeland.ui.screen.SharedViewModel
import io.github.tiagonuneslx.pokeland.ui.theme.Belligan
import io.github.tiagonuneslx.pokeland.ui.theme.Playtime
import io.github.tiagonuneslx.pokeland.ui.theme.PokelandTheme
import io.github.tiagonuneslx.pokeland.ui.theme.Roboto
import io.github.tiagonuneslx.pokeland.util.TriangleShape
import io.github.tiagonuneslx.pokeland.util.thenIf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val pokemon by sharedViewModel.selectedPokemonFlow.collectAsState()
    val moves by viewModel.movesFlow.collectAsState()
    val evolutions by viewModel.evolutionsFlow.collectAsState()
    LaunchedEffect(pokemon) {
        snapshotFlow { pokemon }
            .filterNotNull()
            .collect { pokemon ->
                viewModel.setPokemon(pokemon)
            }
    }
    DetailsScreen(pokemon, moves, evolutions, back = { navController.popBackStack() })
}

@Composable
fun DetailsScreen(
    pokemon: Pokemon?,
    moves: List<Pokemon.Move>?,
    evolutions: List<Pokemon.Evolution>?,
    back: () -> Unit,
) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    LaunchedEffect(window) {
        delay(500)
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
    }
    Surface(
        color = pokemon?.types?.first()?.color ?: Pokemon.Type.Unknown.color,
        contentColor = Color.Black,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            val orientation = LocalConfiguration.current.orientation
            Header(pokemon, back)
            Card(
                pokemon,
                moves,
                evolutions,
                orientation,
            )
            if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
                AsyncImage(
                    model = pokemon?.imageUrl,
                    contentDescription = pokemon?.name,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 72.dp)
                        .size(172.dp)
                )
            }
        }
    }
}

@Composable
private fun Card(
    pokemon: Pokemon?,
    moves: List<Pokemon.Move>?,
    evolutions: List<Pokemon.Evolution>?,
    orientation: Int,
) {
    Surface(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 72.dp else 200.dp)
    ) {
        val pages = listOf("About", "Moves", "Stats", "Evolutions")
        val pagerState = rememberPagerState { pages.size }
        val coroutineScope = rememberCoroutineScope()

        Column(Modifier.padding(top = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 16.dp else 40.dp)) {
            Tabs(pagerState, pages, coroutineScope)

            HorizontalPager(pagerState) { page ->
                when (page) {
                    0 -> About(pokemon)
                    1 -> Moves(moves)
                    2 -> Stats(pokemon)
                    3 -> Evolutions(pokemon, evolutions)
                }
            }
        }
    }
}

@Composable
private fun About(pokemon: Pokemon?) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 40.dp, bottom = 16.dp)
            .padding(horizontal = 32.dp)
    ) {
        val fields = listOf(
            "Bio" to pokemon?.description,
            "Types" to pokemon?.types?.joinToString(),
            "Height" to pokemon?.height?.let { "$it m" },
            "Weight" to pokemon?.weight?.let { "$it kg" },
            "XP" to pokemon?.baseExperience?.toString(),
        )
        fields.forEachIndexed { index, (label, value) ->
            AboutField(label, value)
            val isLast = index == fields.size - 1
            if (!isLast) {
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun Moves(moves: List<Pokemon.Move>?) {
    LazyColumn(
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (moves != null) {
            items(moves, key = { it.name }) { move ->
                Move(move)
            }
        } else {
            items(10) {
                Move(null)
            }
        }
    }
}

@Composable
private fun Stats(pokemon: Pokemon?) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 40.dp, bottom = 16.dp)
            .padding(horizontal = 32.dp)
    ) {
        val fields = listOf(
            "HP" to pokemon?.stats?.hp,
            "ATK" to pokemon?.stats?.attack,
            "DEF" to pokemon?.stats?.defense,
            "SP.ATK" to pokemon?.stats?.specialAttack,
            "SP.DEF" to pokemon?.stats?.specialDefense,
            "SPEED" to pokemon?.stats?.speed,
        )
        fields.forEachIndexed { index, (label, value) ->
            Stat(label, value)
            val isLast = index == fields.size - 1
            if (!isLast) {
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun Evolutions(pokemon: Pokemon?, evolutions: List<Pokemon.Evolution>?) {
    LazyColumn(
        contentPadding = PaddingValues(top = 24.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        if (evolutions != null) {
            items(evolutions) { move ->
                Evolution(pokemon, move)
            }
        } else {
            items(5) {
                Evolution(null, null)
            }
        }
    }
}

@Composable
fun Evolution(pokemon: Pokemon?, evolution: Pokemon.Evolution?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .placeholder(visible = evolution == null, highlight = PlaceholderHighlight.fade())
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = pokemon?.imageUrl,
                contentDescription = pokemon?.name,
                modifier = Modifier
                    .size(80.dp)
            )
            Text(pokemon?.name ?: "")
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Surface(
                    color = MaterialTheme.colors.primaryVariant,
                    shape = RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp),
                    modifier = Modifier
                        .height(4.dp)
                        .weight(1f)
                ) {}
                Surface(
                    color = MaterialTheme.colors.primaryVariant,
                    shape = TriangleShape,
                    modifier = Modifier
                        .size(12.dp)
                        .rotate(90f)
                ) {}
            }
            Text(
                evolution?.minLevel?.let { "Min Level: $it" } ?: "",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = evolution?.evolvesTo?.imageUrl,
                contentDescription = evolution?.evolvesTo?.name,
                modifier = Modifier
                    .size(80.dp)
            )
            Text(evolution?.evolvesTo?.name ?: "")
        }
    }
}

@Composable
private fun Stat(label: String, value: Int?) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            fontFamily = Roboto,
            fontSize = 14.sp,
            color = Color(0xFF8B8B8B),
            modifier = Modifier.width(68.dp)
        )
        Text(
            value?.toString() ?: "",
            fontFamily = Roboto,
            fontSize = 14.sp,
            modifier = Modifier
                .width(40.dp)
        )
        LinearProgressIndicator(
            progress = (value ?: 0) / 255f,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Move(move: Pokemon.Move?) {
    Surface(
        color = move?.type?.color ?: Pokemon.Type.Unknown.color,
        contentColor = Color.Black,
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .fillMaxWidth()
            .thenIf(move == null) {
                Modifier.height(92.dp)
            }
            .placeholder(visible = move == null, highlight = PlaceholderHighlight.fade())
    ) {
        if (move != null) {
            Box(
                Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        move.name,
                        fontFamily = Roboto,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        move.description ?: "",
                        fontFamily = Roboto,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "POWER: ${move.pp}",
                            fontFamily = Playtime,
                            fontSize = 14.sp,
                            modifier = Modifier.width(88.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "PP: ${move.pp}",
                            fontFamily = Playtime,
                            fontSize = 14.sp,
                            modifier = Modifier.width(60.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (move.accuracy == null) "–" else "${move.accuracy} %",
                            fontFamily = Playtime,
                            fontSize = 14.sp,
                            modifier = Modifier.width(54.dp),
                        )
                        Text(
                            move.type.name,
                            fontFamily = Playtime,
                            fontSize = 14.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.width(64.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutField(label: String, value: String?) {
    Row(Modifier.fillMaxWidth()) {
        Text(
            label,
            fontFamily = Roboto,
            fontSize = 14.sp,
            color = Color(0xFF8B8B8B),
            modifier = Modifier.width(80.dp)
        )
        Text(
            value ?: "",
            fontFamily = Roboto,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun Tabs(
    pagerState: PagerState,
    pages: List<String>,
    coroutineScope: CoroutineScope
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .height(5.dp)
                    .requiredWidth(40.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colors.primaryVariant)
            )
        },
        backgroundColor = MaterialTheme.colors.surface,
        divider = {},
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        pages.forEachIndexed { index, title ->
            Text(
                title,
                textAlign = TextAlign.Center,
                fontFamily = Belligan,
                fontSize = 18.sp,
                color = if (isSystemInDarkTheme()) Color(0xFFD5D5D5) else Color(0xFF525252),
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun Header(
    pokemon: Pokemon?,
    back: () -> Unit,
) {
    Box {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Text(pokemon?.number?.let { "#$it" } ?: "", fontFamily = Playtime, fontSize = 30.sp)
            Box(
                Modifier
                    .clip(CircleShape)
                    .size(44.dp)
                    .background(Color(0xBDFFFFFF))
                    .clickable { back() }
            ) {
                Icon(
                    Icons.Default.Close, "close",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp)
                )
            }
        }
        Text(
            pokemon?.name ?: "",
            fontFamily = Belligan,
            fontSize = 30.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    PokelandTheme {
        DetailsScreen(
            PokemonSamples.Bulbasaur,
            listOf(PokemonSamples.Moves.Pound),
            listOf(),
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    PokelandTheme {
        About(
            PokemonSamples.Bulbasaur,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MovesPreview() {
    PokelandTheme {
        Moves(
            listOf(PokemonSamples.Moves.Pound),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EvolutionsPreview() {
    PokelandTheme {
        Evolutions(
            PokemonSamples.Bulbasaur,
            listOf(
                Pokemon.Evolution(
                    PokemonSamples.Ivysaur,
                    18,
                )
            ),
        )
    }
}

