package com.example.manosconnect20

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HacerServicioActivity : AppCompatActivity() {

    private lateinit var nombreServicioEditText: EditText
    private lateinit var precioServicioEditText: EditText
    private lateinit var detallesServicioEditText: EditText
    private lateinit var direccionServicioEditText: EditText
    private lateinit var botonSubirServicio: Button

    // Referencia a la base de datos de Firebase
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hacer_servicio)

        // Inicializar los campos de entrada y el botón
        nombreServicioEditText = findViewById(R.id.nombre_servicio)
        precioServicioEditText = findViewById(R.id.precio_servicio)
        detallesServicioEditText = findViewById(R.id.detalles_servicio)
        direccionServicioEditText = findViewById(R.id.direccion_servicio)
        botonSubirServicio = findViewById(R.id.boton_subir_servicio)

        // Inicializar la base de datos
        database = FirebaseDatabase.getInstance().getReference("servicios")

        // Configurar el listener para el botón
        botonSubirServicio.setOnClickListener {
            subirServicio()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun subirServicio() {
        // Obtener los datos ingresados
        val nombreServicio = nombreServicioEditText.text.toString().trim()
        val precioServicio = precioServicioEditText.text.toString().trim()
        val detallesServicio = detallesServicioEditText.text.toString().trim()
        val direccionServicio = direccionServicioEditText.text.toString().trim()

        // Validar los datos
        if (nombreServicio.isEmpty() || precioServicio.isEmpty() || detallesServicio.isEmpty() || direccionServicio.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un objeto servicio
        val servicioId = database.push().key // Genera un ID único
        val servicio = Servicio(servicioId, nombreServicio, precioServicio, detallesServicio, direccionServicio)

        // Subir los datos a Firebase
        if (servicioId != null) {
            database.child(servicioId).setValue(servicio).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Servicio subido con éxito", Toast.LENGTH_SHORT).show()
                    // Limpiar los campos después de subir el servicio
                    limpiarCampos()
                } else {
                    Toast.makeText(this, "Error al subir el servicio", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun limpiarCampos() {
        nombreServicioEditText.text.clear()
        precioServicioEditText.text.clear()
        detallesServicioEditText.text.clear()
        direccionServicioEditText.text.clear()
    }

    // Clase para representar un servicio
    data class Servicio(val id: String?, val nombre: String, val precio: String, val detalles: String, val direccion: String)
}
