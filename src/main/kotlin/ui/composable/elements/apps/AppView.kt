package ui.composable.elements.apps

import adb.ApplicationType
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import log.AppData
import log.ApplicationManager
import log.getAvailableSpaces
import notifications.InfoManager
import notifications.InfoManagerData
import ui.application.WindowStateManager
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.DividerColored
import ui.composable.elements.SelectionDialog
import ui.composable.sections.info.InfoSection
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.EMPTY_STRING
import utils.getSpaceId
import utils.getStringResource


@Composable
fun AppsView(
    windowStateManager: WindowStateManager,
    applicationManager: ApplicationManager,
    serialNumber: String,
    deviceModel: String,
    adbPath: String,
    apps: List<AppData>,
    identifier: String,
    onAppDeleted: (AppData) -> Unit
) {

    val listState = rememberLazyListState()
    var selectedApplicationType by remember { mutableStateOf(ApplicationType.USER) }
    var searchText by remember { mutableStateOf(EMPTY_STRING) }
    var showDialog by remember { mutableStateOf(false) }
    var buttonsEnabled by remember { mutableStateOf(true) }
    val infoManager = remember { InfoManager() }
    val scope = rememberCoroutineScope()

    fun showMessage(infoManagerData: InfoManagerData) {
        infoManager.showMessage(
            infoManagerData = infoManagerData,
            scope = scope
        )
    }

    if (showDialog) {
        SelectionDialog(
            title = getStringResource("info.select.space"),
            options = getAvailableSpaces(adbPath, identifier).mapIndexed { index, item ->
                item.takeIf { index != 0 } ?: "$item (Default)"
            },
            onOptionSelected = { userInfo ->
                buttonsEnabled = false
                showMessage(
                    infoManagerData = InfoManagerData(
                        message = getStringResource("info.install.application"),
                        duration = null
                    )
                )

                scope.launch {
                    getSpaceId(userInfo)?.let {
                        applicationManager.installApplication(
                            spaceId = it.toString()
                        ).fold(
                            onSuccess = { infoData ->
                                showMessage(infoManagerData = infoData)
                            },
                            onFailure = {
                                showMessage(
                                    infoManagerData = InfoManagerData(
                                        message = it.message ?: EMPTY_STRING,
                                        color = darkRed
                                    )
                                )
                            }
                        )
                    }
                    buttonsEnabled = true
                }
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    val filteredApps = apps.filter { app ->
        val matchesApplicationType = app.applicationType == selectedApplicationType
        val matchesSearchText = searchText.isEmpty() || app.packageId.contains(searchText, ignoreCase = true)
        matchesApplicationType && matchesSearchText
    }

    Scaffold(
        floatingActionButton = {
            if (buttonsEnabled) {
                ExtendedFloatingActionButton(
                    onClick = {
                        showDialog = true
                    },
                    containerColor = darkBlue,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Filled.Add, getStringResource("info.install.app")) },
                    text = { Text(text = getStringResource("info.install.app")) },
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            InfoSection(
                onCloseClicked = {
                    infoManager.clearInfoMessage()
                },
                message = infoManager.infoManagerData.value.message,
                color = infoManager.infoManagerData.value.color,
                buttonVisible = infoManager.infoManagerData.value.buttonVisible
            )

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

                TextField(
                    value = searchText,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    onValueChange = {
                        searchText = it
                    },
                    placeholder = { Text(getStringResource("info.search")) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            DividerColored()

            CircularProgressBar(
                text = getStringResource("info.search.application.empty"),
                isVisible = filteredApps.isEmpty()
            )

            if (filteredApps.isNotEmpty()) {
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
                        ),
                        modifier = Modifier.padding(top = 8.dp),
                    ) {
                        items(filteredApps) { app ->
                            AppCard(
                                windowStateManager = windowStateManager,
                                applicationManager = applicationManager,
                                serialNumber = serialNumber,
                                deviceModel = deviceModel,
                                app = app,
                                onMessage = {
                                    showMessage(it)
                                },
                                buttonsEnabled = buttonsEnabled,
                                onButtonEnabled = { buttonsEnabled = it },
                                onAppDeleted = onAppDeleted
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
}