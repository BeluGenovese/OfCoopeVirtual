package com.coop5.coopvirtual
import android.content.Context
import android.app.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.style.ClickableSpan
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.coop5.coopvirtual.ValidacionesUtils
import com.coop5.coopvirtual.AuthManager
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.HashMap
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class RegistroActivity : AppCompatActivity() {


    private val TAG = "RegistroActivity"

    private val urlTerminosCondiciones = "https://m.coop5.com.ar/politicasAppReclamos.php"
    private lateinit var authManager: AuthManager
    private lateinit var editTextName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnRegistrarse: Button
    private lateinit var editTextMail: EditText
    private lateinit var checkBoxTerms: CheckBox
    lateinit var botonRegistrar: Button


    // Declaración de variables de instancia
    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var email: String





    companion object {
        private const val REQUEST_TERMS = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)


        val textViewInstructions = findViewById<TextView>(R.id.textViewInstructions)
        val editTextPassword2 = findViewById<EditText>(R.id.editTextPassword2)
        // Inicializar las vistas necesarias una sola vez

        editTextName = findViewById(R.id.editTextName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextMail = findViewById(R.id.editTextEmail)
        checkBoxTerms = findViewById(R.id.checkBoxTerms)
        btnRegistrarse = findViewById(R.id.btn_registrarse)
        editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        authManager = AuthManager(this)

        // Asignación única para editTextLastName
        val btnRegistrarse = findViewById<Button>(R.id.btn_registrarse)
        // Asignaciones de findViewById



        val texto = "REGISTRO DE USUARIO"
        val spannableString = SpannableString(texto)


        // Aplicar subrayado
        spannableString.setSpan(
            UnderlineSpan(),
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Cambiar color del texto
        spannableString.setSpan(
            ForegroundColorSpan(getColor(com.coop5.coopvirtual.R.color.coop4)),
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Cambiar el tamaño del texto
        spannableString.setSpan(
            RelativeSizeSpan(1.2f),
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Cambiar el tipo de fuente y el estilo del texto
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Aplicar un sombreado al texto
        textViewInstructions.setShadowLayer(
            5f,
            0f,
            0f,
            getColor(com.coop5.coopvirtual.R.color.black)
        )

        textViewInstructions.text = spannableString

        // Crear un SpannableString para el texto del CheckBox


        checkBoxTerms = findViewById(R.id.checkBoxTerms)
        // Crear un SpannableString para el texto del CheckBox

        val isChecked = checkBoxTerms.isChecked

        val termsString = getString(R.string.terms_and_conditions)
        val termsSpannable = SpannableString(termsString)
        checkBoxTerms.setOnClickListener {
            val intent = Intent(this, TermsActivity::class.java)
            startActivityForResult(intent, REQUEST_TERMS)

        }

        val editTextName = findViewById<EditText>(R.id.editTextName)


        // Manejar cambios en el campo de nombre
        editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString().trim()
                if (!ValidacionesUtils().validarNombre(name)) {
                    editTextName.error = "Nombre inválido"
                } else {
                    editTextName.error = null
                }
            }
        })

        editTextLastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lastName = s.toString().trim()
                Log.d(TAG, "Apellido ingresado: $lastName")
                if (!ValidacionesUtils().validarApellido(lastName)) {
                    Log.d(TAG, "Apellido inválido")
                    editTextLastName.error = "Apellido inválido"
                } else {
                    Log.d(TAG, "Apellido válido")
                    editTextLastName.error = null
                }
            }
        })


// Manejar cambios en el campo de contraseña
        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString().trim()
                if (!ValidacionesUtils().validarPassword(password)) {
                    editTextPassword.error = "Contraseña inválida"
                } else {
                    editTextPassword.error = null
                }
            }
        })


        val btnShowPassword2 = findViewById<ImageButton>(R.id.btnShowPassword2)
        var passwordVisible = false


        btnShowPassword2.setOnClickListener {
            // Alternar entre mostrar u ocultar la contraseña
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                // Mostrar contraseña
                editTextPassword2.transformationMethod = null
                btnShowPassword2.setImageResource(com.google.android.material.R.drawable.design_ic_visibility) // Cambiar el icono a "mostrar contraseña"
                btnShowPassword2.setColorFilter(Color.BLACK) // Cambiar el color del ícono a negro
            } else {
                // Ocultar contraseña
                editTextPassword2.transformationMethod = PasswordTransformationMethod.getInstance()
                btnShowPassword2.setImageResource(com.google.android.material.R.drawable.design_ic_visibility_off) // Cambiar el icono a "ocultar contraseña"
                btnShowPassword2.setColorFilter(Color.BLACK) // Cambiar el color del ícono a negro
            }

            // Mover el cursor al final del texto
            editTextPassword2.setSelection(editTextPassword2.text.length)
        }


        val textViewCuenta: TextView = findViewById(R.id.editTextCuenta)

        textViewCuenta.setOnClickListener {
            val intent = Intent(this, InicioSesionActivity::class.java)
            startActivity(intent)
        }


        val btnShowPassword = findViewById<ImageButton>(R.id.btnShowPassword)


