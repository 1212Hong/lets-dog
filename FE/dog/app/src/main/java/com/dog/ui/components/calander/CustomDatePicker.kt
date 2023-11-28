package com.dog.ui.components.calander

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.dog.util.common.formatDate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CustomDatePicker(
    curDate: String,
    curOpen: Boolean
) {
    val date = remember { mutableStateOf(curDate) }
    val isOpen = remember { mutableStateOf(curOpen) }

    Row(verticalAlignment = Alignment.CenterVertically) {


        OutlinedTextField(
            readOnly = true,
            value = date.value.format(DateTimeFormatter.ISO_DATE),
            label = { Text("Date") },
            onValueChange = {}
        )

        IconButton(
            onClick = { isOpen.value = true } // show de dialog
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar")
        }
    }

    if (isOpen.value) {
        CustomDatePickerDialog(
            onAccept = {
                isOpen.value = false // close dialog

                if (it != null) { // Set the date
                    date.value = formatDate(
                        Instant
                            .ofEpochMilli(it)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate().toString(), "yyyy-MM-dd"
                    )
                }
            },
            onCancel = {
                isOpen.value = false //close dialog
            }
        )
    }

}
