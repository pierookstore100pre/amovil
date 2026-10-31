package edu.iespvigil.simulacro01

import java.io.Serializable

data class Producto(
    var id: Int,
    var nombre: String,
    var categoria: String,
    var precio: Double,
    var stock: Int,
    var imagenUrl: String
) : Serializable
