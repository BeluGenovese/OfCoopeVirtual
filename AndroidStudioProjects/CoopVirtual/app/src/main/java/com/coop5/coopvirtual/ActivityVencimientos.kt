package com.coop5.coopvirtual

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coop5.coopvirtual.MyAdapter
import com.coop5.coopvirtual.MyData
import com.coop5.coopvirtual.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class ActivityVencimientos : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vencimientos)

        // Recuperar las variables de usuario del intent
        val username = intent.getStringExtra("USERNAME")
        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val jsonString = """
            {
                "status":200,
                "message":"Próximos Vencimientos.",
                "body":[
                    {"DsServicio":"Energía", "DsPeriodo":"Mar 24", "FechaVto1":"13/05/2024", "FechaVto2":"20/05/2024", "FechaVto3":"27/05/2024", "IdServicio":1},
                    {"DsServicio":"Agua Potable", "DsPeriodo":"Mar 24", "FechaVto1":"13/05/2024", "FechaVto2":"20/05/2024", "FechaVto3":"27/05/2024", "IdServicio":2},
                    {"DsServicio":"Teléfono", "DsPeriodo":"Mar 24", "FechaVto1":"13/05/2024", "FechaVto2":"20/05/2024", "FechaVto3":"20/05/2024", "IdServicio":3},
                    {"DsServicio":"Apross", "DsPeriodo":"May 24", "FechaVto1":"13/05/2024", "FechaVto2":"20/05/2024", "FechaVto3":"20/05/2024", "IdServicio":4},
                    {"DsServicio":"Internet", "DsPeriodo":"Abr 24", "FechaVto1":"13/05/2024", "FechaVto2":"20/05/2024", "FechaVto3":"20/05/2024", "IdServicio":5},
                    {"DsServicio":"TV Digital (IPTV)", "DsPeriodo":"Abr 24", "FechaVto1":"13/05/2024", "FechaVto2":"20/05/2024", "FechaVto3":"20/05/2024", "IdServicio":30},
                    {"DsServicio":"MóvilCoop (CLARO)", "DsPeriodo":"Abr 24", "FechaVto1":"13/05/2024", "FechaVto2":"20/05/2024", "FechaVto3":"20/05/2024", "IdServicio":32}
                ]
            }
        """.trimIndent()

        try {
            val data = parseData(jsonString)
            adapter = MyAdapter(data)
            recyclerView.adapter = adapter

            // Referencia al TextView del título
            val titleTextView: TextView = findViewById(R.id.titleTextView)

            // Obtener el color desde los recursos
            val color = ContextCompat.getColor(this, R.color.black)

            // Cambiar el color del texto
            titleTextView.setTextColor(color)
        } catch (e: JsonSyntaxException) {
            Log.e("Vencimientos", "Error al parsear los datos: ${e.message}")
        }


        val backButton: Button = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, MainSesionActivity::class.java)
            // Agregar las variables de usuario al Intent
            intent.putExtra("USERNAME", username) // Aquí username es el nombre de usuario obtenido anteriormente
            intent.putExtra("nombre", nombre) // Aquí nombre es el nombre obtenido anteriormente
            intent.putExtra("apellido", apellido) // Aquí apellido es el apellido obtenido anteriormente
            startActivity(intent)
            finish()
        }
    }

    }

    private fun parseData(jsonString: String): List<MyData> {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        val jsonArray = jsonObject.getAsJsonArray("body")

        val type = object : TypeToken<List<MyData>>() {}.type

        Log.d("Vencimientos", "JSON Array: $jsonArray")

        return gson.fromJson(jsonArray, type)
    }


