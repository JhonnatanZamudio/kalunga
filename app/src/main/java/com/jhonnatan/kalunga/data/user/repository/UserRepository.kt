package com.jhonnatan.kalunga.data.user.repository

import com.jhonnatan.kalunga.data.RequestUserLogin
import com.jhonnatan.kalunga.data.user.datasource.UserDataSourceLocal
import com.jhonnatan.kalunga.data.user.entities.RequestUsers
import com.jhonnatan.kalunga.data.user.entities.RequestUsersUpdate
import com.jhonnatan.kalunga.data.user.entities.ResponseUsers
import com.jhonnatan.kalunga.data.user.datasource.UserDataSourceRemote
import com.jhonnatan.kalunga.data.user.entities.User

/****
 * Project: kalunga
 * From: com.jhonnatan.kalunga.data.version.repositories
 * Created by Jhonnatan E. Zamudio P. on 8/07/2021 at 11:04 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 ****/

class UserRepository(
    private val userDataSourceRemote: UserDataSourceRemote,
    private val userDataSourceLocal: UserDataSourceLocal
) : UserRepositoryInterface {

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userDataSourceRemote: UserDataSourceRemote,
            userDataSourceLocal: UserDataSourceLocal
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userDataSourceRemote,userDataSourceLocal)
            }
    }

    override suspend fun getUsersRemote(): List<ResponseUsers> {
        return userDataSourceRemote.getUsers()
    }

    override suspend fun getUserByAccountRemote(account: String): List<ResponseUsers> {
        return userDataSourceRemote.getUserByAccount(account)
    }

    override suspend fun insertUserRemote(requestUsers: RequestUsers): List<ResponseUsers> {
        return userDataSourceRemote.insertUser(requestUsers)
    }

    override suspend fun updateUserRemote(
        account: String,
        requestUsersUpdate: RequestUsersUpdate
    ): List<ResponseUsers> {
        return userDataSourceRemote.updateUser(account, requestUsersUpdate)
    }

    override suspend fun deleteUserRemote(account: String): List<ResponseUsers> {
        return userDataSourceRemote.deleteUser(account)
    }

    override suspend fun getAllUsersLocal(): List<User> {
        return userDataSourceLocal.getAllUsers()
    }

    override suspend fun getUserByAccountLocal(account: String): List<User> {
        return userDataSourceLocal.getUserByAccount(account)
    }

    override suspend fun insertUserLocal(user: User) {
        userDataSourceLocal.insertUser(user)
    }

    override suspend fun updateUserLocal(user: User) {
        userDataSourceLocal.updateUser(user)
    }

    override suspend fun deleteUserLocal(user: User) {
        userDataSourceLocal.deleteUser(user)
    }

    override suspend fun clearUsersLocal() {
        userDataSourceLocal.clearUsers()
    }

    suspend fun loginUserRemote(requestUserLogin: RequestUserLogin): List<ResponseUsers> {
        return userDataSourceRemote.loginUserRemote(requestUserLogin)
    }

}