package com.coop5.coopvirtual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class RecuperarClave : AppCompatActivity() {

    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_clave)


        val btnVolver: Button = findViewById(R.id.btnVolver)
        val btnEnviarCorreo: Button = findViewById(R.id.btnEnviarCorreo)
        val editTextCorreo: EditText = findViewById(R.id.editTextCorreo)



        btnVolver.setOnClickListener {
            Log.d("RecuperarClave8", "Botón 'Volver' presionado.")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }



        btnEnviarCorreo.setOnClickListener {
            val correo = editTextCorreo.text.toString().trim()

            if (correo.isNotEmpty() && correo.contains("@")) {
                Log.d("RecuperarClave7", "Correo ingresado: $correo")
                // Inicia sesión en el backend
                loginUsuario(correo, this)
            } else {
                Log.d("RecuperarClave6", "Correo inválido: $correo")
                Toast.makeText(
                    this,
                    "Por favor, ingrese una dirección de correo válida.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loginUsuario(correo: String, context: Context) {

        Log.d("RecuperarClave2", "Iniciando sesión en el backend para el correo: $correo")
        val url = "http://back.coop5.com.ar:9502/sessionU/open" // Utilizar HTTPS en lugar de HTTP
        val password = "£j4-G&30E%O0ZtG[£%O}YI'yU+VtL7'" // Contraseña en texto plano

        // Encriptar la contraseña utilizando tu lógica de encriptación
        val encryptedPassword = encryptPassword(password)

        Log.d(
            "RecuperarClave2",
            "Contraseña encriptada: $encryptedPassword"
        ) // Agregar log de contraseña encriptada

        // Crear el objeto JSON con el correo y la contraseña encriptada
        val jsonBody = JSONObject().apply {
            put("username", "appCoop") // Usar el correo de la aplicación
            put("password", encryptedPassword) // Usar la contraseña encriptada
        }

        Log.d(
            "RecuperarClave2",
            "Cuerpo de la solicitud JSON: $jsonBody"
        ) // Agregar log del cuerpo de la solicitud JSON

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            Response.Listener { response ->
                try {
                    val status = response.getInt("status")
                    Log.d(
                        "RecuperarClave2",
                        "Respuesta del servidor: $response"
                    ) // Agregar log de la respuesta del servidor

                    if (status == 0) {
                        // Inicio de sesión exitoso, obtener idSession y enviar correo de recuperación
                        val idSession = response.getInt("idSession")
                        val session = response.getString("session")

                        // Desencriptar la sesión recibida del servidor
                        val decryptedSession = decryptSession(session)

                        // Realizar cualquier operación necesaria con la sesión desencriptada

                        // Encriptar la sesión antes de enviarla de vuelta al servidor
                        val encryptedSession = encryptSession(decryptedSession)

                        // Guardar la sesión en SharedPreferences
                        Utilidades.saveSesionToSharedPreferences(this, encryptedSession)
                        enviarCorreoRecuperacion(correo, context, idSession, encryptedSession)

                    } else {
                        val obs = response.optString("obs", "Error desconocido")
                        Log.d("RecuperarClave", "Error al iniciar sesión: $obs")
                        Toast.makeText(context, "Error al iniciar sesión: $obs", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: JSONException) {
                    Log.e("RecuperarClave", "Error al parsear JSON: $e")
                    Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("RecuperarClave9", "Error de red: $error")
                Toast.makeText(context, "Error de red: $error", Toast.LENGTH_SHORT).show()
            }
        )

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsonObjectRequest)
    }

    private fun decryptSession(encryptedSession: String): String {
        Log.d(
            "RecuperarClaseDes",
            "Sesión encriptada recibida para desencriptar: $encryptedSession"
        )
        return try {
            val key = "b6a8f4cd59d4d2c3b22cf14fc7ffe310" // 32-byte key (256 bits)
            val decodedBytes = Base64.decode(encryptedSession, Base64.DEFAULT)
            val iv = decodedBytes.copyOfRange(0, 16)
            val encrypted = decodedBytes.copyOfRange(16, decodedBytes.size)

            Log.d(
                "RecuperarClaseDes",
                "IV decodificado: ${Base64.encodeToString(iv, Base64.DEFAULT)}"
            )
            Log.d(
                "RecuperarClaseDes",
                "Datos encriptados: ${Base64.encodeToString(encrypted, Base64.DEFAULT)}"
            )

            val ivSpec = IvParameterSpec(iv)
            val secretKeySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)

            val decryptedBytes = cipher.doFinal(encrypted)
            val decryptedSession = String(decryptedBytes, StandardCharsets.UTF_8)

            Log.d("RecuperarClaseDes", "Sesión desencriptada: $decryptedSession")
            decryptedSession
        } catch (ex: Exception) {
            Log.e("RecuperarClaseDes", "Error al desencriptar la sesión: ${ex.message}")
            throw RuntimeException(ex)
        }
    }

    private fun encryptSession(session: String): String {
        Log.d("RecuperarClaseEncry", "Sesión recibida para encriptar: $session")
        return try {
            val key = "aefead521acbe4004448e768109eda6b" // 32-byte key (256 bits)
            val iv = generateRandomIV()

            Log.d(
                "RecuperarClaseEncry",
                "IV generado: ${Base64.encodeToString(iv, Base64.DEFAULT)}"
            )

            val ivSpec = IvParameterSpec(iv)
            val secretKeySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)

            val encrypted = cipher.doFinal(session.toByteArray(StandardCharsets.UTF_8))
            val ivAndEncrypted = iv + encrypted
            val encryptedSession = Base64.encodeToString(ivAndEncrypted, Base64.DEFAULT)

            Log.d("RecuperarClaseEncry", "Sesión encriptada: $encryptedSession")
            encryptedSession
        } catch (ex: Exception) {
            Log.e("RecuperarClaseEncry", "Error al encriptar la sesión: ${ex.message}")
            throw RuntimeException(ex)
        }
    }

    private fun encryptPassword(password: String): String {
        try {
            val key = "c10ddfd118bf3ae541fbab17328de27e" // 32-byte key (256 bits)
            val iv = generateRandomIV()

            // Log para mostrar el IV generado
            Log.d("RecuperarClave10", "IV generado: ${Base64.encodeToString(iv, Base64.DEFAULT)}")

            val ivSpec = IvParameterSpec(iv)
            val secretKeySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)

            val encrypted = cipher.doFinal(password.toByteArray(StandardCharsets.UTF_8))
            val ivAndEncrypted = iv + encrypted

            // Log para mostrar la contraseña encriptada
            Log.d(
                "Recuperarclave11",
                "Contraseña encriptada: ${Base64.encodeToString(ivAndEncrypted, Base64.DEFAULT)}"
            )

            return Base64.encodeToString(ivAndEncrypted, Base64.DEFAULT)
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    private fun generateRandomIV(): ByteArray {
        val random = SecureRandom()
        val iv = ByteArray(16) // Utilizar la misma longitud que en PHP (16 bytes)
        random.nextBytes(iv)
        Log.d("RecueprarClave1", "Generated IV: ${Base64.encodeToString(iv, Base64.DEFAULT)}")
        return iv
    }



    private fun enviarCorreoRecuperacion(
        correo: String,
        context: Context,
        idSesion: Int,
        session: String
    ) {
        Log.d("RecuperarClave", "Iniciando el proceso de enviar correo de recuperación.")
        Log.d("RecuperarClave", "Correo: $correo")
        Log.d("RecuperarClave", "ID de Sesión: $idSesion")
        Log.d("RecuperarClave", "Sesión original: $session")

        val url =
            "http://back.coop5.com.ar:9502/cuenta/requestPasswordReset/$idSesion" // URL del endpoint en el backend

        val jsonBody = JSONObject().apply {
            put(
                "email",
                correo
            ) // Agregar el correo electrónico proporcionado por el usuario al JSON
        }
        Log.d("RecuperarClave", "Cuerpo de la solicitud JSON: $jsonBody")

        val cleanSession = session.replace("\n", "") // Remover saltos de línea
        Log.d("RecuperarClave", "Sesión limpia: $cleanSession")

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonBody,
            Response.Listener { response ->
                try {
                    Log.d("RecuperarClave", "Respuesta del servidor: $response")

                    val status = response.getInt("status")

                    if (status == 0) {
                        val obs = response.optString("obs", "Solicitud enviada")
                        Log.d("RecuperarClave", "Correo de recuperación enviado exitosamente: $obs")
                        Toast.makeText(context, obs, Toast.LENGTH_LONG).show()

                        // Crear un Handler para postergar la muestra del segundo mensaje
                        val handler = Handler()
                        handler.postDelayed({
                            // Muestra del segundo mensaje después de un breve retraso
                            Toast.makeText(
                                context,
                                "Revise su correo electrónico para continuar",
                                Toast.LENGTH_LONG
                            ).show()

                            val editTextCorreo: EditText = findViewById(R.id.editTextCorreo)
                            val correoUsuario = editTextCorreo.text.toString().trim()
                            editTextCorreo.text.clear()

                            // Iniciar la actividad de inicio de sesión
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.putExtra("correo_usuario", correoUsuario)
                            startActivity(intent)
                            Log.d("RecuperarClave12", "Actividad de inicio de sesión iniciada.")
                            finish()
                        }, 1000)
                    } else {
                        val obs = response.optString("obs", "Error desconocido")
                        Log.d("RecuperarClave", "Error al enviar correo de recuperación: $obs")
                        Toast.makeText(
                            context,
                            "Error al enviar correo de recuperación: $obs",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    Log.e("RecuperarClave", "Error al parsear JSON: $e")
                    Toast.makeText(
                        context,
                        "Error al enviar correo de recuperación",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("RecuperarClave", "Error de red: $error")
                Toast.makeText(context, "Error de red: $error", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["s"] = cleanSession // Agregar la sesión a los encabezados
                Log.d("RecuperarClave", "Encabezados de la solicitud: $headers")
                return headers
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsonObjectRequest)
    }
}

