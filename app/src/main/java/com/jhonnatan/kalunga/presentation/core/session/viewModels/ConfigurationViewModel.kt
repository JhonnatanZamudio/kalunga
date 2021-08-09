package com.jhonnatan.kalunga.presentation.core.session.viewModels

import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jhonnatan.kalunga.R
import com.jhonnatan.kalunga.data.cities.entities.ResponseCities
import com.jhonnatan.kalunga.data.cities.entities.ResponseCountries
import com.jhonnatan.kalunga.data.cities.repository.CitiesRepository
import com.jhonnatan.kalunga.data.typeDocument.entities.ResponseDocumentType
import com.jhonnatan.kalunga.data.typeDocument.repository.TypeDocumentRepository
import com.jhonnatan.kalunga.data.user.entities.RequestUsers
import com.jhonnatan.kalunga.data.user.entities.User
import com.jhonnatan.kalunga.data.user.repository.UserRepository
import com.jhonnatan.kalunga.domain.injectionOfDependencies.Injection
import com.jhonnatan.kalunga.domain.models.entities.UserAccountData
import com.jhonnatan.kalunga.domain.models.enumeration.*
import com.jhonnatan.kalunga.domain.models.utils.UtilsCountry
import com.jhonnatan.kalunga.domain.models.utils.UtilsNetwork
import com.jhonnatan.kalunga.domain.models.utils.UtilsSecurity
import com.jhonnatan.kalunga.domain.useCases.ConfigurationUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

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
    val snackBarAction = MutableLiveData<Int>()
    val typeDocumentSelectedPosition = MutableLiveData<Int>()
    val numberPhone = MutableLiveData<String>()
    val numberPhoneStart = MutableLiveData<Boolean>()
    val citiesList: MutableLiveData<ArrayList<ResponseCities>> =
        MutableLiveData<ArrayList<ResponseCities>>()
    private val configurationUseCase =
        ConfigurationUseCase(userRepository, citiesRepository, typeDocumentRepository)
    private var validIdentification = MutableLiveData<Int>()
    private var validPhone = MutableLiveData<Int>()
    private var validCity = MutableLiveData<Int>()
    var userAccount = MutableLiveData<UserAccountData>()
    var emailValue = MutableLiveData<String>()
    var nameValue = MutableLiveData<String>()
    var passwordValue = MutableLiveData<String>()
    var statusUser = MutableLiveData<Int>()
    var sessionState = MutableLiveData<Boolean>()
    var typeUser = MutableLiveData<Int>()
    val errorIdentification = MutableLiveData<String>()
    val errorCity = MutableLiveData<String>()
    val errorPhone = MutableLiveData<String>()
    val buttonRegisterDrawable = MutableLiveData<Int>()
    val buttonRegisterEnable = MutableLiveData<Boolean>()
    val snackBarTextWarning = MutableLiveData<String>()
    val snackBarTextError = MutableLiveData<String>()
    val snackBarTextInfo = MutableLiveData<String>()
    val snackBarTextSuccess = MutableLiveData<String>()
    val snackBarNavigate = MutableLiveData<Int>()
    val navigateToStarting = MutableLiveData<Boolean>()
    val navigateToLogIn = MutableLiveData<Boolean>()
    val navigateToDashboard = MutableLiveData<Boolean>()

    init {
        getCountriesSpinner()
        countrySelectedPosition.value =
            configurationUseCase.getCountryPosition(CodeCountries.COLOMBIA.value, countriesList)
        getDocumentTypeSpinner()
        typeDocumentSelectedPosition.value = configurationUseCase.getTypeDocumentPosition(
            CodeTypeDocument.CEDULA_DE_CIUDADANIA.value,
            typeDocumentsList
        )
        validCity.value = 0
        validIdentification.value = 0
        validPhone.value = 0
        snackBarNavigate.value = CodeSnackBarCloseAction.NONE.code
        numberPhoneStart.value=false
    }

    fun setInitialValues() {
        userAccount.value = UserAccountData(
            emailValue.value!!,
            nameValue.value!!,
            emailValue.value!!,
            passwordValue.value!!,
            passwordValue.value!!,
            "",
            "",
            ""
        )
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
            citiesList.value =
                ArrayList(configurationUseCase.getDataCitiesByCodeCountry(countriesList[countrySelectedPosition.value!!].pais))
        }
    }

    fun formatPhone(text: String) {
        val whiteSpacesList: List<Int>
        if (text.isNotEmpty()) {
            if (!text.last().isWhitespace()) {
                whiteSpacesList =
                    UtilsCountry().getWhiteSpaceList(countriesList[countrySelectedPosition.value!!].pais)
                val textMaxLenght =
                    UtilsCountry().getMaxLength(countriesList[countrySelectedPosition.value!!].pais)
                if (whiteSpacesList.isNotEmpty()) {
                    val formatPhone = configurationUseCase.getFormatPhone(text, whiteSpacesList, textMaxLenght)
                    if (formatPhone !== text) {
                        numberPhone.value = formatPhone
                    }
                }
            }
        }
    }

    fun areFieldsEmpty(text: Editable?, field: Int) {

        if (configurationUseCase.areFieldsEmpty(text.toString())) {
            setErrorText(field, ResponseErrorField.ERROR_EMPTY.value)
            when (field) {
                CodeField.IDENTIFICATION_FIELD.code -> validIdentification.value = 0
                CodeField.PHONE_FIELD.code -> validPhone.value = 0
                CodeField.CITY_FIELD.code -> validCity.value = 0
            }
            changeEnableButton()
        } else {
            setErrorText(field, ResponseErrorField.DEFAULT.value)
            when (field) {
                CodeField.IDENTIFICATION_FIELD.code -> {
                    userAccount.value!!.identification = text.toString()
                    validIdentification.value = 1
                    changeEnableButton()
                }
                CodeField.PHONE_FIELD.code -> {
                    userAccount.value!!.phone = text.toString()
                    validPhone.value = 1
                    formatPhone(text.toString())
                    changeEnableButton()
                }
                CodeField.CITY_FIELD.code -> {
                    isValidCity(text.toString())
                }
            }
        }
    }

    private fun setErrorText(field: Int, value: String) {
        when (field) {
            CodeField.IDENTIFICATION_FIELD.code -> errorIdentification.value = value
            CodeField.PHONE_FIELD.code -> errorPhone.value = value
            CodeField.CITY_FIELD.code -> errorCity.value = value
        }
    }

    private fun isValidCity(city: String) {
        if (configurationUseCase.isCityInList(city, citiesList.value!!)) {
            validCity.value = 1
            userAccount.value!!.city = city
            setErrorText(CodeField.CITY_FIELD.code, ResponseErrorField.DEFAULT.value)
        } else {
            validCity.value = 0
            setErrorText(CodeField.CITY_FIELD.code, ResponseErrorField.ERROR_INVALID_CITY.value)
        }
        changeEnableButton()
    }

    private fun changeEnableButton() {
        if (configurationUseCase.changeEnableButton(
                validIdentification.value!!,
                validPhone.value!!,
                validCity.value!!
            )
        ) {
            buttonRegisterDrawable.value = R.drawable.boton_oscuro
            buttonRegisterEnable.value = true
        } else {
            buttonRegisterDrawable.value = R.drawable.boton_oscuro_disabled
            buttonRegisterEnable.value = false
        }
    }

    fun searchUser() {
        viewModelScope.launch {
            processUser()
        }
    }

    fun navigateToLogIn() {
        navigateToLogIn.value = true
    }

    fun navigateToStarting() {
        navigateToStarting.value = true
    }

    suspend fun processUser() {
        val existsUser = configurationUseCase.existsUser(userAccount.value!!.id)
        when (existsUser) {
            0 -> createUser()
            1 -> {
                if (statusUser.value==CodeStatusUser.ENABLED_USER.code){
                    snackBarAction.value=1
                } else if (statusUser.value==CodeStatusUser.UNVALIDATED_USER.code){
                    snackBarAction.value=2
                }
            }
            2 -> snackBarAction.value=5
        }
    }

    suspend fun createUser(){
        val userInfo = RequestUsers(
            userAccount.value!!.email,
            UtilsSecurity().cipherData(userAccount.value!!.password)!!,
            statusUser.value!!,
            sessionState.value!!,
            typeUser.value!!,
            userAccount.value!!.email,
            userAccount.value!!.name,
            typeDocumentsList[typeDocumentSelectedPosition.value!!].valor,
            UtilsSecurity().cipherData(userAccount.value!!.identification)!!,
            UtilsSecurity().cipherData(userAccount.value!!.phone.replace(" ",""))!!,
            countriesList[countrySelectedPosition.value!!].pais,
            userAccount.value!!.city
        )
        val resultUser = configurationUseCase.createUser(userInfo)
        if (resultUser == 0){
            val userLocal = User(
                0,
                userAccount.value!!.email,
                true,
                0,
                userAccount.value!!.email,
                userAccount.value!!.name,
                typeDocumentsList[typeDocumentSelectedPosition.value!!].valor,
                userAccount.value!!.identification,
                userAccount.value!!.phone.replace(" ",""),
                countriesList[countrySelectedPosition.value!!].pais,
                userAccount.value!!.city
            )
            configurationUseCase.insertUserLocal(userLocal)
            navigateToDashboard.value = true
        } else {
            snackBarAction.value=resultUser
        }
    }

    fun checkOnline(context: Context): Boolean {
        return UtilsNetwork().isOnline(context)
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
                    Injection.providerUserRepository(context),
                    Injection.providerCitiesRepository(context),
                    Injection.providerTypeDocumentRepository(context)
                )
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConfigurationViewModel(userRepository, citiesRepository, typeDocumentRepository) as T
    }
}