package com.jhonnatan.kalunga.domain.useCases

import android.Manifest
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.jhonnatan.kalunga.BuildConfig
import com.jhonnatan.kalunga.R
import com.jhonnatan.kalunga.data.room.KalungaDB
import com.jhonnatan.kalunga.data.user.datasource.UserDataSourceLocal
import com.jhonnatan.kalunga.data.user.datasource.UserDataSourceRemote
import com.jhonnatan.kalunga.data.user.entities.User
import com.jhonnatan.kalunga.data.user.repository.UserRepository
import com.jhonnatan.kalunga.data.version.datasource.VersionDataSourceLocal
import com.jhonnatan.kalunga.data.version.repository.VersionRepository
import com.jhonnatan.kalunga.domain.models.enumeration.CodePermissions
import com.jhonnatan.kalunga.domain.useCases.utils.Users
import com.jhonnatan.kalunga.domain.useCases.utils.Versions
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
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
class SplashScreenUseCaseTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private var context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var database: KalungaDB
    private lateinit var versionDataSourceLocal: VersionDataSourceLocal
    private lateinit var userDataSourceLocal: UserDataSourceLocal
    private lateinit var userDataSourceRemote: UserDataSourceRemote
    private lateinit var versionRepository: VersionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var splashScreenUseCase: SplashScreenUseCase
    private val permissionWriteStorge = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val permissionCamera = Manifest.permission.CAMERA
    private val permissionInternet = Manifest.permission.INTERNET
    private val fakeAppUpdateManager by lazy { Mockito.spy(FakeAppUpdateManager(context)) }
    private var users: MutableList<User> = ArrayList()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            KalungaDB::class.java
        ).allowMainThreadQueries().build()
        versionDataSourceLocal = VersionDataSourceLocal.getInstance(database.versionDAO())
        versionRepository = VersionRepository.getInstance(versionDataSourceLocal)
        userDataSourceRemote = UserDataSourceRemote()
        userDataSourceLocal = UserDataSourceLocal.getInstance(database.userDAO())
        userRepository = UserRepository.getInstance(userDataSourceRemote,userDataSourceLocal)
        splashScreenUseCase = SplashScreenUseCase(versionRepository, userRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
        database.close()
    }

    @Test
    fun `Caso 01`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val result = splashScreenUseCase.getAppVersion()
            assertEquals("Versión " + BuildConfig.VERSION_NAME, result)
        }
    }

    @Test
    fun `Caso 02`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            Versions().create(4,versionRepository)
            val result = splashScreenUseCase.getAppVersion()
            assertEquals("Versión " + BuildConfig.VERSION_NAME, result)
        }
    }

    @Test
    fun `Caso 03`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            Versions().create(1,versionRepository)
            val result = splashScreenUseCase.getAppVersion()
            assertEquals("Versión " + BuildConfig.VERSION_NAME, result)
        }
    }

    @Test
    fun `Caso 04`() {
        val result = splashScreenUseCase.getCodePermission(permissionWriteStorge)
        assertEquals(CodePermissions.WRITE_STORAGE.code,result)
    }

    @Test
    fun `Caso 05`() {
        val result = splashScreenUseCase.getCodePermission(permissionCamera)
        assertEquals(CodePermissions.CAMERA.code,result)
    }

    @Test
    fun `Caso 06`() {
        val result = splashScreenUseCase.getMessagePermission(permissionWriteStorge, context)
        assertEquals(context.getString(R.string.rationale_write_storage),result)
    }

    @Test
    fun `Caso 07`() {
        val result = splashScreenUseCase.getMessagePermission(permissionCamera, context)
        assertEquals(context.getString(R.string.rationale_camera),result)
    }

    @Test
    fun `Caso 08`() {
        val result = splashScreenUseCase.getMessagePermission(permissionInternet, context)
        assertEquals(context.getString(R.string.rationale_default),result)
    }

    @Test
    fun `Caso 09`() {
        fakeAppUpdateManager.setUpdateAvailable(UpdateAvailability.UPDATE_AVAILABLE, AppUpdateType.IMMEDIATE)
        val appUpdateInfoTask = fakeAppUpdateManager!!.appUpdateInfo
        val result = splashScreenUseCase.shouldBeUpdated(appUpdateInfoTask.result)
        assertEquals(true,result)
    }

    @Test
    fun `Caso 10`() {
        fakeAppUpdateManager.downloadStarts()
        fakeAppUpdateManager.setUpdateAvailable(UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)
        val appUpdateInfoTask = fakeAppUpdateManager!!.appUpdateInfo
        val result = splashScreenUseCase.shouldBeUpdated(appUpdateInfoTask.result)
        assertEquals(true,result)
    }

    @Test
    fun `Caso 11`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val result = splashScreenUseCase.getUserExist()
            assertEquals(false, result)
        }
    }

    @Test
    fun `Caso 12`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            users = Users().create(1,userRepository)
            val result = splashScreenUseCase.getUserExist()
            assertEquals(true, result)
        }
    }

}