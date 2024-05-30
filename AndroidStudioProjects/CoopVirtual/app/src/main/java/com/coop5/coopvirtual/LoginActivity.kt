package com.coop5.coopvirtual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.content.SharedPreferences
import com.coop5.coopvirtual.Utilidades
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {


    private lateinit var loginButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        // Inicializa las vistas
        loginButton = findViewById(R.id.btn_ingresar)
        usernameEditText = findViewById(R.id.edit_text_username)
        passwordEditText = findViewById(R.id.edit_text_password)
        val token = Utilidades.getTokenFromSharedPreferences(this)
// Configura el listener del botón de inicio de sesión





        loginButton.setOnClickListener {
            // Obtiene los datos de usuario y contraseña
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Realiza la solicitud POST al backend

            val url = "https://api.coop5.com.ar:5003/api/oficina-virtual/login"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener<String> { response ->
                    Log.d(
                        "LoginActivity",
                        "Respuesta del servidor: $response"
                    ) // Registro de log para la respuesta del servidor

                    try {
                        val jsonResponse = JSONObject(response)
                        val status = jsonResponse.getInt("status")
                        val message = jsonResponse.getString("message")

                        if (status == 200) {
                            Log.d("LoginActivity", "Inicio de sesión exitoso: $message")

                            // Extrae el token del servidor
                            val body = jsonResponse.getJSONObject("body")
                            val tokenFromServer = body.getString("token")
                            val nombre = body.getString("nombre")
                            val apellido = body.getString("apellido")
                            // Guarda el token en SharedPreferences
                            Utilidades.saveTokenToSharedPreferences(
                                this@LoginActivity,
                                tokenFromServer
                            )
                            // Guarda el nombre en SharedPreferences si es necesario
                            Utilidades.saveNombreToSharedPreferences(
                                this@LoginActivity,
                                nombre
                            )
                            // Guarda el nombre en SharedPreferences si es necesario
                            Utilidades.saveApellidoToSharedPreferences(
                                this@LoginActivity,
                                apellido
                            )
                            // El inicio de sesión fue exitoso, muestra un mensaje de sesión iniciada
                            Toast.makeText(
                                this@LoginActivity,
                                "¡Sesión iniciada!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this, MainSesionActivity::class.java)
                            intent.putExtra("USERNAME", username)
                            intent.putExtra("nombre",nombre)
                            intent.putExtra("apellido",apellido)
                            startActivity(intent)

                            // Finaliza la actividad actual si deseas que la actividad de inicio de sesión se cierre al iniciar la actividad principal
                            finish()


                        } else {
                            Log.d("LoginActivity", "Error al iniciar sesión: $message")
                            // El inicio de sesión falló, muestra un mensaje de error
                            Toast.makeText(
                                this@LoginActivity,
                                "Error al iniciar sesión: $message",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Log.e("LoginActivity", "Error al parsear JSON: $e")
                    }
                },
                Response.ErrorListener { error ->
                    // Maneja errores de la solicitud
                    Log.e("LoginActivity", "Error de red: $error")
                    Toast.makeText(this@LoginActivity, "Error de red: $error", Toast.LENGTH_SHORT)
                        .show()
                }
            ) {
                // Sobrescribe el método getParams para enviar los datos en el cuerpo de la solicitud
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["dniCuit"] = username
                    params["password"] = password
                    Log.d(
                        "LoginActivity",
                        "Datos enviados al servidor: $params"
                    ) // Registro de log para los datos enviados al servidor
                    return params
                }
            }

            // Agrega la solicitud a la cola de solicitudes de Volley

            val requestQueue = Volley.newRequestQueue(this@LoginActivity)
            requestQueue.add(stringRequest)



        }
        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener {
            // Abre la actividad de registro cuando se hace clic en el botón de registro
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }


        private fun saveTokenToSharedPreferences(token: String) {
          val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
          val editor = sharedPreferences.edit()
          editor.putString("token", token)
          editor.apply()

      }

}











