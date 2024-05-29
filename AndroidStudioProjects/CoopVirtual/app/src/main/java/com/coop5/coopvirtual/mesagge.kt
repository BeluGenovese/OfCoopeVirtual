package com.coop5.coopvirtual

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class mensaje : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.mensajes)
        // Encuentra el LinearLayout que contiene el mensaje y los botones
        val layoutContainer = findViewById<LinearLayout>(R.id.layout_container)

        // Cargar la animación de desvanecimiento desde el archivo XML
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_anim)

        // Aplicar la animación al contenedor del mensaje
        layoutContainer.startAnimation(fadeInAnimation)

        // Encuentra la vista del mensaje
        val messageTextView = findViewById<TextView>(R.id.text_message)

        // Agrega sombra al texto
        messageTextView.setShadowLayer(10f, 0f, 0f, getColor(R.color.shadow_color))

        // Aplica una elevación al contenedor del mensaje para que la sombra sea visible
        layoutContainer.elevation = 10f
    }
}





