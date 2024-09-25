package com.example.manosconnect20

data class Evento(
    val id: String = "", // Se recomienda inicializarlo con un valor por defecto
    val titulo: String = "",
    val fecha: String = "",
    val correo: String = "" // Añadido para almacenar el correo del participante
)
