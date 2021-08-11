package com.jhonnatan.kalunga.data.user.repository

import com.jhonnatan.kalunga.data.RequestUserLogin
import com.jhonnatan.kalunga.data.user.entities.RequestUsers
import com.jhonnatan.kalunga.data.user.entities.RequestUsersUpdate
import com.jhonnatan.kalunga.data.user.entities.ResponseUsers
import com.jhonnatan.kalunga.data.user.entities.User

/****
 * Project: kalunga
 * From: com.jhonnatan.kalunga.data.version.repositories.user
 * Created by Jhonnatan E. Zamudio P. on 8/07/2021 at 11:15 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 ****/

interface UserRepositoryInterface {

    suspend fun getUserByAccountRemote(account: String): List<ResponseUsers>

    suspend fun getUsersRemote(): List<ResponseUsers>

    suspend fun insertUserRemote(requestUsers: RequestUsers): List<ResponseUsers>

    suspend fun updateUserRemote(account: String, requestUsersUpdate: RequestUsersUpdate): List<ResponseUsers>

    suspend fun deleteUserRemote(account: String): List<ResponseUsers>

    suspend fun getAllUsersLocal(): List<User>

    suspend fun getUserByAccountLocal(account: String): List<User>

    suspend fun insertUserLocal(user: User)

    suspend fun updateUserLocal(user: User)

    suspend fun deleteUserLocal(user: User)

    suspend fun clearUsersLocal()

    suspend fun loginUserRemote(requestUserLogin: RequestUserLogin): List<ResponseUsers>

}