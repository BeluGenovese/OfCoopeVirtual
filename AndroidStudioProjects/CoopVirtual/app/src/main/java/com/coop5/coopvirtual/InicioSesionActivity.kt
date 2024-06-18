package com.coop5.coopvirtual


import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class InicioSesionActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        // Configuración del texto "Olvidé la Clave"
        val textViewOlvidarClave = findViewById<TextView>(R.id.text_view_olvidar_clave)
        val texto = "Olvidé la Clave"

        // Crear una instancia de SpannableString
        val spannableString = SpannableString(texto)

        // Aplicar el color verde al texto
        val colorVerde = ContextCompat.getColor(this, R.color.black)
        val colorSpan = ForegroundColorSpan(colorVerde)
        spannableString.setSpan(
            colorSpan,
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Aplicar el estilo subrayado al texto
        val subrayado = UnderlineSpan()
        spannableString.setSpan(
            subrayado,
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Establecer el texto modificado en el TextView
        textViewOlvidarClave.text = spannableString

        // Añade un OnClickListener directamente al TextView
        textViewOlvidarClave.setOnClickListener {
            Log.d("InicioSesionActivity", "Se hizo clic en 'Olvidé la Clave'")
            val intent = Intent(this, RecuperarClave::class.java)
            startActivity(intent)
        }

        // Configuración del botón REGISTRARSE
        val btnRegistrarse = findViewById<Button>(R.id.btn_register)
        btnRegistrarse.setOnClickListener {
            Log.d("InicioSesionActivity", "Se hizo clic en el botón REGISTRARSE")
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // Configuración del TextView "Ingresar Invitado"
        val textViewIngresarInvitado = findViewById<TextView>(R.id.text_view_ingresar_invitado)
        val textoCompleto = textViewIngresarInvitado.text.toString()
        val spannableString2 = SpannableString(textoCompleto)
        val colorVerdePersonalizado = ContextCompat.getColor(this, R.color.coope)
        val colorSpan2 = ForegroundColorSpan(colorVerdePersonalizado)
        val inicioTextoVerde = textoCompleto.indexOf("invitado")

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

        // Añade un OnClickListener al TextView "Ingresar Invitado"
        textViewIngresarInvitado.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
