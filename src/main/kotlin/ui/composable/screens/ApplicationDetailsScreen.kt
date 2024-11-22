package ui.composable.screens

import adb.application.AppDetailType
import adb.application.AppDetailsData
import adb.application.ApplicationManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import kotlinx.coroutines.async
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.DividerColored
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.SelectableRow
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

    val filteredAppDetails = applicationDetails.filter { app ->
        app.type == selectedApplicationType
    }

    Column(modifier = Modifier.padding(paddingValues)) {

        SelectableRow(
            enumValues = AppDetailType.entries.toTypedArray(),
            selectedValue = selectedApplicationType,
            onSelect = { selectedApplicationType = it },
            getTitle = { type ->
                when (type) {
                    AppDetailType.PACKAGE_INFO -> AppDetailType.PACKAGE_INFO.title
                    AppDetailType.MEMORY_INFO -> AppDetailType.MEMORY_INFO.title
                    AppDetailType.BATTERY_USAGE -> AppDetailType.BATTERY_USAGE.title
                    AppDetailType.NETWORK_STATS -> AppDetailType.NETWORK_STATS.title
                }
            }
        )

        DividerColored()

        CircularProgressBar(
            text = getStringResource("info.search.application.empty"),
            isVisible = filteredAppDetails.isEmpty()
        )

        if (applicationDetails.isNotEmpty()) {
            DetailsList(filteredAppDetails)
        }
    }
}

@Composable
private fun DetailsList(
    applicationDetails: List<AppDetailsData>
) {
    ListWithScrollbar(
        content = {
            items(applicationDetails) { appDetailsData ->
                SelectionContainer {
                    Column {
                        Text(
                            text = appDetailsData.info,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    )
}
