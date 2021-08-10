package com.jhonnatan.kalunga.domain.models.enumeration

/**
 * Project: kalunga
 * From: com.jhonnatan.kalunga.domain.models.enumeration
 * Created by Laura S. Sarmiento M. on 16/07/2021 at 6:59 p. m.
 * More info:  https://venecambios-kalunga.com/
 * All rights reserved 2021.
 **/
enum class ResponseErrorField (val value: String){
    DEFAULT(""),
    ERROR_EMPTY("El campo está vacío"),
    ERROR_INVALID_MAIL("Ingresa un correo válido"),
    ERROR_LONG_CHARACTERS("Debe ser mayor a "),
    ERROR_CHARACTERS(" caracteres"),
    ERROR_PASSWORD_DOESNT_MATCH("Las contraseñas no coinciden"),
    ERROR_INVALID_CITY("Seleccione una ciudad sugerida o escriba otra"),
    ERROR_INVALID_PHONE("El número no es valido")
}