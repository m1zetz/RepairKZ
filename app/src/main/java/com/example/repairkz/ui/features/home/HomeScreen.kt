package com.example.repairkz.ui.features.home

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.R

@OptIn(ExperimentalMaterial3Api::class)

fun HomeScreen(navController: NavController){
    val homeScreenViewModel: HomeViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        homeScreenViewModel.effectsChannel.collect {effect ->
            when(effect){
                is Effects.NavigateToSearch -> {
                    val route = if(effect.pattern.isEmpty()){
                        Routes.SEARCH
                    } else {
                        "${Routes.SEARCH}?pattern=${effect.pattern}"
                    }
                    navController.navigate(route)
                }


            }
        }
    }

    val mastersCards = listOf(
        CardFill(Icons.Default.Settings, R.string.plumber),
        CardFill(Icons.Default.Settings, R.string.electrician),
        CardFill(Icons.Default.Settings, R.string.computer),
        CardFill(Icons.Default.Settings, R.string.handyman),
        CardFill(Icons.Default.Settings, R.string.furniture)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RepairSearchBar(
            onClick = {
                homeScreenViewModel.handleIntent(HomeScreenIntent.ClickOnCard(""))
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(mastersCards) {
                CardOfMaster(homeScreenViewModel ,it)
            }
        }
    }
}

@Composable
fun RepairSearchBar(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(56.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Кого ищете?",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

    }
}



@Composable
fun CardOfMaster(homeViewModel: HomeViewModel,cardFill: CardFill) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp)
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .clickable {
                val patternName = context.getString(cardFill.name)
                homeViewModel.handleIntent(HomeScreenIntent.ClickOnCard(patternName))
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = cardFill.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(text = stringResource(cardFill.name))
        }
    }
}

data class CardFill(
    val icon: ImageVector,
    @StringRes val name: Int
)