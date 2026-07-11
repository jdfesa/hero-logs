package com.herologs

import android.app.Application
import com.herologs.di.AppContainer

class HeroLogsApplication : Application() {
    val appContainer: AppContainer by lazy {
        AppContainer(applicationContext)
    }
}
