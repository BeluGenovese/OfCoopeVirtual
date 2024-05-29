package com.coop5.coopvirtual


import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.MetricAffectingSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject


class MainSesionActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var viewPager: ViewPager // Agrega esta variable
    private val REQUEST_INTERNET_PERMISSION = 101
    private val FACEBOOK_REQUEST_CODE = 103 // Example value
    private val INSTAGRAM_REQUEST_CODE = 102 // Example value
    private val REQUEST_CODE_INTERNET = 104


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sesion)


        val nombreTextView: TextView = findViewById(R.id.btn_iniciar_sesion2)
        val apellidoTextView: TextView = findViewById(R.id.text_view_apellido)
        val frameLayout: FrameLayout = findViewById(R.id.frameLayout)
        val username = intent.getStringExtra("USERNAME")
        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")

// Obtener las iniciales del nombre y apellido
        val inicialNombre = nombre?.take(1) ?: ""
        val inicialApellido = apellido?.take(1) ?: ""

// Crear un TextView para mostrar las iniciales dentro del FrameLayout
        val textViewIniciales = TextView(this)
        textViewIniciales.text = "$inicialNombre$inicialApellido"
        textViewIniciales.setTextColor(Color.BLACK)
        textViewIniciales.gravity = Gravity.CENTER
        textViewIniciales.textSize = 18f

// Agregar el TextView al FrameLayout
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.CENTER
        textViewIniciales.layoutParams = layoutParams
        frameLayout.addView(textViewIniciales)

// Mostrar el nombre y apellido en los TextViews correspondientes
        nombreTextView.text = nombre
        apellidoTextView.text = apellido
        val facebookIcon = findViewById<ImageView>(R.id.facebook_icon2)
        facebookIcon.setOnClickListener {


            // Check internet permission once
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.INTERNET
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.INTERNET),
                    FACEBOOK_REQUEST_CODE
                )
                Toast.makeText(
                    this,
                    "Please grant internet permission to proceed.",
                    Toast.LENGTH_SHORT
                ).show()
            } else { // Permission granted, proceed

                val facebookIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/CanalCoopp")
                ) // Use web link

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(facebookIntent)
                } else {
                    // Handle the case where the intent cannot be resolved (e.g., show error message)
                }
            }
        }

        val instagramIcon = findViewById<ImageView>(R.id.ig_icon2)


// Instagram icon click listener
        instagramIcon.setOnClickListener {


            val instagramIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.instagram.com/CanalCoop")
            ) // Use web link

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(instagramIntent)
            } else {
                // Handle the case where the intent cannot be resolved (e.g., show error message)
            }
        }
        val reclamoText = findViewById<TextView>(R.id.tel_text2)
        reclamoText.setOnClickListener {
            onCallButtonClicked()
        }

        val phoneIcon = findViewById<ImageView>(R.id.tele_icon2)
        phoneIcon.setOnClickListener {
            onCallButtonClicked()
        }


        val miBoton = findViewById<Button>(R.id.mi_boton2)
        miBoton.setOnClickListener {

            val url = "https://www.coop5.com.ar" // URL de la página web
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)

        }


        val textViewApellido = findViewById<TextView>(R.id.text_view_apellido)
        val textViewIniciarSesion = findViewById<TextView>(R.id.btn_iniciar_sesion2)

        textViewIniciarSesion.text = nombre
        textViewApellido.text = apellido
        textViewApellido.paintFlags = textViewApellido.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textViewIniciarSesion.paintFlags =
            textViewIniciarSesion.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        Log.d("MainSesionActivity", "Nombre de usuario enviado: $nombre")


        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            val title = "Coope Virtual"
            val spannableTitle = SpannableString(title)
            val fontSize = resources.getDimensionPixelSize(R.dimen.custom_title_size)
            val typeface = ResourcesCompat.getFont(this@MainSesionActivity, R.font.myfont)
            spannableTitle.setSpan(
                AbsoluteSizeSpan(fontSize),
                0,
                spannableTitle.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableTitle.setSpan(
                CustomTypefaceSpan(typeface),
                0,
                spannableTitle.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableTitle.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this@MainSesionActivity,
                        R.color.black
                    )
                ), 0, spannableTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableTitle.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                spannableTitle.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setTitle(spannableTitle)

            // Habilitar la visualización del título predeterminado
            setDisplayShowTitleEnabled(true)
        }

        drawerLayout = findViewById(R.id.drawer_layout2)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )


        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // Establecer el icono del menú en lugar del botón de retroceso
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_24)


        val navView: NavigationView = findViewById(R.id.nav_view2)

        navView.setNavigationItemSelectedListener { menuItem ->
            // Manejar los eventos de clic en los elementos del menú aquí
            true
        }


        //    val bannerContainer: FrameLayout = findViewById(R.id.banner_container)
        val viewPager: ViewPager = findViewById(R.id.viewPager2)
        val width = viewPager.width
        Log.d("Ancho ViewPager", "El ancho del ViewPager es: $width píxeles")

