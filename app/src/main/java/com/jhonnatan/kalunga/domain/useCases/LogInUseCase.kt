package com.jhonnatan.kalunga.domain.useCases

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Laura S. Sarmiento M. on 11/08/2021 at 10:56 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class LogInUseCase {

    fun isNumberPair(number: Int): Boolean {
        return number % 2 == 0
    }

    fun areFieldsEmpty(text: String): Boolean {
        return text.isEmpty()
    }

    fun isValidLong(text: String, min: Int): Boolean {
        return text.length > min
    }
}