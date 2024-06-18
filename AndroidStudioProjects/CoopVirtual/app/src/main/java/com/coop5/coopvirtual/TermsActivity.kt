package com.coop5.coopvirtual

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
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
        btnAceptar.setOnClickListener {
            Log.d("TermsActivity", "Aceptar button clicked. Setting result and finishing activity.")
            val intent = Intent()
            intent.putExtra("checkbox_state", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        val btnNoAceptar = findViewById<Button>(R.id.btnNoAceptar)

        btnNoAceptar.setOnClickListener {
            mostrarDialogo()
        }
    }


    private fun mostrarDialogo() {
        val opciones = arrayOf("Seguir como invitado", "Aceptar Términos")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Qué desea hacer?")
            .setItems(opciones) { dialogInterface: DialogInterface, i: Int ->
                when (i) {
                    0 -> {
                        // Acción para seguir como invitado
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    1 -> {
                        // Acción para volver a la actividad de términos
                        finish()
                    }
                }
            }
        builder.create().show()
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
