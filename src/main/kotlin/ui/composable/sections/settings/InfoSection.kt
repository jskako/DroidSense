package ui.composable.sections.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import ui.composable.elements.DividerColored
import ui.composable.elements.SelectableRow
import utils.ABOUT_LIBRARIES_JSON_NAME
import utils.DOCUMENTS_DIRECTORY
import utils.LICENSE_RESOURCE
import utils.getStringResource
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
            getTitle = { type ->
                when (type) {
                    InfoType.COPYRIGHT -> getStringResource("info.copyright")
                    InfoType.LICENSES -> getStringResource("info.licenses")
                    InfoType.ABOUT -> getStringResource("info.about")
                    InfoType.DONATION -> getStringResource("info.donation")
                }
            }
        )

        DividerColored()

        Spacer(Modifier.height(16.dp))

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
    useResource("$DOCUMENTS_DIRECTORY/$LICENSE_RESOURCE") { res ->
        readFile(res)?.let {
            BasicTextField(
                modifier = Modifier.padding(start = 16.dp),
                value = it,
                readOnly = true,
                onValueChange = { },
                textStyle = TextStyle(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        }
    }
}

@Composable
private fun AboutScreen() {

}

@Composable
private fun LicenseScreen() {
    LibrariesContainer(
        aboutLibsJson = useResource(ABOUT_LIBRARIES_JSON_NAME) {
            it.bufferedReader().readText()
        },
        modifier = Modifier
            .fillMaxSize()
    )
}

private enum class InfoType {
    COPYRIGHT, LICENSES, ABOUT, DONATION
}