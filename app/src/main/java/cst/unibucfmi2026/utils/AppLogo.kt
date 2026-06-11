package cst.unibucfmi2026.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cst.unibucfmi2026.R

@Composable
fun AppLogo(size: Int=60){
    Image(
        painter = painterResource(id = R.drawable.plate),
        contentDescription = "Logo",
        modifier = Modifier.size(size.dp)
    )
}