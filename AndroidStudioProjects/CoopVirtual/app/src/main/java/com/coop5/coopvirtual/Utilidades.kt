package com.coop5.coopvirtual



import android.content.Context
import android.content.SharedPreferences



object Utilidades {



    private const val PREF_NAME = "MyPreferences"
    private const val TOKEN_KEY = "token"
    private const val NOMBRE_KEY = "nombre"
    private const val APELLIDO_KEY = "apellido"


    // Función para guardar el token en SharedPreferences
    fun saveTokenToSharedPreferences(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    // Función para guardar el nombre en SharedPreferences
    fun saveNombreToSharedPreferences(context: Context, nombre: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(NOMBRE_KEY, nombre)
        editor.apply()
    }
    // Función para guardar el nombre en SharedPreferences
    fun saveApellidoToSharedPreferences(context: Context, apellido: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(APELLIDO_KEY, apellido)
        editor.apply()
    }

    // Función para obtener el token desde SharedPreferences
    fun getTokenFromSharedPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, null)
    }
    // Función para limpiar el token de las preferencias compartidas
    fun limpiarToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.apply()
    }
    // Función para limpiar las preferencias compartidas
    fun limpiarPreferenciasCompartidas(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Elimina todos los datos de las preferencias compartidas
        editor.apply()
    }

}


