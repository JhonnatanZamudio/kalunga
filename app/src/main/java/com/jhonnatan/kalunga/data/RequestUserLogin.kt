package com.jhonnatan.kalunga.data

import com.google.gson.annotations.SerializedName

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.data
 * Created by Laura S. Sarmiento M. on 11/08/2021 at 1:31 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
data class RequestUserLogin(
    @SerializedName("account") val account: String,
    @SerializedName("password_user") val passwordUser: String
)