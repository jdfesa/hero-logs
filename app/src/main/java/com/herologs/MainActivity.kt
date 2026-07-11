package com.herologs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.herologs.designsystem.HeroLogsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeroLogsTheme {
                HeroLogsApp(
                    appContainer = (application as HeroLogsApplication).appContainer,
                )
            }
        }
    }
}
