package ui.composable.sections.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.info_about
import com.jskako.droidsense.generated.resources.info_copyright
import com.jskako.droidsense.generated.resources.info_donation
import com.jskako.droidsense.generated.resources.info_licenses
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import ui.composable.elements.DividerColored
import ui.composable.elements.SelectableRow
import utils.ABOUT_LIBRARIES_JSON_NAME
import utils.DOCUMENTS_DIRECTORY
import utils.LICENSE_RESOURCE
import utils.readFile

@Composable
fun InfoSection() {

    var selectedInfoType by remember { mutableStateOf(InfoType.COPYRIGHT) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp)
    ) {
        SelectableRow(
            enumValues = InfoType.entries.toTypedArray(),
            selectedValue = selectedInfoType,
            onSelect = { selectedInfoType = it },
            onTitle = { type ->
                when (type) {
                    InfoType.COPYRIGHT -> Res.string.info_copyright
                    InfoType.LICENSES -> Res.string.info_licenses
                    InfoType.ABOUT -> Res.string.info_about
                    InfoType.DONATION -> Res.string.info_donation
                }
            }
        )

        DividerColored(
            paddingValues = PaddingValues(bottom = 16.dp),
        )

        when (selectedInfoType) {
            InfoType.COPYRIGHT -> CopyrightScreen()
            InfoType.LICENSES -> LicenseScreen()
            InfoType.ABOUT -> AboutScreen()
            InfoType.DONATION -> DonationScreen()
        }
    }
}

@Composable
private fun DonationScreen() {

}

@Composable
private fun CopyrightScreen() {

    val content by produceState(initialValue = "") {
        value = readFile(path = "$DOCUMENTS_DIRECTORY/$LICENSE_RESOURCE")
    }

    BasicTextField(
        modifier = Modifier.padding(start = 16.dp),
        value = content,
        readOnly = true,
        onValueChange = { },
        textStyle = TextStyle(
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    )
}

@Composable
private fun AboutScreen() {

}

@Composable
private fun LicenseScreen() {

    val aboutLibraryJson by produceState(initialValue = "") {
        value = readFile(path = "$DOCUMENTS_DIRECTORY/$ABOUT_LIBRARIES_JSON_NAME")
    }

    if (aboutLibraryJson.isNotBlank()) {
        LibrariesContainer(
            aboutLibsJson = aboutLibraryJson,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

private enum class InfoType {
    COPYRIGHT, LICENSES, ABOUT, DONATION
}