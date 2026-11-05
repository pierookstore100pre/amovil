package edu.iespvigil.tiendainformatica

// ---------------------------------------------------------------------------
// Modelos de datos de la tienda (equivalen a las tablas del proyecto web "ti")
// ---------------------------------------------------------------------------

/**
 * Elemento sencillo (id + texto). Sirve para categorías y marcas, y también
 * para llenar los Spinner: el Spinner muestra lo que devuelve toString().
 */
data class Opcion(val id: Int, val texto: String) {
    override fun toString(): String = texto
}

/** Un producto del catálogo. Incluye el nombre de su categoría y de su marca. */
data class Producto(
    val id: Int = 0,
    val categoriaId: Int = 0,
    val marcaId: Int = 0,              // 0 = sin marca
    val nombre: String = "",
    val descripcion: String = "",
    val descripcionLarga: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val aPedido: Boolean = false,
    val activo: Boolean = true,
    val categoria: String = "",
    val marca: String = "",
    val actualizado: String = ""
)

/** Estados de stock (misma lógica que estado_stock() del proyecto web). */
enum class EstadoStock(val texto: String) {
    DISPONIBLE("Disponible"),
    POCAS_UNIDADES("Pocas unidades"),
    AGOTADO("Agotado"),
    A_PEDIDO("A pedido")
}

/** Calcula el estado de stock del producto. */
fun Producto.estadoStock(): EstadoStock = when {
    aPedido -> EstadoStock.A_PEDIDO
    stock <= 0 -> EstadoStock.AGOTADO
    stock <= TiendaDbHelper.STOCK_CRITICO -> EstadoStock.POCAS_UNIDADES
    else -> EstadoStock.DISPONIBLE
}

/** Un producto está en "stock crítico" si no es a pedido y tiene pocas unidades o ninguna. */
fun Producto.esStockCritico(): Boolean =
    activo && !aPedido && stock <= TiendaDbHelper.STOCK_CRITICO
