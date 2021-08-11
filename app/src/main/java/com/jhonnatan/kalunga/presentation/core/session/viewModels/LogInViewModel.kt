package com.jhonnatan.kalunga.presentation.core.session.viewModels

import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jhonnatan.kalunga.R
import com.jhonnatan.kalunga.data.RequestUserLogin
import com.jhonnatan.kalunga.data.user.entities.RequestUsers
import com.jhonnatan.kalunga.data.user.repository.UserRepository
import com.jhonnatan.kalunga.domain.injectionOfDependencies.Injection
import com.jhonnatan.kalunga.domain.models.entities.UserAccountData
import com.jhonnatan.kalunga.domain.models.enumeration.CodeField
import com.jhonnatan.kalunga.domain.models.enumeration.CodeLong
import com.jhonnatan.kalunga.domain.models.enumeration.CodeSnackBarCloseAction
import com.jhonnatan.kalunga.domain.models.enumeration.ResponseErrorField
import com.jhonnatan.kalunga.domain.models.utils.UtilsFields
import com.jhonnatan.kalunga.domain.models.utils.UtilsNetwork
import com.jhonnatan.kalunga.domain.models.utils.UtilsSecurity
import com.jhonnatan.kalunga.domain.useCases.LogInUseCase
import com.jhonnatan.kalunga.domain.useCases.SignUpUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.presentation.core.session.viewModels
 * Created by Laura S. Sarmiento M. on 11/08/2021 at 10:51 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/

class LogInViewModel(userRepository: UserRepository) : ViewModel() {

    private var passwordCounter = MutableLiveData<Int>()
    var showPassword = MutableLiveData<Boolean>()
    val navigateToSignUp = MutableLiveData<Boolean>()
    val navigateToForgetPassword = MutableLiveData<Boolean>()
    var userAccount = MutableLiveData<UserAccountData>()
    val errorEmail = MutableLiveData<String>()
    val errorPassword = MutableLiveData<String>()
    val buttonLogInDrawable = MutableLiveData<Int>()
    val buttonLogInEnable = MutableLiveData<Boolean>()
    val snackBarTextWarning = MutableLiveData<String>()
    val snackBarTextError = MutableLiveData<String>()
    val snackBarTextInfo = MutableLiveData<String>()
    val snackBarTextSuccess = MutableLiveData<String>()
    val snackBarNavigate = MutableLiveData<Int>()
    val snackBarAction = MutableLiveData<Int>()
    private val logInUseCase = LogInUseCase(userRepository)
    private var validEmail = MutableLiveData<Int>()
    private var validPassword = MutableLiveData<Int>()

    init {
        navigateToSignUp.value = false
        navigateToForgetPassword.value = false
        passwordCounter.value = 0
        validEmail.value = 0
        validPassword.value = 0
        errorEmail.value = ResponseErrorField.DEFAULT.value
        errorPassword.value = ResponseErrorField.DEFAULT.value
        userAccount.value = UserAccountData("","","","", "","","","")
        snackBarNavigate.value = CodeSnackBarCloseAction.NONE.code
    }

    fun areFieldsEmpty(text: Editable?, field: Int) {

        if (UtilsFields().areFieldsEmpty(text.toString())) {
            setErrorText(field, ResponseErrorField.ERROR_EMPTY.value)
            when (field) {
                CodeField.EMAIL_FIELD.code -> validEmail.value = 0
                CodeField.PASSWORD_FIELD.code -> validPassword.value = 0
            }
            changeEnableButton()
        } else {
            setErrorText(field, ResponseErrorField.DEFAULT.value)
            when (field) {
                CodeField.EMAIL_FIELD.code -> {
                    userAccount.value!!.email = text.toString()
                    userAccount.value!!.id = text.toString()
                    isValidEmail(text)
                }
                CodeField.PASSWORD_FIELD.code -> {
                    userAccount.value!!.password = text.toString()
                    isValidLong(text)
                }
            }
        }
    }

    private fun setErrorText(field: Int, value: String) {
        when (field) {
            CodeField.EMAIL_FIELD.code -> errorEmail.value = value
            CodeField.PASSWORD_FIELD.code -> errorPassword.value = value
        }
    }

    private fun isValidEmail(text: Editable?) {
        if (UtilsFields().isValidEmail(text.toString())) {
            setErrorText(CodeField.EMAIL_FIELD.code, ResponseErrorField.DEFAULT.value)
            validEmail.value = 1
            changeEnableButton()
        } else {
            setErrorText(CodeField.EMAIL_FIELD.code, ResponseErrorField.ERROR_INVALID_MAIL.value)
            validEmail.value = 0
            changeEnableButton()
        }
    }

    private fun isValidLong(text: Editable?) {
        if (UtilsFields().isValidLong(text.toString(), CodeLong.PASSWORD_FIELD.code)) {
            setErrorText(CodeField.PASSWORD_FIELD.code, ResponseErrorField.DEFAULT.value)
            validPassword.value = 1
            changeEnableButton()
        } else {
            setErrorText(
                CodeField.PASSWORD_FIELD.code,
                ResponseErrorField.ERROR_LONG_CHARACTERS.value + CodeLong.PASSWORD_FIELD.code + ResponseErrorField.ERROR_CHARACTERS.value
            )
            validPassword.value = 0
            changeEnableButton()
        }
    }

    private fun changeEnableButton() {
        if (logInUseCase.changeEnableButton(
                validEmail.value!!,
                validPassword.value!!
            )
        ) {
            buttonLogInDrawable.value = R.drawable.boton_oscuro
            buttonLogInEnable.value = true
        } else {
            buttonLogInDrawable.value = R.drawable.boton_oscuro_disabled
            buttonLogInEnable.value = false
        }
    }

    fun showPassword(){
        passwordCounter.value = passwordCounter.value!! + 1
        showPassword.value = UtilsFields().isNumberPair(passwordCounter.value!!)
    }

    fun navigateToSignUp() {
        navigateToSignUp.value = true
    }

    fun navigateToForgetPassword() {
        navigateToForgetPassword.value = true
    }

    fun checkOnline(context: Context): Boolean {
        return UtilsNetwork().isOnline(context)
    }


    fun login() {
        viewModelScope.launch {
            loginUserRemote()
        }
    }

    suspend fun loginUserRemote(){
        val userInfo = RequestUserLogin(
            userAccount.value!!.email,
            UtilsSecurity().cipherData(userAccount.value!!.password)!!,
        )
        val resultUser = logInUseCase.loginUser(userInfo)
    }
}



@DelicateCoroutinesApi
@Suppress("UNCHECKED_CAST")
class LogInViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: LogInViewModelFactory? = null
        fun getInstance(context: Context): LogInViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LogInViewModelFactory(
                    Injection.providerUserRepository(context))
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LogInViewModel(userRepository) as T
    }
}