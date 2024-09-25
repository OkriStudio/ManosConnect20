package com.example.manosconnect20

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class calendario : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var botonAgregarEvento: Button
    private val eventos = mutableListOf<Evento>()
    private lateinit var adapter: EventoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendario, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewEventos)
        botonAgregarEvento = view.findViewById(R.id.botonAgregarEvento)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = EventoAdapter(eventos)
        recyclerView.adapter = adapter

        botonAgregarEvento.setOnClickListener {
            mostrarDialogoAgregarEvento()
        }

        return view
    }

    private fun mostrarDialogoAgregarEvento() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_agregar_evento)

        val editTextTitulo = dialog.findViewById<EditText>(R.id.editTextTitulo)
        val editTextFecha = dialog.findViewById<EditText>(R.id.editTextFecha)
        val editTextCorreo = dialog.findViewById<EditText>(R.id.editTextCorreo)
        val buttonAgregar = dialog.findViewById<Button>(R.id.buttonAgregar)

        buttonAgregar.setOnClickListener {
            val titulo = editTextTitulo.text.toString()
            val fecha = editTextFecha.text.toString()
            val correo = editTextCorreo.text.toString()

            if (titulo.isNotEmpty() && fecha.isNotEmpty() && correo.isNotEmpty()) {
                val nuevoEvento = Evento("id-${eventos.size + 1}", titulo, fecha, correo)
                eventos.add(nuevoEvento)
                guardarEventoEnFirestore(nuevoEvento)
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            } else {
                showToast("Por favor, complete todos los campos.")
            }
        }

        dialog.show()
    }

    private fun guardarEventoEnFirestore(evento: Evento) {
        val db = FirebaseFirestore.getInstance()
        db.collection("eventos")
            .add(evento)
            .addOnSuccessListener {
                showToast("Evento guardado exitosamente.")
            }
            .addOnFailureListener { e ->
                showToast("Error al guardar evento: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        cargarEventosDeFirestore()
    }

    private fun cargarEventosDeFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("eventos")
            .get()
            .addOnSuccessListener { documents ->
                eventos.clear()
                for (document in documents) {
                    val evento = document.toObject(Evento::class.java)
                    eventos.add(evento)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                showToast("Error al cargar eventos: ${e.message}")
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            calendario().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
