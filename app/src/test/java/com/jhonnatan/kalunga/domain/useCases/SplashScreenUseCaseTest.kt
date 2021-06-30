package com.jhonnatan.kalunga.domain.useCases

import android.content.Context
import androidx.lifecycle.map
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jhonnatan.kalunga.BuildConfig
import com.jhonnatan.kalunga.data.source.local.dataBases.KalungaDB
import com.jhonnatan.kalunga.data.source.local.dataSources.SplashScreenDataSource
import com.jhonnatan.kalunga.data.source.local.entities.Version
import com.jhonnatan.kalunga.data.source.local.repositories.SplashScreenRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/****
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Jhonnatan E. Zamudio P. on 28/06/2021 at 9:40 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 */

@RunWith(AndroidJUnit4::class)
@Suppress("NonAsciiCharacters")
@ExperimentalCoroutinesApi
class SplashScreenUseCaseTest() {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var context: Context
    private lateinit var database: KalungaDB
    private lateinit var splashScreenDataSource: SplashScreenDataSource
    private lateinit var splashScreenRepository: SplashScreenRepository
    private lateinit var splashScreenUseCase: SplashScreenUseCase


    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            KalungaDB::class.java
        ).allowMainThreadQueries().build()
        splashScreenDataSource = SplashScreenDataSource.getInstance(database.splashScreenDAO())
        splashScreenRepository = SplashScreenRepository.getInstance(splashScreenDataSource)
        splashScreenUseCase  = SplashScreenUseCase(splashScreenRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
        database.close()
    }
    
    @Test
    fun `Caso 1`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val result = splashScreenUseCase.getAppVersion()
            assertEquals("Versión " + BuildConfig.VERSION_NAME,result)
        }
    }

    @Test
    fun `Caso 2`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            splashScreenRepository.insert(Version(0,1,"1.0.0",Calendar.getInstance().time))
            splashScreenRepository.insert(Version(0,2,"1.0.1",Calendar.getInstance().time))
            val result = splashScreenUseCase.getAppVersion()
            splashScreenRepository.clear()
            assertEquals("Versión 1.0.1" ,result)
        }
    }

    @Test
    fun `Caso 3`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            splashScreenRepository.insert(Version(0,1,"1.0.0",Calendar.getInstance().time))
            val result = splashScreenUseCase.getAppVersion()
            splashScreenRepository.clear()
            assertEquals("Versión " + BuildConfig.VERSION_NAME ,result)
        }
    }
}