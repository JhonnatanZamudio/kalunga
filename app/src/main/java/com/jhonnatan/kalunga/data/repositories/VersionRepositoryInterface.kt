package com.jhonnatan.kalunga.data.repositories

import com.jhonnatan.kalunga.data.repositories.source.local.entities.Version

/****
 * Project: kalunga
 * From: com.jhonnatan.kalunga.data.repositories
 * Created by Jhonnatan E. Zamudio P. on 28/06/2021 at 12:06 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 ****/

interface VersionRepositoryInterface {

    suspend fun queryLast(): List<Version>

    suspend fun insert(version: Version)

    suspend fun update(version: Version)

    suspend fun delete(version: Version)

    suspend fun clear()
}