package com.florent.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

// Data class representing a note
data class Note(
    val id: Int,
    var title: String,
    var content: String
)


@Composable
fun NoteApp() {
    val navController = rememberNavController()
    val notes = remember { mutableStateListOf<Note>() } // List to store notes

    // Set up a NavHost to navigate between screens
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, notes) }  // Home screen
        composable("addNote") { AddNoteScreen(navController, notes) } // Add note screen

        // Show note Screen
        composable("showNote/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toInt()
            val note = notes.find { it.id == noteId }
            if (note != null) {
                ShowNoteScreen(navController, note)
            }

        }
        // Edit note Screen
        composable("editNote/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toInt()
            val note = notes.find { it.id == noteId }
            if (note != null) {
                EditNoteScreen(navController, note)
            }
        }
    }
}



@Composable
fun HomeScreen(navController: NavHostController, notes: List<Note>) {

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(top = 64.dp)) {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            // Navigate to the note detail screen with the selected note
                            navController.navigate("showNote/${note.id}") // Shows clicked noted based on note ID
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = note.content,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

            }


        }


        // Add note button at the bottom right
        FAB(
            modifier = Modifier
                .padding(16.dp) // General padding
                .offset(x = -12.dp, y = -45.dp) // Offset to move the button up and to the right
                .align(Alignment.BottomEnd),
            onClick = { navController.navigate("addNote") },
            icon = Icons.Filled.Add
        )
    }
}

@Composable
fun EditNoteScreen(navController: NavHostController, note: Note) {
    var noteTitle by remember { mutableStateOf(note.title) } // State for note title
    var noteContent by remember { mutableStateOf(note.content) } // State for note content
    var errorMessage by remember { mutableStateOf("") } // Error message state

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize() .padding(top = 24.dp)) {
            // Text field for adding title
            TextField(
                value = noteTitle,
                onValueChange = { noteTitle = it },
                label = { Text("Title") },
                isError = (noteTitle.length < 5 || noteTitle.length > 50 ) && errorMessage.isNotEmpty(), // To make the field red if there is an error
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )



            // Text field for adding content
            TextField(
                value = noteContent,
                onValueChange = { noteContent = it },
                label = { Text("Add Note") },
                isError = (noteContent.isEmpty() || noteContent.length > 120 ) && errorMessage.isNotEmpty(),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            // Shows error message when errorMessage contains an error.
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )

            // Save button
            Button(
                onClick = {
                    if (noteTitle.length < 3) {
                        errorMessage = "Title must be at least 3 characters"
                    }
                    else if (noteTitle.length > 50) {
                        errorMessage = "Title must be not contain more than 50 characters"
                    }
                    else if (noteContent.length > 120) {
                        errorMessage = "Note must be not contain more than 120 characters"
                    }
                    else {
                        // Update the note content
                        note.content = noteContent
                        note.title = noteTitle
                        navController.navigate("home")
                    }
                },
                modifier = Modifier
                    .align(Alignment.End)

                    .padding(16.dp)
            ) {
                Text("Save")
            }



        }
        // Back button at the bottom left
        FAB(
            modifier = Modifier
                .padding(16.dp) // General padding
                .offset(x = 12.dp, y = -45.dp) // Offset to move the button up and to the right
                .align(Alignment.BottomStart),
            onClick = { navController.navigate("home") },
            icon = Icons.Filled.ArrowBackIosNew
        )
    }
}



@Composable
fun ShowNoteScreen(navController: NavHostController, note: Note) {
    var noteTitle by remember { mutableStateOf(note.title) } // State for note title
    var noteContent by remember { mutableStateOf(note.content) } // State for note content


    Box(modifier = Modifier.fillMaxSize()) {
        // Use a Column to display the note details
        Card(
            modifier = Modifier
                .padding(top = 64.dp, start = 16.dp, end = 16.dp) // Padding around the Card
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium // Rounded corners
        ) {
        Column(
            modifier = Modifier
                .padding(top = 12.dp) // Padding from the top of the screen
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Display the note title
            Text(
                text = noteTitle,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp) // Space below title
            )
            // Display the note content
            Text(
                text = noteContent,
                modifier = Modifier.padding(top = 4.dp)
            )
            Button(

                onClick = {
                    // Update the note content
                    note.content = noteContent
                    note.title = noteTitle
                    navController.navigate("editNote/${note.id}") // Navigates to edit screen based on the note ID

                },
                modifier = Modifier
                    .align(Alignment.End)

                    .padding(16.dp)
            ) {
                Text("Edit")
            }
        }
            }


        // Back button at the bottom left
        FAB(
            modifier = Modifier
                .padding(16.dp) // General padding
                .offset(x = 12.dp, y = -45.dp) // Offset to move the button up and to the right
                .align(Alignment.BottomStart),
            onClick = { navController.navigate("home") },
            icon = Icons.Filled.ArrowBackIosNew
        )
    }
}






@Composable
fun AddNoteScreen(navController: NavHostController, notes: MutableList<Note>) {
    var noteContent by remember { mutableStateOf("") } // State for note content
    var noteTitle by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize() .padding(top = 24.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Text field for inputting note title
            TextField(
                value = noteTitle,
                onValueChange = { noteTitle = it },
                label = { Text("Enter Title") },
                isError = noteTitle.length < 3 && errorMessage.isNotEmpty(),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )



            // Text field for inputting note content
            TextField(
                value = noteContent,
                onValueChange = { noteContent = it },
                label = { Text("Enter Note") },
                isError = noteContent.length < 5 && errorMessage.isNotEmpty(),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()

            )

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )


            // Save button
            Button(
                onClick = {
                    if (noteTitle.length < 3) {
                        errorMessage = "Title must be at least 3 characters"
                    } else if (noteContent.length < 5) {
                        errorMessage = "Note must be at least 5 characters"
                    } else {
                        // Save the note and navigate back to home

                        notes.add(Note(notes.size + 1, noteTitle, noteContent)) // Add new note
                        navController.navigate("home")
                    }

                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Text("Save")
            }



        }
        // Back button at the bottom left
        FAB(
            modifier = Modifier
                .padding(16.dp) // General padding
                .offset(x = 12.dp, y = -45.dp) // Offset to move the button up and to the right
                .align(Alignment.BottomStart),
            onClick = { navController.navigate("home") },
            icon = Icons.Filled.ArrowBackIosNew
        )
    }
}


// Floating action button
@Composable
fun FAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
) {

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
