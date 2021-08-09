package com.jhonnatan.kalunga.domain.useCases

import com.jhonnatan.kalunga.data.cities.entities.ResponseCities
import com.jhonnatan.kalunga.data.cities.entities.ResponseCountries
import com.jhonnatan.kalunga.data.cities.repository.CitiesRepository
import com.jhonnatan.kalunga.data.typeDocument.entities.ResponseDocumentType
import com.jhonnatan.kalunga.data.typeDocument.repository.TypeDocumentRepository
import com.jhonnatan.kalunga.data.user.entities.RequestUsers
import com.jhonnatan.kalunga.data.user.entities.ResponseUsers
import com.jhonnatan.kalunga.data.user.entities.User
import com.jhonnatan.kalunga.data.user.repository.UserRepository
import com.jhonnatan.kalunga.domain.models.enumeration.CodeStatusUser

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.useCases
 * Created by Laura S. Sarmiento M. on 23/07/2021 at 11:46 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class ConfigurationUseCase(
    private val userRepository: UserRepository,
    private val citiesRepository: CitiesRepository,
    private val typeDocumentRepository: TypeDocumentRepository
) {

    suspend fun getDataCountries(): List<ResponseCountries> {
        return citiesRepository.getDataCountries().sortedBy { myObject -> myObject.codPais }
    }

    fun getCountryPosition(country: String, countriesList: List<ResponseCountries>): Int {
        var aux = 0
        for (x in countriesList.indices)
            if (countriesList[x].pais == country)
                aux = x
        return aux
    }

    suspend fun getDataTypeDocument(): List<ResponseDocumentType> {
        return typeDocumentRepository.getDataTypeDocument().sortedBy { myObject -> myObject.abbreviate }
    }

    fun getTypeDocumentPosition(typeDocument: String, typeDocumentsList: List<ResponseDocumentType>): Int {
        var aux = 0
        for (x in typeDocumentsList.indices)
            if (typeDocumentsList[x].abbreviate == typeDocument)
                aux = x
        return aux
    }

    suspend fun getDataCitiesByCodeCountry(country: String): List<ResponseCities> {
        return citiesRepository.getDataCitiesByCodeCountry(country).sortedBy { myObject -> myObject.pais }
    }

    fun areFieldsEmpty(text: String): Boolean {
        return text.isEmpty()
    }

    fun getFormatPhone(text: String, whiteSpacesList: List<Int>, textMaxLenght:Int) : String{
        for (id in whiteSpacesList) {
            if (id == text.length) {
               return "${text.substring(0,text.length-1)} ${text.last()}"
            }
        }
        return if (text.length <= textMaxLenght){
            text
        } else {
            text.substring(0,textMaxLenght)
        }
    }

    fun isCityInList(text: String, citiesList: ArrayList<ResponseCities>): Boolean{
        var cityFounded = false
        for(id in citiesList[0].data.indices){
            if (citiesList[0].data[id] == text){
                cityFounded = true
                break
            }
        }
        return cityFounded
    }

    fun changeEnableButton(
        document: Int, phone: Int, city: Int
    ): Boolean {
        return document == 1 && phone == 1 && city == 1
    }

    suspend fun existsUser(user: String): Int{
        val userExists = userRepository.getUserByAccountRemote(user)
        if (userExists.first().message == "No existe el usuario en la base de datos"){
            return 0
        } else if (userExists.first().status == "successful"){
            return 1
        } else if (userExists.first().status == "error"){
            return 2
        }
        return 2
    }


    suspend fun createUser(user: RequestUsers): Int{
        val resultUser = userRepository.insertUserRemote(user)
        if (resultUser.first().message!! == "El usuario fue creado exitosamente"){
            if (user.statusUser== CodeStatusUser.ENABLED_USER.code){
                return 0
            } else if (user.statusUser== CodeStatusUser.UNVALIDATED_USER.code){
                return 3
            }
        } else if (resultUser.first().message!! == "Email erroneo...") {
            return 4
        }
        return 5
    }

    suspend fun insertUserLocal(user: User){
        val insertUser = userRepository.insertUserLocal(user)
        return insertUser
    }
}