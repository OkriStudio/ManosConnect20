package com.example.manosconnect20

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class perfil1 : Fragment() {

    private lateinit var imageViewPerfil: ImageView
    private lateinit var textViewNombre: TextView
    private lateinit var textViewApellido: TextView

    // Variables para parámetros
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_perfil1, container, false)

        // Inicializar las vistas
        imageViewPerfil = view.findViewById(R.id.imageViewPerfil)
        textViewNombre = view.findViewById(R.id.textViewNombre)
        textViewApellido = view.findViewById(R.id.textViewApellido)

        // Cargar la información del usuario
        cargarInformacionUsuario()

        return view
    }

    private fun cargarInformacionUsuario() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Asigna la información del usuario a las vistas
            textViewNombre.text = user.displayName ?: "Nombre no disponible"
            textViewApellido.text = user.email?.substringBefore('@') ?: "Apellido no disponible"

            // Cargar imagen de perfil por defecto
            imageViewPerfil.setImageResource(R.mipmap.mujer) // Placeholder por defecto
        } else {
            // Manejar el caso en que el usuario no está autenticado
            textViewNombre.text = "Usuario no autenticado"
            textViewApellido.text = ""
            imageViewPerfil.setImageResource(R.mipmap.mujer) // Placeholder por defecto
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            perfil1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
