package com.coop5.coopvirtual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            // Guarda que la aplicación ya no es la primera vez que se ejecuta
            with(sharedPreferences.edit()) {
                putBoolean("isFirstRun", false)
                apply()
            }

            // Lanza MainActivity si es la primera vez que se ejecuta la aplicación después de cerrarse
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Cierra la aplicación si ya no es la primera vez que se ejecuta después de cerrarse
            finish()
        }
    }
}
