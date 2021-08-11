package com.jhonnatan.kalunga.domain.useCases

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Laura S. Sarmiento M. on 11/08/2021 at 10:56 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class LogInUseCase {

    fun changeEnableButton(
        email: Int, password: Int
    ): Boolean {
        return email == 1 && password == 1
    }
}