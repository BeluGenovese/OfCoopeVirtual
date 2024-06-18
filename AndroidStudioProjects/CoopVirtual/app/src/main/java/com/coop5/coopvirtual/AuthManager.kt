package com.coop5.coopvirtual

import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AuthManager(private val context: Context) {

    private val TAG = "AuthManager"
    private val logTag = "AuthManagerLogs"

    // URL del servidor donde se realizará la solicitud de inicio de sesión
    private val loginUrl = "http://back.coop5.com.ar:9502/sessionU/open"

    // Llave de encriptación para la contraseña
    private val encryptionKey = "c10ddfd118bf3ae541fbab17328de27e"

    // Llave de encriptación para la sesión
    private val sessionEncryptionKey = "aefead521acbe4004448e768109eda6b"

    fun loginApp(username: String, password: String) {

        Log.d(logTag, "Iniciando solicitud de inicio de sesión para usuario: $username")
        // Encriptar la contraseña
        val encryptedPassword = encryptPassword(password)
        Log.d(logTag, "Contraseña encriptada para usuario $username: $encryptedPassword")
        // Crear el objeto JSON con el nombre de usuario y la contraseña encriptada
        val jsonBody = JSONObject().apply {
            put("username", username) // Nombre de usuario proporcionado por el usuario
            put("password", encryptedPassword) // Contraseña encriptada
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, loginUrl, jsonBody,
            Response.Listener { response ->
                try {
                    val status = response.getInt("status")
                    Log.d(TAG, "Respuesta del servidor: $response")

                    if (status == 0) {
                        // Inicio de sesión exitoso
                        val idSession = response.getInt("idSession")
                        val session = response.getString("session")

                        // Desencriptar la sesión recibida del servidor
                        val decryptedSession = decryptSession(session)
                        Log.d(logTag, "Sesión desencriptada para usuario $username: $decryptedSession")
                        // Encriptar la sesión antes de guardarla o enviarla a otro lugar
                        val encryptedSession = encryptSession(decryptedSession)
                        Log.d(logTag, "Sesión encriptada para usuario $username: $encryptedSession")
                        // Ejemplo de guardar la sesión en SharedPreferences
                        Utilidades.saveSesionToSharedPreferences(context, encryptedSession)

                        Log.d(logTag, "Sesión guardada en SharedPreferences para usuario $username")


                    } else {
                        val obs = response.optString("obs", "Error desconocido")


                        Log.d(logTag, "Error al iniciar sesión para usuario $username: $obs")
                        Toast.makeText(context, "Error al iniciar sesión: $obs", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Log.e(logTag, "Error al parsear JSON para usuario $username: $e")

                    Log.e(TAG, "Error al parsear JSON: $e")
                    Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "Error de red: $error")
                Toast.makeText(context, "Error de red: $error", Toast.LENGTH_SHORT).show()
            }
        )

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsonObjectRequest)
    }
    fun getUsername(): String {
        Log.d(logTag, "Obteniendo nombre de usuario: coopApp")
        return "coopApp"

        // Implementación para obtener el nombre de usuario
    }

    fun getEncryptedPassword(password: String): String {
        val encryptedPassword = encryptPassword(password)
        Log.d(logTag, "Encriptando contraseña: $encryptedPassword")
        return encryptedPassword

    }
    // Método para encriptar la contraseña
    private fun encryptPassword(password: String): String {
        return try {
            val iv = generateRandomIV()
            val ivSpec = IvParameterSpec(iv)
            val secretKeySpec = SecretKeySpec(encryptionKey.toByteArray(StandardCharsets.UTF_8), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)
            val encrypted = cipher.doFinal(password.toByteArray(StandardCharsets.UTF_8))
            val ivAndEncrypted = iv + encrypted
            Base64.encodeToString(ivAndEncrypted, Base64.DEFAULT)



        } catch (ex: Exception) {
            Log.e(TAG, "Error al encriptar la contraseña: ${ex.message}")
            throw RuntimeException(ex)
        }
    }

    // Método para desencriptar la sesión recibida del servidor
    private fun decryptSession(encryptedSession: String): String {
        return try {
            val key = sessionEncryptionKey // Llave para desencriptar la sesión (debería ser la misma que en el backend)
            val decodedBytes = Base64.decode(encryptedSession, Base64.DEFAULT)
            val iv = decodedBytes.copyOfRange(0, 16)
            val encrypted = decodedBytes.copyOfRange(16, decodedBytes.size)

            val ivSpec = IvParameterSpec(iv)
            val secretKeySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)

            val decryptedBytes = cipher.doFinal(encrypted)
            String(decryptedBytes, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            Log.e(TAG, "Error al desencriptar la sesión: ${ex.message}")
            throw RuntimeException(ex)
        }
    }

    // Método para encriptar la sesión antes de enviarla al servidor
    private fun encryptSession(session: String): String {
        return try {
            val iv = generateRandomIV()
            val ivSpec = IvParameterSpec(iv)
            val secretKeySpec = SecretKeySpec(sessionEncryptionKey.toByteArray(StandardCharsets.UTF_8), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)
            val encrypted = cipher.doFinal(session.toByteArray(StandardCharsets.UTF_8))
            val ivAndEncrypted = iv + encrypted
            Base64.encodeToString(ivAndEncrypted, Base64.DEFAULT)
        } catch (ex: Exception) {
            Log.e(TAG, "Error al encriptar la sesión: ${ex.message}")
            throw RuntimeException(ex)
        }
    }

    // Método para generar un IV aleatorio
    private fun generateRandomIV(): ByteArray {
        val random = SecureRandom()
        val iv = ByteArray(16) // Utilizar la misma longitud que en PHP (16 bytes)
        random.nextBytes(iv)
        return iv
    }
}
