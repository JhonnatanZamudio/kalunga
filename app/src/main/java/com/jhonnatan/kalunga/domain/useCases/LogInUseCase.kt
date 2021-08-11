package com.jhonnatan.kalunga.domain.useCases

import com.jhonnatan.kalunga.data.RequestUserLogin
import com.jhonnatan.kalunga.data.user.entities.RequestUsers
import com.jhonnatan.kalunga.data.user.entities.ResponseUsers
import com.jhonnatan.kalunga.data.user.entities.UserRemote
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
    lateinit var responseMessage: String
    lateinit var userRemote: UserRemote
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
            responseMessage = resultUser.first().message!!
            if (resultUser.first().status == "successful") {
                if (resultUser.first().message == "Cambiar password") {
                    return 1
                } else if (resultUser.first().message == "Datos correctos") {
                    userRemote = resultUser.first().data?.first()!!
                    return 2
                } else {
                    return 3
                }
            } else if (resultUser.first().status == "error") {
                return 4
            }
        }
        return 4
    }
}