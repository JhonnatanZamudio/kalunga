package com.jhonnatan.kalunga.presentation.core.session.viewModels

import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jhonnatan.kalunga.data.cities.entities.ResponseCities
import com.jhonnatan.kalunga.data.cities.entities.ResponseCountries
import com.jhonnatan.kalunga.data.cities.repository.CitiesRepository
import com.jhonnatan.kalunga.data.typeDocument.entities.ResponseDocumentType
import com.jhonnatan.kalunga.data.typeDocument.repository.TypeDocumentRepository
import com.jhonnatan.kalunga.data.user.repository.UserRepository
import com.jhonnatan.kalunga.domain.injectionOfDependencies.Injection
import com.jhonnatan.kalunga.domain.models.enumeration.CodeCountries
import com.jhonnatan.kalunga.domain.models.enumeration.CodeField
import com.jhonnatan.kalunga.domain.models.enumeration.CodeTypeDocument
import com.jhonnatan.kalunga.domain.models.enumeration.EnumerationWhiteSpaces
import com.jhonnatan.kalunga.domain.useCases.ConfigurationUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.presentation.core.session.viewModels
 * Created by Laura S. Sarmiento M. on 22/07/2021 at 6:10 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class ConfigurationViewModel(
    userRepository: UserRepository,
    citiesRepository: CitiesRepository,
    typeDocumentRepository: TypeDocumentRepository
) : ViewModel() {

    lateinit var countriesList: List<ResponseCountries>
    lateinit var typeDocumentsList: List<ResponseDocumentType>
    val countrySelectedPosition = MutableLiveData<Int>()
    val typeDocumentSelectedPosition = MutableLiveData<Int>()
    val numberFormat = MutableLiveData<String>()
    val citiesList: MutableLiveData<ArrayList<ResponseCities>> = MutableLiveData<ArrayList<ResponseCities>>()
    private val configurationUseCase =
        ConfigurationUseCase(userRepository, citiesRepository, typeDocumentRepository)

    init {
        getCountriesSpinner()
        countrySelectedPosition.value = configurationUseCase.getCountryPosition(CodeCountries.COLOMBIA.value,countriesList)
        getDocumentTypeSpinner()
        typeDocumentSelectedPosition.value = configurationUseCase.getTypeDocumentPosition(CodeTypeDocument.CEDULA_DE_CIUDADANIA.value,typeDocumentsList)
    }

    private fun getCountriesSpinner() {
        viewModelScope.launch {
            countriesList = configurationUseCase.getDataCountries()
        }
    }

    private fun getDocumentTypeSpinner() {
        viewModelScope.launch {
            typeDocumentsList = configurationUseCase.getDataTypeDocument()
        }
    }

    fun getDataCitiesByCodeCountry() {
        viewModelScope.launch {
            citiesList.value = ArrayList(configurationUseCase.getDataCitiesByCodeCountry(countriesList[countrySelectedPosition.value!!].pais))
        }
    }

    fun formatPhone(text: String, type: Char) {
        var whiteSpacesList: List<Int> = (listOf())
        if (text.isNotEmpty()) {
            if (!text.last().isWhitespace()) {
                when (countriesList[countrySelectedPosition.value!!].pais) {
                    CodeCountries.COLOMBIA.value -> whiteSpacesList =
                        EnumerationWhiteSpaces.COLOMBIA.code
                    CodeCountries.VENEZUELA.value -> whiteSpacesList =
                        EnumerationWhiteSpaces.VENEZUELA.code
                    CodeCountries.ITALIA.value -> whiteSpacesList =
                        EnumerationWhiteSpaces.ITALIA.code
                    CodeCountries.ESPANA.value -> whiteSpacesList =
                        EnumerationWhiteSpaces.ESPANA.code
                    CodeCountries.ESTADOS_UNIDOS.value -> whiteSpacesList =
                        EnumerationWhiteSpaces.ESTADOS_UNIDOS.code
                    CodeCountries.CHILE.value -> whiteSpacesList = EnumerationWhiteSpaces.CHILE.code
                    CodeCountries.ECUADOR.value -> whiteSpacesList =
                        EnumerationWhiteSpaces.ECUADOR.code
                    CodeCountries.PERU.value -> whiteSpacesList = EnumerationWhiteSpaces.PERU.code
                }
                if (whiteSpacesList.isNotEmpty()) {
                    var temporalNumber: String = text.replace(" ","")
                    for (id in whiteSpacesList) {
                        if (type=='0') {
                            if (id == text.length) {
                                numberFormat.value = "${text} "
                            }
                        } else {
                            for (letter in text.indices) {
                                if (letter == id){
                                    temporalNumber = temporalNumber.substring(0,letter)+" "+temporalNumber.substring(letter,temporalNumber.length)
                                }
                            }
                            numberFormat.value=temporalNumber
                        }
                    }
                }
            }
        }
    }

    fun encryptInfo(password_user: String, document_number: String, phone_number: String) {
        val plaintext: ByteArray =
            byteArrayOf(password_user.toByte(), document_number.toByte(), phone_number.toByte())
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(16)
        val key: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(
            Cipher.ENCRYPT_MODE, key,
            OAEPParameterSpec(
                "SHA-256",
                "MGF1",
                MGF1ParameterSpec.SHA256,
                PSource.PSpecified.DEFAULT
            )
        )
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val iv: ByteArray = cipher.iv
    }
}

@DelicateCoroutinesApi
@Suppress("UNCHECKED_CAST")
class ConfigurationViewModelFactory(
    private val userRepository: UserRepository,
    private val citiesRepository: CitiesRepository,
    private val typeDocumentRepository: TypeDocumentRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ConfigurationViewModelFactory? = null
        fun getInstance(context: Context): ConfigurationViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ConfigurationViewModelFactory(
                    Injection.providerConfigurationUserRepository(context),
                    Injection.providerConfigurationCitiesRepository(context),
                    Injection.providerConfigurationTypeDocumentRepository(context)
                )
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConfigurationViewModel(userRepository, citiesRepository, typeDocumentRepository) as T
    }
}