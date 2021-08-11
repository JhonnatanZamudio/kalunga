package com.jhonnatan.kalunga.domain.useCases

import com.jhonnatan.kalunga.domain.models.utils.UtilsFields
import com.jhonnatan.kalunga.domain.useCases.utils.DataPools
import io.github.serpro69.kfaker.Faker
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Laura S. Sarmiento M. on 16/07/2021 at 6:28 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 */

@Suppress("UNCHECKED_CAST")
class SignUpUseCaseTest {
    private lateinit var signUpUseCase: SignUpUseCase
    private val faker = Faker()
    private var emails = mutableListOf<String>()

    @Before
    fun setup() {
        signUpUseCase = SignUpUseCase()
    }
/*

    @Test
    fun `Caso 01`() {
        val result = UtilsFields().areFieldsEmpty("")
        assertEquals(true, result)
    }

    @Test
    fun `Caso 02`() {
        val result = UtilsFields().areFieldsEmpty(faker.animal.name())
        assertEquals(false, result)
    }

    @Test
    fun `Caso 03`() {
        emails = DataPools().createData("signUp/datapool_1", 0, 1) as MutableList<String>
        for (id in 0 until emails.size) {
            val result = UtilsFields().isValidEmail(emails[id])
            assertEquals(false, result)
        }
    }

    @Test
    fun `Caso 04`() {
        emails = DataPools().createData("signUp/datapool_2", 0, 1) as MutableList<String>
        for (id in 0 until emails.size) {
            val result = UtilsFields().isValidEmail(emails[id])
            assertEquals(true, result)
        }
    }

    @Test
    fun `Caso 05`() {
        val result = UtilsFields().isValidLong(faker.animal.name().first().toString(), 2)
        assertEquals(false, result)
    }

    @Test
    fun `Caso 06`() {
        val result = UtilsFields().isValidLong(faker.animal.name(), 2)
        assertEquals(true, result)
    }

    @Test
    fun `Caso 07`() {
        val result = UtilsFields().isValidLong(faker.animal.name().first().toString(), 5)
        assertEquals(false, result)
    }

    @Test
    fun `Caso 08`() {
        val result = UtilsFields().isValidLong(
            faker.animal.name() + faker.animal.name() + faker.animal.name(),
            5
        )
        assertEquals(true, result)
    }

*/

    @Test
    fun `Caso 09`() {
        val result = signUpUseCase.arePasswordsEqual(faker.animal.name(), faker.address.city())
        assertEquals(false, result)
    }

    @Test
    fun `Caso 10`() {
        val animal = faker.animal.name()
        val result = signUpUseCase.arePasswordsEqual(animal, animal)
        assertEquals(true, result)
    }

    @Test
    fun `Caso 11`() {
        val result = signUpUseCase.isNumberPair(1)
        assertEquals(false, result)
    }

    @Test
    fun `Caso 12`() {
        val result = signUpUseCase.isNumberPair(2)
        assertEquals(true, result)
    }

    @Test
    fun `Caso 13`() {
        val data = DataPools().createData("signUp/datapool_3", 1,4) as Array<MutableList<Int>>
        for (id in 0 until data[0].size) {
            val result = signUpUseCase.changeEnableButton(data[0][id], data[1][id], data[2][id], data[3][id])
            assertEquals(false, result)
        }
    }

    @Test
    fun `Caso 14`() {
        val data = DataPools().createData("signUp/datapool_4", 1, 4) as Array<MutableList<Int>>
        for (id in 0 until data[0].size) {
            val result = signUpUseCase.changeEnableButton(data[0][id], data[1][id], data[2][id], data[3][id])
            assertEquals(true, result)
        }
    }

}