package cst.unibucfmi2026

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cst.unibucfmi2026.ui.navigation.AuthNavigation
import cst.unibucfmi2026.ui.theme.UnibucFMI2026Theme
import cst.unibucfmi2026.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeViewModel: ThemeViewModel by viewModels()
        setContent {
            val isDark by themeViewModel.isDarkTheme.collectAsState()
            UnibucFMI2026Theme(darkTheme = isDark, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavigation()
                }
            }
        }

        Log.e("TAG", "onCreate:")
    }

    override fun onStart(){
        super.onStart()
        Log.e("TAG", "onStart:")
    }

    override fun onResume(){
        super.onResume()
        Log.e("TAG", "onResume:")
    }

    override fun onPause(){
        super.onPause()
        Log.e("TAG", "onPause:")
    }

    override fun onStop(){
        super.onStop()
        Log.e("TAG", "onStop:")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.e("TAG", "onDestroy:")
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val context= LocalContext.current

    Button(
        onClick = {
            Log.e("TAG", "onClick:")

            val intent= Intent(context, MainActivity2::class.java)
            context.startActivity(intent)
            (context as? ComponentActivity)?.finish()
        },
        modifier= Modifier.fillMaxWidth()
    ) {
        Text(
        text = "Hello $name!",
        modifier = modifier
    )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnibucFMI2026Theme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun AuthNavPreview() {
    UnibucFMI2026Theme {
        AuthNavigation()
    }
}