// Agregar fragmentos al adaptador del ViewPager
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ImageFragment.newInstance(R.drawable.sensa_background))
        adapter.addFragment(ImageFragment.newInstance(R.drawable.bannerweb))
        adapter.addFragment(ImageFragment.newInstance(R.drawable.pagar))
        adapter.addFragment(ImageFragment.newInstance(R.drawable.trabajo))

// Establecer adaptador en el ViewPager
        viewPager.adapter = adapter
        adapter.notifyDataSetChanged() // Notificar al adaptador después de agregar todos los fragmentos
        // Configurar temporizador para cambiar las imágenes automáticamente
        // Configurar temporizador para cambiar las imágenes automáticamente
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager.currentItem
                val totalItems = adapter.count

                if (totalItems > 0) {
                    val nextItem = (currentItem + 1) % totalItems
                    viewPager.setCurrentItem(nextItem, true)
                }

                // Volver a programar el temporizador para ejecutar este Runnable después de 3000 ms
                handler.postDelayed(this, 10000) // Cambiar de imagen cada 3 segundos
            }
        }


        // Iniciar el temporizador después de un breve retraso
        handler.postDelayed(runnable, 8000)

        val btnChatBot = findViewById<ImageButton>(R.id.btn_chat_bot2)
        btnChatBot.setOnClickListener {
            val phoneNumber = "5493525610800" // Reemplaza con el número de teléfono deseado

            // Crear un intent con la acción ACTION_SEND para abrir WhatsApp
            val intent = Intent(Intent.ACTION_SEND)

            // Establecer el paquete de WhatsApp para garantizar que se abra en WhatsApp
            intent.setPackage("com.whatsapp")

            // Establecer el tipo de contenido del intent como texto plano
            intent.type = "text/plain"

            // Agregar el número de teléfono al intent como destinatario (este es opcional)
            intent.putExtra("jid", phoneNumber + "@s.whatsapp.net")

            // Agregar el mensaje predeterminado al intent (esto también es opcional)
            intent.putExtra(Intent.EXTRA_TEXT, "Hola, Quiero hacer una consulta.")

            try {
                // Iniciar la actividad con el intent
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // WhatsApp no está instalado, mostrar un mensaje de error o tomar otra acción
                Toast.makeText(
                    this,
                    "WhatsApp no está instalado en este dispositivo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Obtener referencia a los ImageButtons
        val btnEnergia2 = findViewById<ImageButton>(R.id.btn_Energia2)
        val btnAgua2 = findViewById<ImageButton>(R.id.btn_Agua2)
        val btnBanco2 = findViewById<ImageButton>(R.id.btn_Banco2)
        val btnApross2 = findViewById<ImageButton>(R.id.btn_Apross2)

        // Cambiar el color de fondo de los ImageButtons después de
        // iniciar sesión con éxito
        val nuevoColorFondo = ContextCompat.getColor(this, R.color.verdeWhat)
        btnEnergia2.setBackgroundColor(nuevoColorFondo)
        btnAgua2.setBackgroundColor(nuevoColorFondo)
        btnBanco2.setBackgroundColor(nuevoColorFondo)
        btnApross2.setBackgroundColor(nuevoColorFondo)


        val btnInternet2 = findViewById<ImageButton>(R.id.btn_Internet2)
        val btnTvDigital2 = findViewById<ImageButton>(R.id.btn_TvDigital2)
        val btnTelefono2 = findViewById<ImageButton>(R.id.btn_Telefono2)
        val btnMovil2 = findViewById<ImageButton>(R.id.btn_Movil2)

        btnInternet2.setBackgroundColor(nuevoColorFondo)
        btnTvDigital2.setBackgroundColor(nuevoColorFondo)
        btnTelefono2.setBackgroundColor(nuevoColorFondo)
        btnMovil2.setBackgroundColor(nuevoColorFondo)


        val btnTramites: Button = findViewById(R.id.btn_tramites2)
        btnTramites.setOnClickListener {
            // Crear un Intent para abrir la pantalla de trámites
            val intent = Intent(this@MainSesionActivity, TramitesActivity::class.java)

            // Iniciar la actividad de trámites
            startActivity(intent)

        }

        val btnVencimientos2: Button = findViewById(R.id.btn_VencimientosCortes2)
        btnVencimientos2.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val url = "https://api.coop5.com.ar:5003/api/oficina-virtual/info-general/vencimientos"
            val request = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    val result = StringBuilder()
                    try {
                        val jsonArray = response.getJSONArray("body")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val dsServicio = jsonObject.getString("DsServicio")
                            val dsPeriodo = jsonObject.getString("DsPeriodo")
                            val fechaVto1 = jsonObject.getString("FechaVto1")
                            val fechaVto2 = jsonObject.getString("FechaVto2")
                            val fechaVto3 = jsonObject.getString("FechaVto3")

                            result.append("Servicio: $dsServicio\n")
                            result.append("Periodo: $dsPeriodo\n")
                            result.append("Fecha Vencimiento 1: $fechaVto1\n")
                            result.append("Fecha Vencimiento 2: $fechaVto2\n")
                            result.append("Fecha Vencimiento 3: $fechaVto3\n\n")
                        }

                        val intent = Intent(this, ActivityVencimientos::class.java)
                        intent.putExtra("datos", result.toString())
                        intent.putExtra("USERNAME", username)
                        intent.putExtra("nombre", nombre)
                        intent.putExtra("apellido", apellido)
                        startActivity(intent)


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
            queue.add(request)
        }
    }

    private fun showPopup() {
        // Verifica si el usuario ha iniciado sesión
        if (!usuarioHaIniciadoSesion()) {
            val text = "PARA ACCEDER ES NECESARIO INICIAR SESIÓN."
            val iniciarSesionIndex = text.indexOf("INICIAR SESIÓN")
            val accederIndex = text.indexOf("ACCEDER")

            // Longitudes de las palabras clave
            val iniciarSesionLength = "INICIAR SESIÓN".length
            val accederLength = "ACCEDER".length

            val spannableString = SpannableString(text)

            // Aplicar estilo a "INICIAR SESIÓN"
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.coop3)),
                iniciarSesionIndex,
                iniciarSesionIndex + iniciarSesionLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                iniciarSesionIndex,
                iniciarSesionIndex + iniciarSesionLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Aplicar estilo a "ACCEDER"
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.coop3)),
                accederIndex,
                accederIndex + accederLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                accederIndex,
                accederIndex + accederLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Inflar el diseño XML del mensaje
            val inflater = LayoutInflater.from(this)
            val dialogView = inflater.inflate(R.layout.mensajes, null)

            // Configurar el texto del TextView dentro del diálogo con el texto spanneado
            val textView = dialogView.findViewById<TextView>(R.id.text_message)
            textView.text = spannableString

