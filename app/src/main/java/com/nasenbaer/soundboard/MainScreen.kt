package com.nasenbaer.soundboard

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nasenbaer.soundboard.data.Sound
import com.nasenbaer.soundboard.ui.AddSoundDialog
import com.nasenbaer.soundboard.ui.ButtonsScreen

enum class MainScreen(@StringRes val title: Int) {
    Main(title = R.string.app_name)
}

@Composable
fun SoundBoardApp(
    viewModel: MainViewModel, navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MainScreen.valueOf(
        backStackEntry?.destination?.route ?: MainScreen.Main.name
    )

    StartScreenScaffold(navController, currentScreen, viewModel)
}

@Composable
fun StartScreenScaffold(
    navController: NavHostController, currentScreen: MainScreen, viewModel: MainViewModel
) {
    viewModel.showDialog = remember { mutableStateOf(false) }
    viewModel.soundsList = remember { mutableStateListOf<Sound>() }

    if (viewModel.showDialog.value) {
        AddSoundDialog(viewModel)
    }

    Scaffold(topBar = {
        MainAppBar(currentScreen)
    }, floatingActionButton = {
        FloatingActionButton(onClick = { viewModel.showDialog.value = true }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController, startDestination = MainScreen.Main.name,
                // "magic" margins to match the same spacing as in AddSoundDialog
                modifier = Modifier.padding(top = 12.dp, end = 16.dp)
            ) {
                composable(route = MainScreen.Main.name) {

                    LaunchedEffect(viewModel.soundsList) {
                        viewModel.loadSounds()
                    }

                    val buttonsAndActions = mutableMapOf<String, () -> Unit>()
                    viewModel.soundsList.forEach { (id, name, uri) ->
                        buttonsAndActions[name.toString()] = { viewModel.play(id) }
                    }
                    ButtonsScreen(buttonsAndActions)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainAppBar(
    currentScreen: MainScreen
) {
    TopAppBar(colors = TopAppBarDefaults.largeTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ), title = {
        Text(text = stringResource(currentScreen.title))
    })
}

@Preview
@Composable
fun PreviewAppBar() {
    MainAppBar(
        currentScreen = MainScreen.Main
    )
}
