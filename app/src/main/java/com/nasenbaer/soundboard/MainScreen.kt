package com.nasenbaer.soundboard

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nasenbaer.soundboard.ui.AddSoundDialog
import com.nasenbaer.soundboard.ui.ButtonsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class MainScreen(@StringRes val title: Int) {
    Main(title = R.string.app_name)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundBoardApp(
    viewModel: MainViewModel, navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MainScreen.valueOf(
        backStackEntry?.destination?.route ?: MainScreen.Main.name
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    AppDrawer(
        drawerState, currentScreen, coroutineScope, viewModel
    ) {
        StartScreenScaffold(navController, currentScreen, coroutineScope, drawerState, viewModel)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun StartScreenScaffold(
    navController: NavHostController,
    currentScreen: MainScreen,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: MainViewModel
) {
    val showDialog = remember { mutableStateOf(false) }
    if(showDialog.value){
        AddSoundDialog(viewModel = viewModel, save = { showDialog.value = false }, abort = {showDialog.value = false})
    }

    Scaffold(topBar = {
        AppBar(currentScreen, coroutineScope, drawerState)
    }, floatingActionButton = {
        FloatingActionButton(onClick = { showDialog.value = true }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainScreen.Main.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = MainScreen.Main.name) {
                ButtonsScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppBar(
    currentScreen: MainScreen, coroutineScope: CoroutineScope, drawerState: DrawerState
) {
    TopAppBar(colors = TopAppBarDefaults.largeTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    ), title = {
        Text(text = stringResource(currentScreen.title))
    }, navigationIcon = {
        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
            Icon(Icons.Default.Menu, "Menu Icon")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewAppBar() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    AppBar(
        currentScreen = MainScreen.Main, coroutineScope = coroutineScope, drawerState = drawerState
    )
}
