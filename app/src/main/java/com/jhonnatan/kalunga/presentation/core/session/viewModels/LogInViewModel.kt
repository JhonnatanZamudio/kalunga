package com.jhonnatan.kalunga.presentation.core.session.viewModels

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jhonnatan.kalunga.domain.models.entities.UserAccountData
import com.jhonnatan.kalunga.domain.models.enumeration.CodeField
import com.jhonnatan.kalunga.domain.models.enumeration.ResponseErrorField
import com.jhonnatan.kalunga.domain.useCases.LogInUseCase
import com.jhonnatan.kalunga.domain.useCases.SignUpUseCase
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.presentation.core.session.viewModels
 * Created by Laura S. Sarmiento M. on 11/08/2021 at 10:51 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/

class LogInViewModel : ViewModel() {

    private var passwordCounter = MutableLiveData<Int>()
    var showPassword = MutableLiveData<Boolean>()
    val navigateToSignUp = MutableLiveData<Boolean>()
    val navigateToForgetPassword = MutableLiveData<Boolean>()
    private val logInUseCase = LogInUseCase()

    init {
        navigateToSignUp.value = false
        navigateToForgetPassword.value = false
        passwordCounter.value = 0
    }

    fun areFieldsEmpty(text: Editable?, field: Int) {

    }

    fun showPassword(){
        passwordCounter.value = passwordCounter.value!! + 1
        showPassword.value = logInUseCase.isNumberPair(passwordCounter.value!!)
    }

    fun navigateToSignUp() {
        navigateToSignUp.value = true
    }

    fun navigateToForgetPassword() {
        navigateToForgetPassword.value = true
    }
}



@DelicateCoroutinesApi
@Suppress("UNCHECKED_CAST")
class LogInViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: LogInViewModelFactory? = null
        fun getInstance(): LogInViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LogInViewModelFactory()
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LogInViewModel() as T
    }
}