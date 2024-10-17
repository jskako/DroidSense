package ui.composable.elements.apps

import adb.ApplicationType
import androidx.compose.foundation.VerticalScrollbar
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import log.AppData
import notifications.InfoManagerData
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.DividerColored
import ui.composable.elements.HintBox
import ui.composable.elements.iconButtons.IconButton
import utils.EMPTY_STRING
import utils.getStringResource


@Composable
fun AppsView(
    apps: List<AppData>,
    onMessage: (InfoManagerData) -> Unit
) {

    val listState = rememberLazyListState()
    var selectedApplicationType by remember { mutableStateOf(ApplicationType.USER) }
    var searchText by remember { mutableStateOf(EMPTY_STRING) }

    val filteredApps = apps.filter { app ->
        val matchesApplicationType = app.applicationType == selectedApplicationType
        val matchesSearchText = searchText.isEmpty() || app.packageId.contains(searchText, ignoreCase = true)
        matchesApplicationType && matchesSearchText
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getStringResource("info.app.user"),
                textAlign = TextAlign.Start,
                fontWeight = if (selectedApplicationType == ApplicationType.USER) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        selectedApplicationType = ApplicationType.USER
                    },
            )

            Text(
                text = getStringResource("info.app.system"),
                textAlign = TextAlign.Start,
                fontWeight = if (selectedApplicationType == ApplicationType.SYSTEM) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        selectedApplicationType = ApplicationType.SYSTEM
                    },
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                HintBox(
                    text = searchText,
                    onValueChanged = {
                        searchText = it
                    }
                )
                IconButton(
                    text = getStringResource("info.install.app"),
                    icon = Icons.Default.Add,
                    onClick = {}
                )
            }
        }

        DividerColored()

        CircularProgressBar(
            text = getStringResource("info.getting.application"),
            isVisible = filteredApps.isEmpty()
        )

        if (filteredApps.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    items(filteredApps) { app ->
                        AppsCard(
                            app = app,
                            onMessage = onMessage
                        )
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