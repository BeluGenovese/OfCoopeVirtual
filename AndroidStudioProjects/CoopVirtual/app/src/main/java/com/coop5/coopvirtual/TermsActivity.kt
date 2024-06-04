package com.coop5.coopvirtual

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast


class TermsActivity : AppCompatActivity() {

    private val TAG = "TermsActivity"
    private val REQUEST_REGISTRATION = 101 // Código de solicitud para la actividad de registro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        val btnAceptar = findViewById<Button>(R.id.btnAceptar)
        val btnNoAceptar = findViewById<Button>(R.id.btnNoAceptar)

        btnAceptar.setOnClickListener {
            Log.d("TermsActivity", "Aceptar button clicked. Setting result and finishing activity.")
            val intent = Intent()
            intent.putExtra("checkbox_state", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }


        btnNoAceptar.setOnClickListener {
            Log.d(TAG, "No Aceptar button clicked.")
            // Mostrar mensaje de que debe aceptar los términos y condiciones
            Toast.makeText(this, "Para continuar, debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_REGISTRATION && resultCode == Activity.RESULT_OK) {
            // El usuario ha completado el registro, finalizar esta actividad
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
