package com.coop5.coopvirtual

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.SecureRandom

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var btnShowPassword: ImageButton
    private var passwordVisible: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        // Inicializa las vistas



        loginButton = findViewById(R.id.btn_ingresar)


        usernameEditText = findViewById(R.id.usernameEditText)


        // Recuperar el correo pasado en el Intent
        val correoUsuario = intent.getStringExtra("correo_usuario")

        // Colocar el correo en el campo de usuario
        if (correoUsuario != null) {
            usernameEditText.setText(correoUsuario)
        }



        passwordEditText = findViewById(R.id.editTextPassword)


        btnShowPassword = findViewById(R.id.btnShowPassword)


        btnShowPassword.setOnClickListener {
            // Alternar entre mostrar u ocultar la contraseña
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                // Mostrar contraseña
                passwordEditText.transformationMethod = null
                btnShowPassword.setImageResource(com.google.android.material.R.drawable.design_ic_visibility) // Cambiar el icono a "mostrar contraseña"
                btnShowPassword.setColorFilter(Color.BLACK) // Cambiar el color del ícono a negro
            } else {
                // Ocultar contraseña
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                btnShowPassword.setImageResource(com.google.android.material.R.drawable.design_ic_visibility_off) // Cambiar el icono a "ocultar contraseña"
                btnShowPassword.setColorFilter(Color.BLACK) // Cambiar el color del ícono a negro
            }

            // Mover el cursor al final del texto
            passwordEditText.setSelection(passwordEditText.text.length)
        }



        val textViewIngresarInvitado: TextView = findViewById(R.id.text_view_ingresar_invitado)

        val textoCompleto2 = textViewIngresarInvitado.text.toString()
        val spannableString2 = SpannableString(textoCompleto2)
        val colorVerdePersonalizado = ContextCompat.getColor(this, R.color.coope)
        val colorSpan2 = ForegroundColorSpan(colorVerdePersonalizado)
        val inicioTextoVerde = textoCompleto2.indexOf("invitado")


        spannableString2.setSpan(
            colorSpan2,
            inicioTextoVerde,
            inicioTextoVerde + "invitado".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(
            StyleSpan(android.graphics.Typeface.BOLD),
            inicioTextoVerde,
            inicioTextoVerde + "invitado".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(
            UnderlineSpan(),
            inicioTextoVerde,
            inicioTextoVerde + "invitado".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textViewIngresarInvitado.text = spannableString2

        textViewIngresarInvitado.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        loginButton.setOnClickListener {

            val email = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Por favor ingrese email y contraseña.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Encriptar la contraseña usando AES-256-CBC
                val encryptedPassword = encryptPassword(password)
                val decryptedPassword = decryptPassword(encryptedPassword)

                // Agregar logs para mostrar lo que ingresa el usuario
                Log.d("LoginActivity", "Email ingresado: $email")
                Log.d("LoginActivity", "Contraseña encriptada: $encryptedPassword")
                Log.d("LoginActivity5", "Contraseña desencriptada: $decryptedPassword")

                val url = "http://back.coop5.com.ar:9502/sessionC/open"

                val jsonBody = JSONObject()
                try {
                    jsonBody.put("email", email)
                    jsonBody.put("password", encryptedPassword.trim())
                    // Eliminar espacios en blanco al final
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


                Log.d("LoginActivity", "Enviando solicitud a: $url")
                Log.d("LoginActivity", "Datos enviados: $jsonBody")

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, jsonBody,
                    Response.Listener { response ->
                        Log.d("LoginActivity", "Respuesta del servidor: $response")

                        try {

                            val status = response.getInt("status")

                            if (status == 0) {

                                Log.d("LoginActivity", "Inicio de sesión exitoso")
                                val idSession = response.getInt("idSession")
                                val session = response.getString("session")
                                val nombre = response.getString("nombre")
                                val apellido = response.getString("apellido")

                                // Guardar idSesion y sesion en SharedPreferences
                                Utilidades.saveIdSesionToSharedPreferences(this, idSession)
                                Utilidades.saveSesionToSharedPreferences(this, session)
                                // Guardar nombre y apellido en SharedPreferences
                                Utilidades.saveNombreToSharedPreferences(this, nombre)
                                Utilidades.saveApellidoToSharedPreferences(this, apellido)

                                // Muestra un mensaje de sesión iniciada
                                Toast.makeText(
                                    this@LoginActivity,
                                    "¡Sesión iniciada!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, MainSesionActivity::class.java)
                                intent.putExtra("USERNAME", email)
                                intent.putExtra("idSession", idSession)
                                intent.putExtra("session", session)
                                intent.putExtra("nombre", nombre)
                                intent.putExtra("apellido", apellido)

                                startActivity(intent)
                                finish()
                            } else {
                                val obs = response.optString("obs", "Error desconocido")
                                Log.d("LoginActivity", "Error al iniciar sesión: $obs")
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Error al iniciar sesión: $obs",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: JSONException) {
                            Log.e("LoginActivity", "Error al parsear JSON: $e")
                            Toast.makeText(
                                this@LoginActivity,
                                "Error al parsear JSON",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.e("LoginActivity", "Error de red: $error")
                        Toast.makeText(
                            this@LoginActivity,
                            "Error de red: $error",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                )

                val requestQueue = Volley.newRequestQueue(this@LoginActivity)
                requestQueue.add(jsonObjectRequest)
            }
        }






        val textViewOlvidarClave = findViewById<TextView>(R.id.text_view_olvidar_clave)
        val texto = "Olvidé la Clave"
        val spannableString = SpannableString(texto)
        val colorVerde = ContextCompat.getColor(this, R.color.black)
        val colorSpan = ForegroundColorSpan(colorVerde)
        spannableString.setSpan(
            colorSpan,
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            UnderlineSpan(),
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textViewOlvidarClave.text = spannableString

        textViewOlvidarClave.setOnClickListener {
            Log.d("InicioSesionActivity", "Se hizo clic en 'Olvidé la Clave'")
            val intent = Intent(this, RecuperarClave::class.java)
            startActivity(intent)
        }

        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun generateRandomKey(): ByteArray {
        val random = SecureRandom()
        val key = ByteArray(32)
        random.nextBytes(key)
        return key
    }

    private fun generateRandomIV(): ByteArray {
        val random = SecureRandom()
        val iv = ByteArray(16) // Utilizar la misma longitud que en PHP (16 bytes)
        random.nextBytes(iv)
        Log.d("LoginActivity", "Generated IV: ${Base64.encodeToString(iv, Base64.DEFAULT)}")
        return iv
    }

    private fun encryptPassword(password: String): String {
        try {
            val key = "c10ddfd118bf3ae541fbab17328de27e" // 32-byte key (256 bits)
            val iv = generateRandomIV()

            // Log para mostrar el IV generado
            Log.d("LoginActivity2", "IV generado: ${Base64.encodeToString(iv, Base64.DEFAULT)}")

            val ivSpec = IvParameterSpec(iv)
            val secretKeySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)

            val encrypted = cipher.doFinal(password.toByteArray(StandardCharsets.UTF_8))
            val ivAndEncrypted = iv + encrypted

            // Log para mostrar la contraseña encriptada
            Log.d("LoginActivity3", "Contraseña encriptada: ${Base64.encodeToString(ivAndEncrypted, Base64.DEFAULT)}")

            return Base64.encodeToString(ivAndEncrypted, Base64.DEFAULT)
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }


    private fun decryptPassword(encryptedPassword: String): String {
        try {
            val key = "c10ddfd118bf3ae541fbab17328de27e" // 32-byte key (256 bits)
            val decodedBytes = Base64.decode(encryptedPassword, Base64.DEFAULT)
            val iv = decodedBytes.copyOfRange(0, 16)
            val encrypted = decodedBytes.copyOfRange(16, decodedBytes.size)

            val ivSpec = IvParameterSpec(iv)
            val secretKeySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)

            val decryptedBytes = cipher.doFinal(encrypted)

            return String(decryptedBytes, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

}
