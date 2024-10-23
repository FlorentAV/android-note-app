package com.florent.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.florent.noteapp.ui.theme.NoteAppTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.add
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteApp()
        }
    }
}




@Composable
fun NoteApp() {
    val navController = rememberNavController()

    // Set up a NavHost with two screens: Home and AddNote
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }  // Home screen
        composable("addNote") { AddNoteScreen(navController) } // Add note screen
        composable("showNote") { ShowNoteScreen(navController) }// Show chosen note
    }
}



@Composable
fun HomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // FloatingActionButton at the bottom right

        FAB(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { navController.navigate("addNote") },
            icon = Icons.Filled.Add)


    }
}

@Composable
fun ShowNoteScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    )

}


@Composable
fun AddNoteScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        // FloatingActionButton at the bottom right
        FAB(
            modifier = Modifier.align(Alignment.BottomStart),
            onClick = { navController.navigate("home") },
            icon = Icons.Filled.ArrowBackIosNew)
    }

}



@Composable
fun FAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector, ) {

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(icon, contentDescription = "Floating action button")

    }

}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    NoteAppTheme {
        NoteApp()
    }
}
