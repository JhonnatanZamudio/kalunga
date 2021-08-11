package com.jhonnatan.kalunga.domain.useCases

import com.jhonnatan.kalunga.domain.models.enumeration.CodePatterns

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Laura S. Sarmiento M. on 16/07/2021 at 6:21 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class SignUpUseCase {

    fun arePasswordsEqual(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun changeEnableButton(
        email: Int, name: Int, password: Int, confirmPassword: Int,
    ): Boolean {
        return email == 1 && name == 1 && password == 1 && confirmPassword == 1
    }

    fun isNumberPair(number: Int): Boolean {
        return number % 2 == 0
    }

}