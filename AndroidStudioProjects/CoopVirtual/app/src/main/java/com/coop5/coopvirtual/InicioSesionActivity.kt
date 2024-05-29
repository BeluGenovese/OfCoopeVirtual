package com.coop5.coopvirtual

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class InicioSesionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)


        val textViewOlvidarClave = findViewById<TextView>(R.id.text_view_olvidar_clave)

        val texto = "Olvidé la Clave"

        val spannableString = SpannableString(texto)

        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        spannableString.setSpan(ForegroundColorSpan(getColor(R.color.black)), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textViewOlvidarClave.text = spannableString

        // Establecer el MovementMethod para activar los enlaces
        textViewOlvidarClave.movementMethod = LinkMovementMethod.getInstance()
    }
}
