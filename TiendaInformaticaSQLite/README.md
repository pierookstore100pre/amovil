# Tienda Informática (SQLite) — Android Studio Quail 3

App Android en Kotlin que **combina dos proyectos**:

| Proyecto | Qué aporta |
|---|---|
| `03_02_guia_SQLite02` (repo `amovil`) | La técnica: `SQLiteOpenHelper`, `ContentValues`, `rawQuery`, ViewBinding y las operaciones Guardar / Modificar / Borrar / Consultar |
| `ti` (tienda en CodeIgniter 4) | El dominio: categorías, marcas y productos con stock, "a pedido" y estado de stock, más los datos de ejemplo de sus scripts SQL |

El resultado es la versión móvil y sin conexión de la tienda: el mismo esquema de la base MySQL, pero guardado en SQLite dentro del teléfono.

## Cómo abrirla

1. Descomprime la carpeta `TiendaInformaticaSQLite` (por ejemplo en `C:\AndroidProyectos`).
2. En Android Studio Quail 3: **File > Open** y elige esa carpeta.
3. Espera el *Gradle Sync* (la primera vez descarga `recyclerview 1.4.0`, que es la única dependencia nueva).
4. Ejecuta en un emulador o dispositivo con Android 9 (API 28) o superior.

La configuración de Gradle (AGP, SDK, JDK) es la misma de la guía `03_02`.

## Qué hace la app

- **Catálogo** (pantalla principal): lista de productos con búsqueda por nombre/descripción y filtros por categoría y marca. Muestra el estado de stock con color: *Disponible*, *Pocas unidades* (5 o menos, igual que `STOCK_CRITICO` de la web), *Agotado* y *A pedido*.
- **Formulario de producto** (botón **+** o tocar un producto): crear, modificar y eliminar. Al editar, el stock cambia con **Entrada / Salida**, como la gestión de stock del panel admin web (una salida mayor al stock se rechaza).
- **Categorías y Marcas** (menú de la barra superior): mantenimiento de ambas tablas con la misma pantalla. No deja borrar una categoría o marca que tenga productos.

## Correspondencia con los dos proyectos

| Guía 03_02 (`miSQLiteHelper`) | Esta app (`TiendaDbHelper`) |
|---|---|
| tabla `amigos` | tablas `categorias`, `marcas`, `productos` |
| `anadirDato` | `anadirProducto`, `guardarMaestro` |
| `modificarDato` | `modificarProducto`, `guardarMaestro` |
| `borrarDato` | `borrarProducto`, `borrarMaestro` |
| `SELECT * FROM amigos` | `listarProductos(texto, categoria, marca)` |

| Proyecto web `ti` | Esta app |
|---|---|
| `database/01_...sql` y `02_...sql` | `cargarDatosDeEjemplo()`: 4 categorías, 5 marcas y 7 productos |
| `ProductoModel::conFiltros` | `listarProductos` (misma consulta con JOIN) |
| `estado_stock()` / `STOCK_CRITICO` | `Producto.estadoStock()` / `TiendaDbHelper.STOCK_CRITICO` |
| `Admin\Stock::ajustar` | `TiendaDbHelper.ajustarStock` |
| `Admin\SimpleCrud` (categorías, marcas) | `MaestroActivity` |

No se migraron las partes que necesitan servidor: login, carrito, pedidos y ventas.

## Estructura

```
app/src/main/java/edu/iespvigil/tiendainformatica/
├── Modelos.kt               Opcion, Producto, EstadoStock
├── TiendaDbHelper.kt        Base de datos SQLite (esquema, datos de ejemplo, CRUD)
├── MainActivity.kt          Catálogo con búsqueda y filtros
├── ProductoAdapter.kt       Tarjetas del RecyclerView
├── ProductoFormActivity.kt  Crear / editar / eliminar producto y mover stock
└── MaestroActivity.kt       Mantenimiento de categorías y marcas
```

## Para volver a empezar con los datos de ejemplo

Desinstala la app del emulador (o borra sus datos): la base de datos `tienda_informatica.db` se vuelve a crear con los datos de ejemplo la próxima vez que se abra.
