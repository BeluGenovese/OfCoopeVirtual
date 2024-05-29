package com.coop5.coopvirtual

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.MetricAffectingSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
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
import com.google.android.material.navigation.NavigationView
import com.coop5.coopvirtual.BannerFragment
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.provider.Settings
import android.widget.ImageView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var viewPager: ViewPager // Agrega esta variable
    private val REQUEST_INTERNET_PERMISSION = 101
    private  val FACEBOOK_REQUEST_CODE = 103 // Example value
    private  val INSTAGRAM_REQUEST_CODE = 102 // Example value
    private val REQUEST_CODE_INTERNET = 104



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val facebookIcon = findViewById<ImageView>(R.id.facebook_icon)
        facebookIcon.setOnClickListener {

            // Check internet permission once
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), FACEBOOK_REQUEST_CODE)
                Toast.makeText(this, "Please grant internet permission to proceed.", Toast.LENGTH_SHORT).show()
            } else { // Permission granted, proceed

                val facebookIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/CanalCoopp")) // Use web link

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(facebookIntent)
                } else {
                    // Handle the case where the intent cannot be resolved (e.g., show error message)
                }
            }
        }


        val instagramIcon = findViewById<ImageView>(R.id.ig_icon)

// Instagram icon click listener
        instagramIcon.setOnClickListener {


            val instagramIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/CanalCoop")) // Use web link

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(instagramIntent)
            } else {
                // Handle the case where the intent cannot be resolved (e.g., show error message)
            }
        }
        val reclamoText = findViewById<TextView>(R.id.tel_text)
        reclamoText.setOnClickListener {
            onCallButtonClicked()
        }

        val phoneIcon = findViewById<ImageView>(R.id.tele_icon)
        phoneIcon.setOnClickListener {
            onCallButtonClicked()
        }


        val miBoton = findViewById<Button>(R.id.mi_boton)
        miBoton.setOnClickListener {

            val url = "https://www.coop5.com.ar" // URL de la página web
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)

        }
        val btnChatBot = findViewById<ImageButton>(R.id.btn_chat_bot)
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
                Toast.makeText(this, "WhatsApp no está instalado en este dispositivo", Toast.LENGTH_SHORT).show()
            }
        }

// Inflar la animación desde el archivo XML
        val fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_anim)

// Asignar la animación al ImageButton
        btnChatBot.startAnimation(fadeAnimation)

        val textViewChat: TextView = findViewById(R.id.text_view_chat)

// Inflar la animación desde el archivo XML
        val fadeAnimation2 = AnimationUtils.loadAnimation(this, R.anim.fade_anim)
        val color = ContextCompat.getColor(this, R.color.coope)

// Asignar la animación al TextView
        textViewChat.startAnimation(fadeAnimation2)
        textViewChat.setShadowLayer(10f, 0f, 0f, color)

// Encuentra una referencia al botón por su ID
        val btnInfo: Button = findViewById(R.id.btn_info)

// Asocia un listener de clic al botón
        btnInfo.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón
            showPopup()
        }
        textViewChat.setShadowLayer(10f, 0f, 0f, color)

// Encuentra una referencia al botón por su ID
        val btnVencimientos: Button = findViewById(R.id.btn_VencimientosCortes)

// Asocia un listener de clic al botón
        btnVencimientos.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón
            showPopup()
        }

        val btnTramites: Button = findViewById(R.id.btn_tramites)

// Asocia un listener de clic al botón
        btnTramites.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón
            showPopup()
        }


        val btnAgua: ImageButton = findViewById(R.id.btn_Agua)

// Asocia un listener de clic al botón de agua
        btnAgua.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón de agua
            showPopup()
        }

        val btnBanco: ImageButton = findViewById(R.id.btn_Banco)

// Asocia un listener de clic al botón de agua
        btnBanco.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón de agua
            showPopup()
        }

        val btnApross: ImageButton = findViewById(R.id.btn_Apross)

// Asocia un listener de clic al botón de agua
        btnApross.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón de agua
            showPopup()
        }


        val btnEnergia: ImageButton = findViewById(R.id.btn_Energia)

// Asocia un listener de clic al botón de agua
        btnEnergia.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón de agua
            showPopup()
        }

        val btnInternet: ImageButton = findViewById(R.id.btn_Internet)

