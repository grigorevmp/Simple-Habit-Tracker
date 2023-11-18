package com.grigorevmp.habits.presentation.screen.settings.settings

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grigorevmp.habits.R
import com.grigorevmp.habits.presentation.screen.settings.elements.SettingsBaseCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ImpexCard(
    getPreparedHabitsList: ((String) -> Unit) -> Unit,
) {
    val context = LocalContext.current

    SettingsBaseCard(
        cardTitle = stringResource(R.string.impex_export_title),
        cardIconResource = R.drawable.ic_upload,
        cardIconDescription = stringResource(R.string.impex_export_icon_description),
        cardColor = CardDefaults.cardColors(),
        cardOnClick = { },
    ) {

        Column {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
                    exportDatabase(context, "Habit tracker", getPreparedHabitsList)
                }) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.impex_export_statistic)
                )
            }
        }
    }
}

fun exportDatabase(
    context: Context,
    exportDbName: String,
    getPreparedHabitsList: ((String) -> Unit) -> Unit,
) {
    try {
        getPreparedHabitsList { str ->
            CoroutineScope(Dispatchers.IO).launch {
                val exportDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + "/" + exportDbName
                )
                if (!exportDir.exists()) {
                    exportDir.mkdirs()
                }

                val exportFile = File(exportDir, "exported_data_${System.currentTimeMillis()}.txt")
                if (withContext(Dispatchers.IO) {
                        exportFile.createNewFile()
                    }) {
                    exportFile.outputStream().use { stream ->
                        stream.write(str.toByteArray())
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.impex_data_exported_to, exportFile),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.impex_failed_to_export_data),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


@Preview(showBackground = true)
@Composable
fun ImpexCardPreview() {
    ImpexCard(getPreparedHabitsList = { _ -> })
}