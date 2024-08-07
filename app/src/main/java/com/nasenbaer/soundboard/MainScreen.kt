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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.nasenbaer.soundboard.ui.DeleteDialog
import kotlinx.coroutines.selects.select

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
    val snackbarHostState = remember { SnackbarHostState() }

    StartScreenScaffold(navController, currentScreen, snackbarHostState, viewModel)
}

@Composable
fun StartScreenScaffold(
    navController: NavHostController,
    currentScreen: MainScreen,
    snackbarHostState: SnackbarHostState,
    viewModel: MainViewModel
) {
    viewModel.showAddSoundDialog = remember { mutableStateOf(false) }
    viewModel.soundsList = remember { mutableStateListOf() }

    val showConfirmDeleteDialog = remember { mutableStateOf(false) }

    if (viewModel.showAddSoundDialog.value) {
        AddSoundDialog(viewModel, snackbarHostState)
    }
    if (showConfirmDeleteDialog.value) {
        DeleteDialog(onConfirmation = { viewModel.deleteSelected(); showConfirmDeleteDialog.value = false }, onDismiss = {showConfirmDeleteDialog.value = false}, soundName = viewModel.selectedSound.name.toString())
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        MainAppBar(currentScreen)
    }, floatingActionButton = {
        FloatingActionButton(onClick = { viewModel.showAddSoundDialog.value = true }) {
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

                    viewModel.soundsList.forEach { element ->
                        element.play = {
                            viewModel.selectedSound = element
                            viewModel.playSelected()
                        }
                        element.delete = {
                            viewModel.selectedSound = element
                            showConfirmDeleteDialog.value = true
                        }
                    }
                    ButtonsScreen(viewModel.soundsList)
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
