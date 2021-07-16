package com.jhonnatan.kalunga.presentation.core.session.viewModels

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jhonnatan.kalunga.domain.models.enumeration.CodeField
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.presentation.core.session.viewModels
 * Created by Laura S. Sarmiento M. on 16/07/2021 at 5:32 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class SignUpViewModel : ViewModel(){

    val errorEmail = MutableLiveData<String>()
    val errorName = MutableLiveData<String>()
    val errorPassword = MutableLiveData<String>()
    val errorPasswordConfirm = MutableLiveData<String>()

    init {
        errorEmail.value = ""
        errorName.value = ""
        errorPassword.value = ""
        errorPasswordConfirm.value = ""
    }

    fun areFieldsEmpty(text: Editable?, field: Int) {
        /*when(field){
            CodeField.EMAIL_FIELD.code ->
        }*/
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