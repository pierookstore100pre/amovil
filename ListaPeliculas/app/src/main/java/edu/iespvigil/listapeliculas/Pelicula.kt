package edu.iespvigil.listapeliculas

import java.io.Serializable

data class Pelicula(
    var idImagen: Int,
    var titulo: String,
    var director: String,
    var genero: String,
    var calificacion: Double,
    var duracion: Int,
    var fecha: String
) : Serializable
