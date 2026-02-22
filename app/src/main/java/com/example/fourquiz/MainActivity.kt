// --- GEMINI HEADER ---
package com.example.fourquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fourquiz.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent is the bridge between the Android OS and Jetpack Compose
        setContent {
            // We call your root navigation graph here. 
            // This replaces the entire screen with your custom UI!
            AppNavigation()
        }
    }
}
// --- GEMINI FOOTER ---