// Asocia un listener de clic al botón de agua
        btnInternet.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón de agua
            showPopup()
        }

        val btnTv: ImageButton = findViewById(R.id.btn_TvDigital)

// Asocia un listener de clic al botón de agua
        btnTv.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón de agua
            showPopup()
        }
        val btnMovil: ImageButton = findViewById(R.id.btn_Movil)

// Asocia un listener de clic al botón de agua
        btnMovil.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón de agua
            showPopup()
        }
        val btnTelefono: ImageButton = findViewById(R.id.btn_Telefono)

// Asocia un listener de clic al botón de agua
        btnTelefono.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón de agua
            showPopup()
        }
        val TextReclamo: TextView = findViewById(R.id.tel_reclamo)

// Asocia un listener de clic al botón de agua
        TextReclamo.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón de agua
            showPopup()
        }
// Dentro del método onCreate() de MainActivity
        val btnIniciarSesion = findViewById<Button>(R.id.btn_iniciar_sesion)
        btnIniciarSesion.setOnClickListener {
            // Llamar a la función startLoginActivity() cuando se haga clic en el botón
            startLoginActivity()
        }



// Asocia un listener de clic al botón
        btnTramites.setOnClickListener {
            // Llama al método showPopup cuando se hace clic en el botón
            showPopup()
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)


        setSupportActionBar(toolbar)



        supportActionBar?.apply {
            // Configurar el título del ActionBar
            val title = "Coope Virtual"
            val spannableTitle = SpannableString(title)
            val fontSize = resources.getDimensionPixelSize(R.dimen.custom_title_size) // Obtener el tamaño desde los recursos

            // Obtener la fuente personalizada desde los recursos
            val typeface = ResourcesCompat.getFont(this@MainActivity, R.font.myfont)
            spannableTitle.setSpan(
                AbsoluteSizeSpan(fontSize),
                0,
                spannableTitle.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Aplicar la fuente personalizada al título
            val customTypefaceSpan = CustomTypefaceSpan(typeface)
            spannableTitle.setSpan(
                customTypefaceSpan,
                0,
                spannableTitle.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Aplicar el color al título
            spannableTitle.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.black
                    )
                ), 0, spannableTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Aplicar negrita al título
            spannableTitle.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                spannableTitle.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Establecer el título en el ActionBar
            setTitle(spannableTitle)
        }

        drawerLayout = findViewById(R.id.drawer_layout)
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

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
// Obtén el contenedor del banner

      //  1404083

        //    val bannerContainer: FrameLayout = findViewById(R.id.banner_container)
        val viewPager: ViewPager = findViewById(R.id.viewPager)
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
    }
    private fun onCallButtonClicked() {
        // Request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION_CODE)
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


    // ... other methods

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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

    //Función para abrir un sitio web

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
                startLoginActivity()
            }
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Agrega esta línea, con esto hago que si tengo una main en la pila corriendo, se elimine
        startActivity(intent)
    }


    // Método ficticio para verificar si el usuario ha iniciado sesión
    private fun usuarioHaIniciadoSesion(): Boolean {


        // Aquí implementa la lógica para verificar si el usuario ha iniciado sesión
        // Devuelve true si el usuario ha iniciado sesión, de lo contrario, devuelve false


        return false // Cambia esto según la lógica de tu aplicación

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_contratos -> {
                // Acción cuando se selecciona "Contratos"
                true
            }
            R.id.nav_tramites -> {
                // Acción cuando se selecciona "Configuración"
                true
            }
            R.id.nav_legales -> {
                // Acción cuando se selecciona "Documentos"
                true
            }
            R.id.nav_salir -> {
                // Acción cuando se selecciona "Salir"
                true
            }
            else -> false
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private class CustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {

        override fun updateMeasureState(p: android.text.TextPaint) {
            typeface?.let { p.typeface = it }
        }

        override fun updateDrawState(tp: android.text.TextPaint) {
            typeface?.let { tp.typeface = it }
        }
    }



    fun onExitButtonClick(view: View) {
        finish() // Cerrar la actividad actual

        // Lógica para manejar el clic en el botón de salir
    }


    override fun onBackPressed() {
        finish()
    }


}



