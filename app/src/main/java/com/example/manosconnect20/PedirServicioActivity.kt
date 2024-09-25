package com.example.manosconnect20

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.*

class PedirServicioActivity : AppCompatActivity() {

    private lateinit var serviciosContainer: LinearLayout
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pedir_servicio)

        serviciosContainer = findViewById(R.id.servicios_container)

        // Inicializar Firebase Database
        database = FirebaseDatabase.getInstance().getReference("servicios")

        // Cargar los servicios
        cargarServicios()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarServicios() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                serviciosContainer.removeAllViews() // Limpiar antes de cargar nuevos datos
                for (servicioSnapshot in snapshot.children) {
                    val servicio = servicioSnapshot.getValue(Servicio::class.java)
                    servicio?.let {
                        agregarServicioView(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PedirServicioActivity, "Error al cargar servicios: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun agregarServicioView(servicio: Servicio) {
        val servicioTextView = TextView(this)
        servicioTextView.text = "${servicio.nombre}: ${servicio.precio}\n${servicio.detalles}\nDirección: ${servicio.direccion}"
        servicioTextView.textSize = 18f
        servicioTextView.setPadding(8, 8, 8, 8)
        serviciosContainer.addView(servicioTextView)
    }

    // Clase para representar un servicio
    data class Servicio(val id: String? = null, val nombre: String = "", val precio: String = "", val detalles: String = "", val direccion: String = "")
}
