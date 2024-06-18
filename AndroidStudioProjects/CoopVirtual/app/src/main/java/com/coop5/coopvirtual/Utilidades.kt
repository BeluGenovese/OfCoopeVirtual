package com.coop5.coopvirtual

import android.content.Context
import android.content.SharedPreferences

object Utilidades {

    private const val PREF_NAME = "MyPreferences"
    private const val ID_SESION_KEY = "idSesion"
    private const val SESION_KEY = "sesion"
    private const val NOMBRE_KEY = "nombre"
    private const val APELLIDO_KEY = "apellido"

    // Función para guardar el ID de sesión en SharedPreferences
    fun saveIdSesionToSharedPreferences(context: Context, idSesion: Int) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(ID_SESION_KEY, idSesion)
        editor.apply()
    }

    // Función para guardar la sesión en SharedPreferences
    fun saveSesionToSharedPreferences(context: Context, sesion: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(SESION_KEY, sesion)
        editor.apply()
    }

    // Función para guardar el nombre en SharedPreferences
    fun saveNombreToSharedPreferences(context: Context, nombre: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(NOMBRE_KEY, nombre)
        editor.apply()
    }

    // Función para guardar el apellido en SharedPreferences
    fun saveApellidoToSharedPreferences(context: Context, apellido: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(APELLIDO_KEY, apellido)
        editor.apply()
    }

    // Función para obtener el ID de sesión desde SharedPreferences
    fun getIdSesionFromSharedPreferences(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(ID_SESION_KEY, 0) // 0 es el valor predeterminado si no se encuentra
    }

    // Función para obtener la sesión desde SharedPreferences
    fun getSesionFromSharedPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(SESION_KEY, null)
    }

    // Función para limpiar las preferencias compartidas
    fun limpiarPreferenciasCompartidas(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Elimina todos los datos de las preferencias compartidas
        editor.apply()
    }
}
