package com.jhonnatan.kalunga.data.user.datasource

import com.jhonnatan.kalunga.data.RequestUserLogin
import com.jhonnatan.kalunga.data.user.entities.RequestUsers
import com.jhonnatan.kalunga.data.user.entities.RequestUsersUpdate
import com.jhonnatan.kalunga.data.user.entities.ResponseUsers
import com.jhonnatan.kalunga.data.user.source.UserApiClient
import com.jhonnatan.kalunga.data.retrofit.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/****
 * Project: kalunga
 * From: com.jhonnatan.kalunga.data.source.remote.services
 * Created by Jhonnatan E. Zamudio P. on 8/07/2021 at 10:45 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 ****/

class UserDataSourceRemote {

    private val retrofit = RetrofitHelper.getRetrofit()
    private val response = retrofit.create(UserApiClient::class.java)

    suspend fun getUsers(): List<ResponseUsers> {
        return withContext(Dispatchers.IO) {
            response.getAllUsers().body() ?: emptyList()
        }
    }

    suspend fun getUserByAccount(account: String): List<ResponseUsers> {
        return withContext(Dispatchers.IO) {
            response.getUserByAccount(account).body() ?: emptyList()
        }
    }

    suspend fun insertUser(requestUsers: RequestUsers): List<ResponseUsers> {
        return withContext(Dispatchers.IO) {
            response.insertUser(requestUsers).body() ?: emptyList()
        }
    }

    suspend fun updateUser(
        account: String,
        requestUsersUpdate: RequestUsersUpdate
    ): List<ResponseUsers> {
        return withContext(Dispatchers.IO) {
            response.updateUser(account, requestUsersUpdate).body() ?: emptyList()
        }
    }

    suspend fun deleteUser(account: String): List<ResponseUsers> {
        return withContext(Dispatchers.IO) {
            response.deleteUser(account).body() ?: emptyList()
        }
    }

    suspend fun loginUserRemote(requestUsersLogin: RequestUserLogin): List<ResponseUsers> {
        return withContext(Dispatchers.IO) {
            response.loginUserRemote(requestUsersLogin).body() ?: emptyList()
        }
    }
}