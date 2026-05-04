package cst.unibucfmi2026.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cst.unibucfmi2026.R
import cst.unibucfmi2026.ui.theme.UnibucFMI2026Theme
import cst.unibucfmi2026.utils.AppLogo
import cst.unibucfmi2026.utils.Background

@Composable
fun HomePage(onLogout: () -> Unit = {}) {
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
            Button(onClick = onLogout) {
                Text(stringResource(R.string.logout))
            }

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
