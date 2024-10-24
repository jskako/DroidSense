package ui.composable.screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import log.ApplicationManager
import ui.composable.elements.CircularProgressBar
import utils.Colors.darkBlue
import utils.getStringResource


@Composable
fun ApplicationDetailsScreen(
    applicationManager: ApplicationManager,
    packageId: String
) {

    val listState = rememberLazyListState()
    var applicationDetails by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        val userAppsData = async {
            applicationManager.getAppDetails(
                packageName = packageId
            )
        }

        applicationDetails = userAppsData.await() ?: emptyList()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {

                },
                containerColor = darkBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.Filled.Add, getStringResource("info.install.app")) },
                text = { Text(text = getStringResource("info.install.app")) },
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            CircularProgressBar(
                text = getStringResource("info.search.application.empty"),
                isVisible = applicationDetails.isEmpty()
            )

            if (applicationDetails.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 80.dp
                        )
                    ) {
                        items(applicationDetails) { line ->
                            SelectionContainer {
                                Text(text = line)
                            }
                        }
                    }
                    VerticalScrollbar(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .padding(end = 5.dp)
                            .width(15.dp),
                        adapter = rememberScrollbarAdapter(
                            scrollState = listState
                        )
                    )
                }
            }
        }
    }
}