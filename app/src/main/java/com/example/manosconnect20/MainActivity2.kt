package com.example.manosconnect20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        // Configuración para los Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Cargar el fragmento Casa inmediatamente al iniciar
        cargarFragmentoCasa()

        // Configuración de los listeners para los botones
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        // Obtener referencias a los botones
        val imageButton1: ImageButton = findViewById(R.id.imageButton)
        val imageButton2: ImageButton = findViewById(R.id.imageButton2)
        val imageButton3: ImageButton = findViewById(R.id.imageButton3)
        val imageButton4: ImageButton = findViewById(R.id.imageButton4)
        val imageButton6: ImageButton = findViewById(R.id.imageButton6)
        val imageButton7: ImageButton = findViewById(R.id.imageButton7)
        val imageButton8: ImageButton = findViewById(R.id.imageButton8)

        // Configurar los listeners para cada botón
        imageButton1.setOnClickListener { cargarFragmentoCasa() }
        imageButton2.setOnClickListener { cargarFragmentoPerfil() }
        imageButton3.setOnClickListener { cargarFragmentoBuscar() }
        imageButton4.setOnClickListener { cargarFragmentoTrofeo() }
        imageButton6.setOnClickListener { cargarFragmentoPerfil1() }
        imageButton7.setOnClickListener { cargarFragmentoCalendario() }
        imageButton8.setOnClickListener { cargarFragmentoMensajero() }
    }

    private fun mostrarInformacionUsuario() {
        val usuario = FirebaseAuth.getInstance().currentUser
        if (usuario != null) {
            val email = usuario.email ?: ""
            // Muestra la información del usuario, por ejemplo, en un TextView
            showToast("Bienvenido, $email")
        }
    }


    private fun cargarFragmentoCasa() {
        val casaFragment = casa.newInstance("Valor de Param1", "Valor de Param2") // Crear el fragmento casa
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contenedor, casaFragment) // Reemplazar el contenedor con casa
        fragmentTransaction.addToBackStack(null) // Opcional, para poder volver al fragmento anterior
        fragmentTransaction.commit()
    }

    private fun cargarFragmentoPerfil() {
        val perfilFragment = perfil.newInstance("Valor de Param1", "Valor de Param2")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contenedor, perfilFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun cargarFragmentoPerfil1() {
        val perfil1Fragment = perfil1.newInstance("Valor de Param1", "Valor de Param2")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contenedor, perfil1Fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun cargarFragmentoBuscar() {
        val buscarFragment = buscar.newInstance("Valor de Param1", "Valor de Param2")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contenedor, buscarFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun cargarFragmentoCalendario() {
        val calendarioFragment = calendario.newInstance("Valor de Param1", "Valor de Param2")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contenedor, calendarioFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun cargarFragmentoMensajero() {
        val mensajeroFragment = mensajero.newInstance("Valor de Param1", "Valor de Param2")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contenedor, mensajeroFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun cargarFragmentoTrofeo() {
        val trofeoFragment = Trofeo.newInstance("Valor de Param1", "Valor de Param2")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contenedor, trofeoFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
