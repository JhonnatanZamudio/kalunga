package com.jhonnatan.kalunga.domain.models.utils

import android.annotation.SuppressLint
import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.models.utils
 * Created by Laura S. Sarmiento M. on 27/07/2021 at 10:38 a. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
class UtilsSecurity {

    private val password = "xMAcAfTt5EB3iuJB"

    @SuppressLint("GetInstance")
    @Throws(Exception::class)
    fun cipherData(data: String): String {
        val secretKeySpec: SecretKeySpec = generateKey(password)
        val cipher: Cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val datoEncriptado: ByteArray = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(datoEncriptado, Base64.DEFAULT).trim()
    }

    @Throws(java.lang.Exception::class)
    private fun generateKey(clave: String): SecretKeySpec {
        val sha: MessageDigest = MessageDigest.getInstance("SHA-256")
        var key = clave.toByteArray(charset("UTF-8"))
        key = sha.digest(key)
        return SecretKeySpec(key, "AES")
    }

    @SuppressLint("GetInstance")
    @Throws(java.lang.Exception::class)
    private fun decipherData(data: String): String {
        val secretKeySpec = generateKey(password)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        val datoDescifrado =
            Base64.decode(data, Base64.DEFAULT)
        val datoDescifradoByte = cipher.doFinal(datoDescifrado)
        return String(datoDescifradoByte)
    }
}