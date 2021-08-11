package com.jhonnatan.kalunga.domain.models.utils

import com.jhonnatan.kalunga.domain.models.enumeration.CodePatterns

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.models.utils
 * Created by Laura S. Sarmiento M. on 11/08/2021 at 12:23 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class UtilsFields {
    fun areFieldsEmpty(text: String): Boolean {
        return text.isEmpty()
    }

    fun isValidEmail(text: String): Boolean {
        return text.matches(CodePatterns.EMAIL_VALIDATION.value.toRegex())
    }

    fun isValidLong(text: String, min: Int): Boolean {
        return text.length > min
    }
}