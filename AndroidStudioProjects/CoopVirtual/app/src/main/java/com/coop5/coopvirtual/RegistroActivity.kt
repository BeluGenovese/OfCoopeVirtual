package com.coop5.coopvirtual

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
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
import android.util.Log
import android.widget.CheckBox

class RegistroActivity : AppCompatActivity() {

    private val TAG = "RegistroActivity"
    private lateinit var checkBoxTerms: CheckBox
    private val urlTerminosCondiciones = "https://m.coop5.com.ar/politicasAppReclamos.php"

    companion object {
        private const val REQUEST_TERMS = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        val textViewInstructions = findViewById<TextView>(R.id.textViewInstructions)

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
            ForegroundColorSpan(getColor(com.coop5.coopvirtual.R.color.white)),
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
        val termsString = getString(R.string.terms_and_conditions)
        val termsSpannable = SpannableString(termsString)
        checkBoxTerms.setOnClickListener {
            val intent = Intent(this, TermsActivity::class.java)
            startActivityForResult(intent, REQUEST_TERMS)

        }


        val editTextPassword2 = findViewById<EditText>(R.id.editTextPassword2)
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


        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
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




}








