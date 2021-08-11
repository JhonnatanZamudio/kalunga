package com.jhonnatan.kalunga.domain.useCases

import com.jhonnatan.kalunga.data.RequestUserLogin
import com.jhonnatan.kalunga.data.user.entities.RequestUsers
import com.jhonnatan.kalunga.data.user.entities.ResponseUsers
import com.jhonnatan.kalunga.data.user.repository.UserRepository
import com.jhonnatan.kalunga.domain.models.enumeration.CodeStatusUser

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Laura S. Sarmiento M. on 11/08/2021 at 10:56 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class LogInUseCase(
    private val userRepository: UserRepository) {

    fun changeEnableButton(
        email: Int, password: Int
    ): Boolean {
        return email == 1 && password == 1
    }

    suspend fun loginUser(user: RequestUserLogin): Int{
        val resultUser = userRepository.loginUserRemote(user)
        println("resultUser")
        println(resultUser)
        if (!resultUser.isNullOrEmpty()){
            if (resultUser.first().message == "El correo electrónico no se encuentra asociado a una cuenta de Kalunga") {
                return 0
            } else if (resultUser.first().status == "successful") {
                return 1
            } else if (resultUser.first().status == "error") {
                return 2
            }
        }
        return 2
    }
}