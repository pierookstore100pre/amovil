package edu.iespvigil.practica

data class Estudiante(val id: Int, val nombre: String)

data class Tramite(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val requisitos: String,
    val costo: String
)

data class Pendiente(
    val id: Int,
    val tramite: String,
    val fecha: String,
    val estudiante: String,
    val estado: String
)