// Manejar clic en el botón para mostrar u ocultar la contraseña
        btnShowPassword.setOnClickListener {
            // Alternar entre mostrar u ocultar la contraseña
            passwordVisible = !passwordVisible

            if (passwordVisible) {

                // Mostrar contraseña
                editTextPassword.transformationMethod = null
                btnShowPassword.setImageResource(com.google.android.material.R.drawable.design_ic_visibility) // Cambiar el icono a "mostrar contraseña"
                btnShowPassword.setColorFilter(Color.BLACK) // Cambiar el color del ícono a negro
            } else {
                // Ocultar contraseña
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnShowPassword.setImageResource(com.google.android.material.R.drawable.design_ic_visibility_off) // Cambiar el icono a "ocultar contraseña"
                btnShowPassword.setColorFilter(Color.BLACK) // Cambiar el color del ícono a negro
            }

            // Mover el cursor al final del texto
            editTextPassword.setSelection(editTextPassword.text.length)
        }


        // Subrayar el texto
        textViewInstructions.paintFlags =
            textViewInstructions.paintFlags or Paint.UNDERLINE_TEXT_FLAG



        btnRegistrarse.setOnClickListener {
            val name = editTextName.text.toString().trim()

            val lastName = editTextLastName.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            Log.d(
                "RegistroActivityLogs",
                "Iniciando registro con nombre: $name, apellido: $lastName y contraseña: $password"
            )

            // Validar nombre, apellido y contraseña
            val esNombreValido = ValidacionesUtils().validarNombre(name)
            val esApellidoValido = ValidacionesUtils().validarApellido(lastName)
            val esPasswordValido = ValidacionesUtils().validarPassword(password)



            val correo = editTextMail.text.toString().trim()


            // Mostrar errores si los campos no son válidos
            if (!esNombreValido) {
                editTextName.error = "Nombre inválido"
                Log.d("RegistroActivityLogs", "Nombre inválido: $name")
                return@setOnClickListener  // Salir del listener si el nombre no es válido
            } else {
                editTextName.error = null
                Log.d("RegistroActivityLogs", "Nombre válido: $name")
            }

            if (!esApellidoValido) {
                editTextLastName.error = "Apellido inválido"
                Log.d("RegistroActivityLogs", "Apellido inválido: $lastName")
                return@setOnClickListener  // Salir del listener si el apellido no es válido
            } else {
                editTextLastName.error = null
                Log.d("RegistroActivityLogs", "Apellido válido: $lastName")
            }

            if (!esPasswordValido) {
                editTextPassword.error = "Contraseña inválida"
                Log.d("RegistroActivityLogs", "Contraseña inválida")
                return@setOnClickListener  // Salir del listener si la contraseña no es válida
            } else {
                editTextPassword.error = null
                Log.d("RegistroActivityLogs", "Contraseña válida")
            }

            // Si todos los campos son válidos, continuar con el registro
            if (esNombreValido && esApellidoValido && esPasswordValido) {
                Log.d("RegistroCuenta", "Correo ingresado: $correo")
                // Inicia sesión en el backend
                loginApp(correo, this)
            } else {
                Log.d("RegistroCuenta1", "Correo inválido: $correo")
                Toast.makeText(
                    this,
                    "Por favor, ingrese una dirección de correo válida.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loginApp(correo: String, context: Context) {

        Log.d("RegistroApp", "Iniciando sesión en el backend para el correo: $correo")
        val url = "http://back.coop5.com.ar:9502/sessionU/open" // Utilizar HTTPS en lugar de HTTP
        val password = "£j4-G&30E%O0ZtG[£%O}YI'yU+VtL7'" // Contraseña en texto plano

        // Encriptar la contraseña utilizando tu lógica de encriptación
        val encryptedPassword = encryptPassword(password)

        Log.d(
            "RegistroApp1",
            "Contraseña encriptada: $encryptedPassword"
        ) // Agregar log de contraseña encriptada

        // Crear el objeto JSON con el correo y la contraseña encriptada
        val jsonBody = JSONObject().apply {
            put("username", "appCoop") // Usar el correo de la aplicación
            put("password", encryptedPassword) // Usar la contraseña encriptada
        }

        Log.d(
            "RegistroApp2",
            "Cuerpo de la solicitud JSON: $jsonBody"
        ) // Agregar log del cuerpo de la solicitud JSON

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            Response.Listener { response ->
                try {
                    val status = response.getInt("status")
                    Log.d(
                        "RegistroApp4",
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
                        // Obtener los datos para enviar al registro
                        val nombre = editTextName.text.toString()
                        val apellido = editTextLastName.text.toString()
                        val email = editTextMail.text.toString()
                        val password1 = editTextPassword.text.toString() // Capturar la contraseña ingresada
                        // Llamar a enviarRegistroCuenta con los datos del usuario
                        enviarRegistroCuenta(nombre, apellido, email, password1, this, idSession, encryptedSession)
                    }

                    else {
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

    // Método para manejar el resultado de la actividad TermsActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TERMS && resultCode == Activity.RESULT_OK) {
            // Obtener el estado del CheckBox del resultado
            val checkboxState = data?.getBooleanExtra("checkbox_state", false) ?: false
            // Establecer el estado del CheckBox
            checkBoxTerms.isChecked = checkboxState
        }
    }

    private fun enviarRegistroCuenta(
        nombre: String,
        apellido: String,
        email: String,
        password1: String,
        context: Context,
        idSesion: Int,
        session: String
    ) {
        Log.d("RegistroCuenta1", "Iniciando el proceso de registro de cuenta.")
        Log.d("RegistroCuenta2", "Correo: $email")
        Log.d("RegistroCuenta3", "ID de Sesión: $idSesion")
        Log.d("RegistroCuenta4", "Sesión original: $session")

        val url = "http://back.coop5.com.ar:9502/cuenta/add/$idSesion" // Backend endpoint URL

        val jsonBody = JSONObject().apply {
            put("nombre", nombre)  // Add user-provided name to JSON
            put("apellido", apellido)  // Add user-provided last name to JSON
            put("email", email)  // Add user-provided email to JSON
            put("password", password1)  // Add user-provided password to JSON
        }

        Log.d("RegistroCuenta5", "Cuerpo de la solicitud JSON: $jsonBody")

        val cleanSession = session.replace("\n", "") // Remove newline characters from session
        Log.d("RegistroCuenta6", "Sesión limpia: $cleanSession")

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonBody,
            Response.Listener { response ->
                try {
                    Log.d("RegistroCuenta7", "Respuesta del servidor: $response")

                    val status = response.getInt("status")

                    if (status == 0) {
                        val obs = response.optString("obs", "Solicitud enviada")
                        Log.d("RegistroCuenta8", "Cuenta creada exitosamente: $obs")
                        Toast.makeText(context, obs, Toast.LENGTH_LONG).show()

                        // Create a Handler to delay showing the second message
                        val handler = Handler()
                        handler.postDelayed({
                            // Show the second message after a short delay
                            Toast.makeText(
                                context,
                                "Revise su correo electrónico para continuar",
                                Toast.LENGTH_LONG
                            ).show()

                            // Clear the email EditText field
                            val editTextCorreo: EditText = findViewById(R.id.editTextCorreo)
                            editTextCorreo.text.clear()

                            // Start the login activity
                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(intent)
                            Log.d("RegistroCuenta9", "Actividad de inicio de sesión iniciada.")
                            finish()
                        }, 1000)
                    } else {
                        val obs = response.optString("obs", "Error desconocido")
                        Log.d("RegistroCuenta10", "Error al crear la cuenta: $obs")
                        Toast.makeText(
                            context,
                            "Error al crear la cuenta: $obs",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    Log.e("RegistroCuenta11", "Error al parsear JSON: $e")
                    Toast.makeText(
                        context,
                        "Error al crear la cuenta",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("RegistroCuenta12", "Error de red: $error")
                Toast.makeText(context, "Error de red: $error", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["s"] = cleanSession // Add session to headers
                Log.d("RegistroCuenta13", "Encabezados de la solicitud: $headers")
                return headers
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsonObjectRequest)
    }

}