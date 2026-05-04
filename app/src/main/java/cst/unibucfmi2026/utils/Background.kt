package cst.unibucfmi2026.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import cst.unibucfmi2026.R

@Composable
fun Background(alpha: Float= 0.2f){
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
    )
}