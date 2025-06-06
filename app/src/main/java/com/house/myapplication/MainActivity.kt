package com.house.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.house.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ListScreen()
                }
            }
        }
    }
}

/**
 * Displays the list; passes along 4 callbacks for each row:
 *   • onHeartClick
 *   • onDeleteClick
 *   • onMoveUpClick
 *   • onMoveDownClick
 */
@Composable
fun ListScreen(viewModel: MyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(uiState.items, key = { it.id }) { item ->
            ListItemRow(
                item = item,
                onHeartClick = { viewModel.toggleHeart(item.id) },
                onDeleteClick = { viewModel.deleteItem(item.id) },
                onMoveUpClick = { viewModel.moveItemUp(item.id) },
                onMoveDownClick = { viewModel.moveItemDown(item.id) }
            )
        }
    }
}

/**
 * One row with:
 *   • Title (weight = 1f)
 *   • Heart icon (toggles isHearted)
 *   • ↑ / ↓ icons (reorder)
 *   • Delete icon (removes item)
 *
 * Each Icon uses Modifier.clickable { … } directly.
 */
@Composable
fun ListItemRow(
    item: ListItem,
    onHeartClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoveUpClick: () -> Unit,
    onMoveDownClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1) Title
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.size(8.dp))

        // 2) Heart (Box + clickable)
        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable { onHeartClick() },
            contentAlignment = Alignment.Center
        ) {
            if (item.isHearted) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Unheart",
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Heart",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        // 3) Move Up
        Icon(
            imageVector = Icons.Filled.ThumbUp,
            contentDescription = "Move Up",
            modifier = Modifier
                .size(24.dp)
                .clickable { onMoveUpClick() }
        )

        Spacer(modifier = Modifier.size(8.dp))

        // 4) Move Down
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "Move Down",
            modifier = Modifier
                .size(24.dp)
                .clickable { onMoveDownClick() }
        )

        Spacer(modifier = Modifier.size(8.dp))

        // 5) Delete
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete",
            modifier = Modifier
                .size(24.dp)
                .clickable { onDeleteClick() }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
