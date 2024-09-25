package com.example.manosconnect20

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class perfil : Fragment() {

    private lateinit var editTextNombre: EditText
    private lateinit var editTextApellido: EditText
    private lateinit var buttonGuardar: Button
    private lateinit var buttonSeleccionarImagen: Button
    private lateinit var imageViewPerfil: ImageView
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        editTextNombre = view.findViewById(R.id.editTextNombre)
        editTextApellido = view.findViewById(R.id.editTextApellido)
        buttonGuardar = view.findViewById(R.id.buttonGuardar)
        buttonSeleccionarImagen = view.findViewById(R.id.buttonSeleccionarImagen)
        imageViewPerfil = view.findViewById(R.id.imageViewPerfil)

        cargarInformacionUsuario()

        buttonSeleccionarImagen.setOnClickListener {
            seleccionarImagen()
        }

        buttonGuardar.setOnClickListener {
            guardarCambios()
        }

        return view
    }

    private fun cargarInformacionUsuario() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            editTextNombre.setText(user.displayName)
            editTextApellido.setText(user.email?.substringBefore('@'))
            if (user.photoUrl != null) {
                Glide.with(this)
                    .load(user.photoUrl)
                    .into(imageViewPerfil)
            }
        }
    }

    private fun seleccionarImagen() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.data != null) {
            imageUri = data.data
            imageViewPerfil.setImageURI(imageUri)
        }
    }

    private fun guardarCambios() {
        val user = FirebaseAuth.getInstance().currentUser
        val nuevoNombre = editTextNombre.text.toString()

        if (user != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(nuevoNombre)
                .build()

            user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Subir la nueva imagen si se ha seleccionado
                    if (imageUri != null) {
                        val filePath = storageReference.child("profileImages/${user.uid}.jpg")
                        filePath.putFile(imageUri!!).addOnCompleteListener { uploadTask ->
                            if (uploadTask.isSuccessful) {
                                filePath.downloadUrl.addOnSuccessListener { uri ->
                                    // Actualizar la URL de la foto de perfil
                                    val newProfileUpdates = UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build()

                                    user.updateProfile(newProfileUpdates).addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            // Redirigir a MainActivity2 después de guardar
                                            val intent = Intent(requireContext(), MainActivity2::class.java)
                                            startActivity(intent)
                                            requireActivity().finish() // Opcional: cerrar la actividad actual
                                        } else {
                                            // Manejar error al actualizar el perfil
                                        }
                                    }
                                }
                            } else {
                                // Manejar error de subida
                            }
                        }
                    } else {
                        // Redirigir a MainActivity2 si no hay imagen
                        val intent = Intent(requireContext(), MainActivity2::class.java)
                        startActivity(intent)
                        requireActivity().finish() // Opcional: cerrar la actividad actual
                    }
                } else {
                    // Manejar error
                }
            }
        }
    }
    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            perfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
