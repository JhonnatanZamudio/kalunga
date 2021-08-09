package com.jhonnatan.kalunga.domain.useCases

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jhonnatan.kalunga.data.cities.datasource.CitiesDataSourceLocal
import com.jhonnatan.kalunga.data.cities.entities.ResponseCities
import com.jhonnatan.kalunga.data.cities.repository.CitiesRepository
import com.jhonnatan.kalunga.data.cities.source.CitiesJSON
import com.jhonnatan.kalunga.data.room.KalungaDB
import com.jhonnatan.kalunga.data.typeDocument.datasource.TypeDocumentDataSourceLocal
import com.jhonnatan.kalunga.data.typeDocument.repository.TypeDocumentRepository
import com.jhonnatan.kalunga.data.typeDocument.source.TypeDocumentJSON
import com.jhonnatan.kalunga.data.user.datasource.UserDataSourceLocal
import com.jhonnatan.kalunga.data.user.datasource.UserDataSourceRemote
import com.jhonnatan.kalunga.data.user.repository.UserRepository
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.jhonnatan.kalunga.domain.models.enumeration.CodeStatusUser
import com.jhonnatan.kalunga.domain.models.utils.UtilsCountry
import com.jhonnatan.kalunga.domain.useCases.utils.Countries
import com.jhonnatan.kalunga.domain.useCases.utils.DataPools
import com.jhonnatan.kalunga.domain.useCases.utils.TypesDocument
import com.jhonnatan.kalunga.domain.useCases.utils.Users

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Laura S. Sarmiento M. on 23/07/2021 at 6:06 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class ConfigurationUseCaseTest {
    private lateinit var userDataSourceRemote: UserDataSourceRemote
    private lateinit var userDataSourceLocal: UserDataSourceLocal
    private lateinit var citiesDataSourceLocal: CitiesDataSourceLocal
    private lateinit var typeDocumentDataSourceLocal: TypeDocumentDataSourceLocal
    private lateinit var userRepository: UserRepository
    private lateinit var citiesRepository: CitiesRepository
    private lateinit var typeDocumentRepository: TypeDocumentRepository
    private lateinit var configurationUseCase: ConfigurationUseCase
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private var context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var database: KalungaDB
    private lateinit var citiesJSON: CitiesJSON
    private lateinit var typeDocumentJSON: TypeDocumentJSON
    private val faker = Faker()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            KalungaDB::class.java
        ).allowMainThreadQueries().build()
        userDataSourceRemote = UserDataSourceRemote()
        userDataSourceLocal = UserDataSourceLocal.getInstance(database.userDAO())
        userRepository = UserRepository.getInstance(userDataSourceRemote, userDataSourceLocal)
        citiesJSON = CitiesJSON(context)
        citiesDataSourceLocal = CitiesDataSourceLocal(citiesJSON)
        citiesRepository = CitiesRepository.getInstance(citiesDataSourceLocal)
        typeDocumentJSON = TypeDocumentJSON(context)
        typeDocumentDataSourceLocal = TypeDocumentDataSourceLocal(typeDocumentJSON)
        typeDocumentRepository = TypeDocumentRepository.getInstance(typeDocumentDataSourceLocal)
        configurationUseCase =
            ConfigurationUseCase(userRepository, citiesRepository, typeDocumentRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
        database.clearAllTables()
        database.close()
    }

    @Test
    fun `Caso 01`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val result = configurationUseCase.getDataCountries()
            Assert.assertEquals(Countries().getList(), result)
        }
    }

    @Test
    fun `Caso 02`() {
        val result = configurationUseCase.getCountryPosition("", Countries().getList())
        Assert.assertEquals(0, result)
        val result1 =
            configurationUseCase.getCountryPosition(faker.name.name(), Countries().getList())
        Assert.assertEquals(0, result1)
    }

    @Test
    fun `Caso 03`() {
        val result = configurationUseCase.getCountryPosition("Colombia", Countries().getList())
        Assert.assertEquals(5, result)
    }

    @Test
    fun `Caso 04`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val result = configurationUseCase.getDataTypeDocument()
            Assert.assertEquals(TypesDocument().getList(), result)
        }
    }

    @Test
    fun `Caso 05`() {
        val result = configurationUseCase.getTypeDocumentPosition("", TypesDocument().getList())
        Assert.assertEquals(0, result)
        val result1 =
            configurationUseCase.getTypeDocumentPosition(
                faker.name.name(),
                TypesDocument().getList()
            )
        Assert.assertEquals(0, result1)
        val result2 = configurationUseCase.getTypeDocumentPosition("CC", TypesDocument().getList())
        Assert.assertEquals(0, result2)
    }


    @Test
    fun `Caso 06`() {
        val result =
            configurationUseCase.getFormatPhone("313", UtilsCountry().getWhiteSpaceList("Colombia"),UtilsCountry().getMaxLength("Colombia"))
        Assert.assertEquals("313", result)
    }


    @Test
    fun `Caso 07`() {
        val result = configurationUseCase.getFormatPhone(
            "3133",
            UtilsCountry().getWhiteSpaceList("Colombia"),
            UtilsCountry().getMaxLength("Colombia")
        )
        Assert.assertEquals("313 3", result)
    }

    @Test
    fun `Caso 08`() {
        val result = configurationUseCase.areFieldsEmpty("")
        Assert.assertEquals(true, result)
    }

    @Test
    fun `Caso 09`() {
        val result = configurationUseCase.areFieldsEmpty(faker.animal.name())
        Assert.assertEquals(false, result)
    }

    @Test
    fun `Caso 10`() {
        val result = configurationUseCase.isCityInList(
            faker.animal.name(),
            ArrayList(listOf(ResponseCities(listOf("Bogotá", "Medellin", "Cali"), "Colombia")))
        )
        Assert.assertEquals(false, result)
    }

    @Test
    fun `Caso 11`() {
        val result = configurationUseCase.isCityInList(
            "Bogotá",
            ArrayList(listOf(ResponseCities(listOf("Bogotá", "Medellin", "Cali"), "Colombia")))
        )
        Assert.assertEquals(true, result)
    }

    @Test
    fun `Caso 12`() {
        val data =
            DataPools().createData("configuration/datapool_1", 1, 3) as Array<MutableList<Int>>
        for (id in 0 until data[0].size) {
            val result =
                configurationUseCase.changeEnableButton(data[0][id], data[1][id], data[2][id])
            Assert.assertEquals(false, result)
        }
    }

    @Test
    fun `Caso 13`() {
        val data =
            DataPools().createData("configuration/datapool_2", 1, 3) as Array<MutableList<Int>>
        for (id in 0 until data[0].size) {
            val result =
                configurationUseCase.changeEnableButton(data[0][id], data[1][id], data[2][id])
            Assert.assertEquals(true, result)
        }
    }

    @Test
    fun `Caso 14`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val result = configurationUseCase.existsUser(faker.animal.name())
            Assert.assertEquals(0, result)
        }
    }

    @Test
    fun `Caso 15`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val result = configurationUseCase.existsUser("unitTesting@kalunga.com")
            Assert.assertEquals(1, result)
        }
    }

    @Test
    fun `Caso 16`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val userInfo = Users().createRequest(CodeStatusUser.ENABLED_USER.code)
            val result = configurationUseCase.createUser(userInfo)
            Assert.assertEquals(0, result)
            userDataSourceRemote.deleteUser(userInfo.account)
        }
    }

    @Test
    fun `Caso 17`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val userInfo = Users().createRequest(CodeStatusUser.UNVALIDATED_USER.code)
            val result = configurationUseCase.createUser(userInfo)
            Assert.assertEquals(3, result)
            userDataSourceRemote.deleteUser(userInfo.account)
        }
    }

    @Test
    fun `Caso 18`() {
        val result = configurationUseCase.getFormatPhone(
            "313 33581422",
            UtilsCountry().getWhiteSpaceList("Colombia"),
            UtilsCountry().getMaxLength("Colombia")
        )
        Assert.assertEquals("313 3358142", result)
    }

    @Test
    fun `Caso 19`() {
        val result = configurationUseCase.isValidPhone(
            "313 33581422",
            UtilsCountry().getMaxLength("Colombia")
        )
        Assert.assertEquals(false, result)
    }
}