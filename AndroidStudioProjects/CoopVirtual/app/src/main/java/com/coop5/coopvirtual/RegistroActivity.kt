package com.coop5.coopvirtual
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        val textViewInstructions = findViewById<TextView>(R.id.textViewInstructions)

        val texto = "REGISTRO DE USUARIO"
        val spannableString = SpannableString(texto)

        // Aplicar subrayado
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Cambiar color del texto
        spannableString.setSpan(ForegroundColorSpan(getColor(R.color.white)), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Cambiar el tamaño del texto
        spannableString.setSpan(RelativeSizeSpan(1.5f), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Cambiar el tipo de fuente y el estilo del texto
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Aplicar un sombreado al texto
        textViewInstructions.setShadowLayer(5f, 0f, 0f, getColor(R.color.black))

        textViewInstructions.text = spannableString
    }
}
