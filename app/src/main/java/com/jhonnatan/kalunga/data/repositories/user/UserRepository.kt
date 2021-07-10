package com.jhonnatan.kalunga.data.repositories.user

import com.jhonnatan.kalunga.data.source.remote.entities.requests.RequestUsers
import com.jhonnatan.kalunga.data.source.remote.entities.requests.RequestUsersUpdate
import com.jhonnatan.kalunga.data.source.remote.entities.responses.ResponseUsers
import com.jhonnatan.kalunga.data.source.remote.services.UserService

/****
 * Project: kalunga
 * From: com.jhonnatan.kalunga.data.repositories
 * Created by Jhonnatan E. Zamudio P. on 8/07/2021 at 11:04 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 ****/

class UserRepository(private val userService: UserService) : UserRepositoryInterface {

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(userService: UserService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userService)
            }
    }

    override suspend fun getUsersRemote(): List<ResponseUsers> {
        return userService.getUsers()
    }

    override suspend fun getUserByAccountRemote(account: String): List<ResponseUsers> {
        return userService.getUserByAccount(account)
    }

    override suspend fun insertUser(requestUsers: RequestUsers): List<ResponseUsers> {
        return userService.insertUser(requestUsers)
    }

    override suspend fun updateUser(account: String, requestUsersUpdate: RequestUsersUpdate): List<ResponseUsers> {
        return userService.updateUser(account, requestUsersUpdate)
    }

    override suspend fun deleteUser(account: String): List<ResponseUsers> {
        return userService.deleteUser(account)
    }

}