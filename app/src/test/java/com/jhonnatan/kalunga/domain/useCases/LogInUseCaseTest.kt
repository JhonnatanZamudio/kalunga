package com.jhonnatan.kalunga.domain.useCases

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jhonnatan.kalunga.data.RequestUserLogin
import com.jhonnatan.kalunga.data.room.KalungaDB
import com.jhonnatan.kalunga.data.user.datasource.UserDataSourceLocal
import com.jhonnatan.kalunga.data.user.datasource.UserDataSourceRemote
import com.jhonnatan.kalunga.data.user.repository.UserRepository
import com.jhonnatan.kalunga.domain.models.utils.UtilsSecurity
import com.jhonnatan.kalunga.domain.useCases.utils.Users
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Laura S. Sarmiento M. on 11/08/2021 at 11:09 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class LogInUseCaseTest {
    private lateinit var logInUseCase: LogInUseCase
    private val faker = Faker()
    private var emails = mutableListOf<String>()

    private lateinit var userDataSourceRemote: UserDataSourceRemote
    private lateinit var userDataSourceLocal: UserDataSourceLocal
    private lateinit var userRepository: UserRepository
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private var context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var database: KalungaDB

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            KalungaDB::class.java
        ).allowMainThreadQueries().build()
        userDataSourceRemote = UserDataSourceRemote()
        userDataSourceLocal = UserDataSourceLocal.getInstance(database.userDAO())
        userRepository = UserRepository.getInstance(userDataSourceRemote, userDataSourceLocal)
        logInUseCase = LogInUseCase(userRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }


    @Test
    fun `Caso 01`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val users = Users().cloneServer()
            val userInfo = RequestUserLogin(
                users.email,
                "Jhotec2013",
            )
            val result = logInUseCase.loginUser(userInfo)
            Assert.assertEquals(2, result)
        }
    }


    @Test
    fun `Caso 02`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val userInfo = RequestUserLogin(
                faker.animal.name(),
                UtilsSecurity().cipherData(faker.animal.name()),
            )
            val result = logInUseCase.loginUser(userInfo)
            Assert.assertEquals(3, result)
        }
    }

}