// Agregar sombra al texto
            textView.setShadowLayer(
                10f, // Radio de desenfoque
                0f,  // Desplazamiento X
                0f,  // Desplazamiento Y
                Color.GREEN // Color de la sombra
            )

            // Construir el diálogo emergente con el estilo personalizado
            val dialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setView(dialogView)
                .create()

            // Mostrar el diálogo emergente
            dialog.show()

            // Configurar el clic del botón "Aceptar"
            val btnAceptar = dialogView.findViewById<Button>(R.id.btnAceptar)
            btnAceptar.setOnClickListener {
                // Aquí inicias la actividad para iniciar sesión
                val intent = Intent(this@MainSesionActivity, InicioSesionActivity::class.java)
                startActivity(intent)
                dialog.dismiss() // Cierra el diálogo después de iniciar sesión
            }

        }
    }

    private fun usuarioHaIniciadoSesion(): Boolean {
        // Aquí implementa la lógica para verificar si el usuario ha iniciado sesión
        // Devuelve true si el usuario ha iniciado sesión, de lo contrario, devuelve false
        return false // Cambia esto según la lógica de tu aplicación
    }


    private fun onCallButtonClicked() {
        // Request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION_CODE
            )
        } else {
            // Permission already granted. Make the call.
            makePhoneCall()
        }
    }


    private companion object {
        const val REQUEST_CALL_PERMISSION_CODE = 123
    }


    private fun makePhoneCall() {
        // Construct the phone number with the desired prefix and actual number
        val phoneNumber = "3525466201"

        // Use Uri.parse with "tel:" scheme to create a phone number URI
        val phoneUri = Uri.parse("tel:$phoneNumber")

        // Create an Intent with ACTION_CALL and the phone number URI
        val intent = Intent(Intent.ACTION_CALL, phoneUri)

        // Start the activity to initiate the call
        startActivity(intent)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted. Make the call.
                makePhoneCall()
            } else {
                // Permission denied. Inform the user (optional)
            }
        }
    }


    private class CustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {
        override fun updateDrawState(ds: TextPaint) {
            typeface?.let { ds.typeface = it }
        }

        override fun updateMeasureState(p: TextPaint) {
            typeface?.let { p.typeface = it }

        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {


            return true
        }
        return false // Devuelve false si el actionBarDrawerToggle no maneja el evento
    }

    // Función de extensión para mostrar un diálogo de confirmación
    fun AppCompatActivity.mostrarDialogoConfirmacion(mensaje: String, accionPositiva: () -> Unit) {
        AlertDialog.Builder(this)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, which ->
                accionPositiva()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cerrarSesion() {
        val token = Utilidades.getTokenFromSharedPreferences(this)
        if (token != null) {
            Log.d("MainSesionActivity", "Iniciando cierre de sesión con token: $token")

            val url = "https://api.coop5.com.ar:5003/api/oficina-virtual/logout"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener<String> { response ->
                    Log.d("MainSesionActivity", "Respuesta del servidor: $response")

                    // Mostrar mensaje de cierre de sesión
                    Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_LONG).show()

                    // Redirigir a la actividad principal inmediatamente
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                },
                Response.ErrorListener { error ->
                    Log.e("MainSesionActivity", "Error de red: ${error.message}")
                    Toast.makeText(
                        this,
                        "Error al cerrar sesión: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }

                override fun getBody(): ByteArray {
                    return byteArrayOf() // No se necesita un cuerpo en este caso
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        } else {
            Log.e("MainSesionActivity", "No se pudo obtener el token de SharedPreferences")
            Toast.makeText(this, "No se pudo obtener el token de sesión", Toast.LENGTH_LONG).show()
        }
    }


    private fun mostrarDialogoConfirmacion(mensaje: String, accionPositiva: () -> Unit) {
        AlertDialog.Builder(this)
            .setMessage(mensaje)

            .setPositiveButton("Aceptar") { dialog, which ->
                // Ejecutar la acción positiva
                accionPositiva()
                // Cerrar sesión
                cerrarSesion()


            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    override fun onBackPressed() {


        // Mostrar un cuadro de diálogo de confirmación
        mostrarDialogoConfirmacion("¿Estás seguro de que deseas salir?") {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val token = Utilidades.getTokenFromSharedPreferences(this)
                if (token != null) {
                    val actionView = item.actionView
                    actionView?.setBackgroundColor(ContextCompat.getColor(this, R.color.verde))
                    cerrarSesion()
                } else {
                    Log.e("MainSesionActivity", "No se pudo obtener el token de SharedPreferences")
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuItem = menu?.findItem(R.id.action_logout)
        menuItem?.setActionView(R.layout.menu_item_button)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)

        // Obtener el ítem del menú "Cerrar Sesión"
        val logoutItem = menu?.findItem(R.id.action_logout)
        // Aplicar el filtro de color al fondo del ítem del menú
        logoutItem?.actionView?.background?.setColorFilter(
            ContextCompat.getColor(this, R.color.verde),
            PorterDuff.Mode.SRC_IN
        )

        return true
    }
}


