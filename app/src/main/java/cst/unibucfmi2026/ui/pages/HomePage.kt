package cst.unibucfmi2026.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.activity.ComponentActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cst.unibucfmi2026.R
import cst.unibucfmi2026.ui.theme.UnibucFMI2026Theme
import cst.unibucfmi2026.utils.AppLogo
import cst.unibucfmi2026.utils.Background
import cst.unibucfmi2026.viewmodel.ThemeViewModel

@Composable
fun HomePage(
    onLogout: () -> Unit = {},
    onTestFoodApi: () -> Unit = {},
    onMyFoods: () -> Unit = {},
    onDeveloperSettings: () -> Unit = {}
) {
    val activity = LocalContext.current as ComponentActivity
    val themeViewModel: ThemeViewModel = viewModel(viewModelStoreOwner = activity)
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Background(0.4f)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo(120)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onTestFoodApi, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.search_food))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onMyFoods, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.my_foods))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.logout))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isDarkTheme) "Dark mode" else "Light mode",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Switch(checked = isDarkTheme, onCheckedChange = { themeViewModel.toggle() })
            }
        }
        IconButton(
            onClick = onDeveloperSettings,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.developer_settings),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    UnibucFMI2026Theme {
        HomePage()
    }
}
