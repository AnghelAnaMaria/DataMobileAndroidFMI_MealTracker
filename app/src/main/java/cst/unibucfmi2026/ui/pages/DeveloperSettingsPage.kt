package cst.unibucfmi2026.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cst.unibucfmi2026.R
import cst.unibucfmi2026.utils.DeveloperSettingsManager

@Composable
fun DeveloperSettingsPage(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val devSettings = remember { DeveloperSettingsManager.getInstance(context) }
    var apiKey by remember { mutableStateOf(devSettings.fdcApiKey) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Manual Top Bar
            Surface(
                shadowElevation = 3.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.developer_settings),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.fdc_api_config),
                    style = MaterialTheme.typography.titleMedium
                )
                
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text(stringResource(R.string.fdc_api_key)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.enter_api_key)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            devSettings.fdcApiKey = apiKey
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.save))
                    }
                    
                    OutlinedButton(
                        onClick = {
                            devSettings.resetFdcApiKey()
                            apiKey = devSettings.fdcApiKey
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.reset_to_default))
                    }
                }
            }
        }
    }
}
