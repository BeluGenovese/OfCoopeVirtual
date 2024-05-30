package com.coop5.coopvirtual

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InicioSesionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        // Configuración del texto "Olvidé la Clave"
        val textViewOlvidarClave = findViewById<TextView>(R.id.text_view_olvidar_clave)
        val texto = "Olvidé la Clave"
        val spannableString = SpannableString(texto)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(getColor(R.color.black)), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textViewOlvidarClave.text = spannableString
        textViewOlvidarClave.movementMethod = LinkMovementMethod.getInstance()

        // Configuración del botón REGISTRARSE
        val btnRegistrarse = findViewById<Button>(R.id.btn_register)
        btnRegistrarse.setOnClickListener {
            // Agregar un log
            Log.d("InicioSesionActivity", "Se hizo clic en el botón REGISTRARSE")

            // Inicia la actividad de registro
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
