package com.coop5.coopvirtual

class ValidacionesUtils {

    fun validarNombre(nombre: String): Boolean {
        return nombre.length in 1..15
    }

    fun validarApellido(apellido: String): Boolean {
        return apellido.length in 1..150
    }

    fun validarPassword(contraseña: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@$!%*?&]{8,64}\$".toRegex()
        return contraseña.matches(regex)
    }

    fun validarCorreo(correo: String): Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@(.+)\$".toRegex()
        return correo.matches(regex)
    }
}
