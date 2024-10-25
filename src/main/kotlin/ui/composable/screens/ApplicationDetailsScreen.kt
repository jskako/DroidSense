package ui.composable.screens

import adb.application.AppDetailType
import adb.application.AppDetailsData
import adb.application.ApplicationManager
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import ui.composable.elements.CircularProgressBar
import utils.getStringResource

@Composable
fun ApplicationDetailsScreen(
    applicationManager: ApplicationManager,
    packageId: String
) {
    var applicationDetails by remember { mutableStateOf<List<AppDetailsData>>(emptyList()) }

    LaunchedEffect(Unit) {
        val appDataDeferred = async {
            applicationManager.getAppDetails(packageName = packageId)
        }
        applicationDetails = appDataDeferred.await() ?: emptyList()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        ApplicationDetailsContent(
            applicationDetails = applicationDetails,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun ApplicationDetailsContent(
    applicationDetails: List<AppDetailsData>,
    paddingValues: PaddingValues
) {

    var selectedApplicationType by remember { mutableStateOf(AppDetailType.PACKAGE_INFO) }

    Column(modifier = Modifier.padding(paddingValues)) {
        CircularProgressBar(
            text = getStringResource("info.search.application.empty"),
            isVisible = applicationDetails.isEmpty()
        )

        if (applicationDetails.isNotEmpty()) {
            DetailsList(applicationDetails)
        }
    }
}

@Composable
private fun DetailsList(
    applicationDetails: List<AppDetailsData>
) {

    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            items(applicationDetails) { line ->
                SelectionContainer {
                    Column {
                        Text(
                            text = line.title,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = line.info,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(end = 5.dp)
                .width(15.dp),
            adapter = rememberScrollbarAdapter(scrollState = listState)
        )
    }
}
