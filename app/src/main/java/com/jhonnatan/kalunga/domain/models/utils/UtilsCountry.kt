package com.jhonnatan.kalunga.domain.models.utils

import com.jhonnatan.kalunga.R
import com.jhonnatan.kalunga.domain.models.enumeration.CodeCountries
import com.jhonnatan.kalunga.domain.models.enumeration.EnumerationPhoneMaxLength
import com.jhonnatan.kalunga.domain.models.enumeration.EnumerationWhiteSpaces

/****
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.models.utils
 * Created by Jhonnatan E. Zamudio P. on 25/07/2021 at 11:56 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 ****/

class UtilsCountry {
    fun getIdFlag(country: String): Int {
        when (country) {
            "Colombia" -> return R.mipmap.colombia
            "Venezuela" -> return R.mipmap.venezuela
            "España" -> return R.mipmap.espana
            "Italia" -> return R.mipmap.italia
            "Estados Unidos" -> return R.mipmap.estados_unidos
            "Chile" -> return R.mipmap.chile
            "Perú" -> return R.mipmap.peru
            "Ecuador" -> return R.mipmap.ecuador
            else -> return 0
        }
    }

    fun getWhiteSpaceList(country: String): List<Int> {
        return when (country) {
            CodeCountries.COLOMBIA.value -> EnumerationWhiteSpaces.COLOMBIA.code
            CodeCountries.VENEZUELA.value -> EnumerationWhiteSpaces.VENEZUELA.code
            CodeCountries.ITALIA.value -> EnumerationWhiteSpaces.ITALIA.code
            CodeCountries.ESPANA.value -> EnumerationWhiteSpaces.ESPANA.code
            CodeCountries.ESTADOS_UNIDOS.value -> EnumerationWhiteSpaces.ESTADOS_UNIDOS.code
            CodeCountries.CHILE.value -> EnumerationWhiteSpaces.CHILE.code
            CodeCountries.ECUADOR.value -> EnumerationWhiteSpaces.ECUADOR.code
            CodeCountries.PERU.value -> EnumerationWhiteSpaces.PERU.code
            else -> (listOf())
        }
    }

    fun getMaxLength(country: String): Int {
        return when (country) {
            CodeCountries.COLOMBIA.value -> EnumerationPhoneMaxLength.COLOMBIA.code
            CodeCountries.VENEZUELA.value -> EnumerationPhoneMaxLength.VENEZUELA.code
            CodeCountries.ITALIA.value -> EnumerationPhoneMaxLength.ITALIA.code
            CodeCountries.ESPANA.value -> EnumerationPhoneMaxLength.ESPANA.code
            CodeCountries.ESTADOS_UNIDOS.value -> EnumerationPhoneMaxLength.ESTADOS_UNIDOS.code
            CodeCountries.CHILE.value -> EnumerationPhoneMaxLength.CHILE.code
            CodeCountries.ECUADOR.value -> EnumerationPhoneMaxLength.ECUADOR.code
            CodeCountries.PERU.value -> EnumerationPhoneMaxLength.PERU.code
            else -> (0)
        }
    }
}