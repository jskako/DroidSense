package ui.composable.elements.apps

import adb.application.AppData
import adb.application.ApplicationManager
import adb.application.ApplicationType
import adb.getAvailableSpaces
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_app_system
import com.jskako.droidsense.generated.resources.info_app_user
import com.jskako.droidsense.generated.resources.info_install_app
import com.jskako.droidsense.generated.resources.info_install_application
import com.jskako.droidsense.generated.resources.info_search
import com.jskako.droidsense.generated.resources.info_search_application_empty
import com.jskako.droidsense.generated.resources.info_select_space
import com.jskako.droidsense.generated.resources.string_placeholder
import data.ArgsText
import kotlinx.coroutines.launch
import notifications.InfoManager
import notifications.InfoManagerData
import org.jetbrains.compose.resources.stringResource
import ui.application.WindowStateManager
import ui.composable.elements.CircularProgressBar
import ui.composable.elements.DividerColored
import ui.composable.elements.ListWithScrollbar
import ui.composable.elements.SelectableRow
import ui.composable.elements.SelectionDialog
import ui.composable.sections.info.InfoSection
import utils.Colors.darkBlue
import utils.Colors.darkRed
import utils.Colors.transparentTextFieldDefault
import utils.EMPTY_STRING
import utils.getSpaceId


@Composable
fun AppsView(
    windowStateManager: WindowStateManager,
    applicationManager: ApplicationManager,
    serialNumber: String,
    deviceModel: String,
    adbPath: String,
    apps: List<AppData>,
    identifier: String,
    onAppDeleted: (AppData) -> Unit,
    onAppInstalled: () -> Unit,
) {

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
            title = Res.string.info_select_space,
            options = getAvailableSpaces(adbPath, identifier).mapIndexed { index, item ->
                item.takeIf { index != 0 } ?: "$item (Default)"
            },
            onOptionSelected = { userInfo ->
                buttonsEnabled = false
                showMessage(
                    infoManagerData = InfoManagerData(
                        message = ArgsText(
                            textResId = Res.string.info_install_application
                        ),
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
                                onAppInstalled()
                            },
                            onFailure = { error ->
                                showMessage(
                                    infoManagerData = InfoManagerData(
                                        message = ArgsText(
                                            textResId = Res.string.string_placeholder,
                                            formatArgs = listOf(error.message ?: EMPTY_STRING)
                                        ),
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
                    icon = { Icon(Icons.Filled.Add, stringResource(Res.string.info_install_app)) },
                    text = { Text(text = stringResource(Res.string.info_install_app)) },
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
                SelectableRow(
                    enumValues = ApplicationType.entries.toTypedArray(),
                    selectedValue = selectedApplicationType,
                    onSelect = { selectedApplicationType = it },
                    onTitle = { type ->
                        when (type) {
                            ApplicationType.USER -> Res.string.info_app_user
                            ApplicationType.SYSTEM -> Res.string.info_app_system
                        }
                    }
                )

                TextField(
                    value = searchText,
                    colors = transparentTextFieldDefault,
                    singleLine = true,
                    onValueChange = {
                        searchText = it
                    },
                    placeholder = { Text(stringResource(Res.string.info_search)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            DividerColored()

            CircularProgressBar(
                text = ArgsText(textResId = Res.string.info_search_application_empty),
                isVisible = filteredApps.isEmpty()
            )

            if (filteredApps.isNotEmpty()) {
                ListWithScrollbar(
                    lazyModifier = Modifier.padding(top = 8.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 80.dp
                    ),
                    content = {
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
                )
            }
        }
    }
}