package com.jhonnatan.kalunga.domain.useCases

import io.github.serpro69.kfaker.Faker
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Laura S. Sarmiento M. on 11/08/2021 at 11:09 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
@Suppress("UNCHECKED_CAST")
class LogInUseCaseTest {
    private lateinit var logInUseCase: LogInUseCase
    private val faker = Faker()
    private var emails = mutableListOf<String>()

    @Before
    fun setup() {
        logInUseCase = LogInUseCase()
    }


}