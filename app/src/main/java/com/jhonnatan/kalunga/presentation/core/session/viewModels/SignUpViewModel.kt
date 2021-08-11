package com.jhonnatan.kalunga.presentation.core.session.viewModels

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jhonnatan.kalunga.R
import com.jhonnatan.kalunga.domain.models.entities.UserAccountData
import com.jhonnatan.kalunga.domain.models.enumeration.CodeField
import com.jhonnatan.kalunga.domain.models.enumeration.CodeLong
import com.jhonnatan.kalunga.domain.models.enumeration.ResponseErrorField
import com.jhonnatan.kalunga.domain.models.utils.UtilsFields
import com.jhonnatan.kalunga.domain.useCases.SignUpUseCase
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.presentation.core.session.viewModels
 * Created by Laura S. Sarmiento M. on 16/07/2021 at 5:32 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class SignUpViewModel : ViewModel() {

    val errorEmail = MutableLiveData<String>()
    val errorName = MutableLiveData<String>()
    val errorPassword = MutableLiveData<String>()
    val errorPasswordConfirm = MutableLiveData<String>()
    val buttonContinueDrawable = MutableLiveData<Int>()
    val buttonContinueEnable = MutableLiveData<Boolean>()
    private val signUpUseCase = SignUpUseCase()
    val navigateToConfiguration = MutableLiveData<Boolean>()
    val navigateToLogIn = MutableLiveData<Boolean>()
    var userAccount = MutableLiveData<UserAccountData>()
    var showPassword = MutableLiveData<Boolean>()
    var showPasswordConfirm = MutableLiveData<Boolean>()
    private var passwordCounter = MutableLiveData<Int>()
    private var passwordConfirmCounter = MutableLiveData<Int>()
    private var validEmail = MutableLiveData<Int>()
    private var validName = MutableLiveData<Int>()
    private var validPassword = MutableLiveData<Int>()
    private var validPasswordConfirm = MutableLiveData<Int>()

    init {
        navigateToConfiguration.value = false
        navigateToLogIn.value = false
        errorEmail.value = ResponseErrorField.DEFAULT.value
        errorName.value = ResponseErrorField.DEFAULT.value
        errorPassword.value = ResponseErrorField.DEFAULT.value
        errorPasswordConfirm.value = ResponseErrorField.DEFAULT.value
        userAccount.value = UserAccountData("","","","", "","","","")
        passwordCounter.value = 0
        passwordConfirmCounter.value = 0
        validEmail.value = 0
        validName.value = 0
        validPassword.value = 0
        validPasswordConfirm.value = 0
    }

    fun areFieldsEmpty(text: Editable?, field: Int) {

        if (UtilsFields().areFieldsEmpty(text.toString())) {
            setErrorText(field, ResponseErrorField.ERROR_EMPTY.value)
            when (field) {
                CodeField.EMAIL_FIELD.code -> validEmail.value = 0
                CodeField.NAME_FIELD.code -> validName.value = 0
                CodeField.PASSWORD_FIELD.code -> validPassword.value = 0
                CodeField.PASSWORD_CONFIRM_FIELD.code -> validPasswordConfirm.value = 0
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
                CodeField.NAME_FIELD.code -> {
                    userAccount.value!!.name = text.toString()
                    isValidLong(text,CodeField.NAME_FIELD.code,CodeLong.NAME_FIELD.code, validName)
                }
                CodeField.PASSWORD_FIELD.code -> {
                    userAccount.value!!.password = text.toString()
                    isValidLong(
                        text,
                        CodeField.PASSWORD_FIELD.code,
                        CodeLong.PASSWORD_FIELD.code,
                        validPassword
                    )
                    arePasswordsEqual(userAccount.value!!.passwordConfirm,userAccount.value!!.password)
                }
                CodeField.PASSWORD_CONFIRM_FIELD.code -> {
                    userAccount.value!!.passwordConfirm = text.toString()
                    arePasswordsEqual(userAccount.value!!.passwordConfirm,userAccount.value!!.password)
                }
            }
        }
    }

    private fun setErrorText(field: Int, value: String) {
        when (field) {
            CodeField.EMAIL_FIELD.code -> errorEmail.value = value
            CodeField.NAME_FIELD.code -> errorName.value = value
            CodeField.PASSWORD_FIELD.code -> errorPassword.value = value
            CodeField.PASSWORD_CONFIRM_FIELD.code -> errorPasswordConfirm.value = value
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

    private fun isValidLong(text: Editable?, code: Int, minValue: Int, validItem: MutableLiveData<Int>) {
        if (UtilsFields().isValidLong(text.toString(), minValue)) {
            setErrorText(code, ResponseErrorField.DEFAULT.value)
            validItem.value = 1
            changeEnableButton()
        } else {
            setErrorText(
                code,
                ResponseErrorField.ERROR_LONG_CHARACTERS.value + minValue + ResponseErrorField.ERROR_CHARACTERS.value
            )
            validItem.value = 0
            changeEnableButton()
        }
    }

    private fun arePasswordsEqual(confirmPassword: String, password: String) {
        if (signUpUseCase.arePasswordsEqual(confirmPassword, password)) {
            setErrorText(CodeField.PASSWORD_CONFIRM_FIELD.code, ResponseErrorField.DEFAULT.value)
            validPasswordConfirm.value = 1
            changeEnableButton()
        } else {
            setErrorText(
                CodeField.PASSWORD_CONFIRM_FIELD.code,
                ResponseErrorField.ERROR_PASSWORD_DOESNT_MATCH.value
            )
            validPasswordConfirm.value = 0
            changeEnableButton()
        }
    }

    private fun changeEnableButton() {
        if (signUpUseCase.changeEnableButton(
                validEmail.value!!,
                validName.value!!,
                validPassword.value!!,
                validPasswordConfirm.value!!
            )
        ) {
            buttonContinueDrawable.value = R.drawable.boton_oscuro
            buttonContinueEnable.value = true
        } else {
            buttonContinueDrawable.value = R.drawable.boton_oscuro_disabled
            buttonContinueEnable.value = false
        }
    }

    fun navigateToConfiguration() {
        navigateToConfiguration.value = true
    }

    fun navigateToLogIn() {
        navigateToLogIn.value = true
    }

    fun showPassword(field: Int){
        when (field) {
            CodeField.PASSWORD_FIELD.code -> {
                passwordCounter.value = passwordCounter.value!! + 1
                showPassword.value = signUpUseCase.isNumberPair(passwordCounter.value!!)
            }
            CodeField.PASSWORD_CONFIRM_FIELD.code -> {
                passwordConfirmCounter.value = passwordConfirmCounter.value!! + 1
                showPasswordConfirm.value = signUpUseCase.isNumberPair(passwordConfirmCounter.value!!)
            }
        }
    }

}


@DelicateCoroutinesApi
@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: SignUpViewModelFactory? = null
        fun getInstance(): SignUpViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SignUpViewModelFactory()
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel() as T
    }
}