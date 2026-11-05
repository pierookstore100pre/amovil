package com.pierook.ti.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest
import java.util.UUID

data class User(val id: Long, val username: String, val role: String)
data class Product(val id: Long, val name: String, val price: Double, val stock: Int)

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                salt TEXT NOT NULL,
                password_hash TEXT NOT NULL,
                role TEXT NOT NULL CHECK(role IN ('admin','user')),
                created_at TEXT DEFAULT CURRENT_TIMESTAMP)"""
        )
        db.execSQL(
            """CREATE TABLE products(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price REAL NOT NULL,
                stock INTEGER NOT NULL DEFAULT 0)"""
        )
        // Datos iniciales
        insertUser(db, "admin", "admin123", ROLE_ADMIN)
        insertUser(db, "usuario", "user123", ROLE_USER)
        insertProduct(db, "Teclado mecánico", 120.0, 15)
        insertProduct(db, "Mouse inalámbrico", 45.5, 30)
        insertProduct(db, "Audífonos Bluetooth", 89.9, 20)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }

    // ---------- USUARIOS ----------
    private fun hash(password: String, salt: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest((salt + password).toByteArray())
            .joinToString("") { "%02x".format(it) }

    private fun insertUser(db: SQLiteDatabase, username: String, password: String, role: String): Long {
        val salt = UUID.randomUUID().toString()
        val cv = ContentValues().apply {
            put("username", username)
            put("salt", salt)
            put("password_hash", hash(password, salt))
            put("role", role)
        }
        return db.insert("users", null, cv) // -1 si el usuario ya existe
    }

    /** Devuelve el usuario si credenciales y rol coinciden; si no, null. */
    fun login(username: String, password: String, role: String): User? {
        readableDatabase.rawQuery(
            "SELECT id, salt, password_hash FROM users WHERE username=? AND role=?",
            arrayOf(username, role)
        ).use { c ->
            if (c.moveToFirst()) {
                val salt = c.getString(1)
                if (hash(password, salt) == c.getString(2)) {
                    return User(c.getLong(0), username, role)
                }
            }
        }
        return null
    }

    fun registerUser(username: String, password: String): Boolean =
        insertUser(writableDatabase, username, password, ROLE_USER) != -1L

    fun getUsers(): List<User> {
        val list = mutableListOf<User>()
        readableDatabase.rawQuery("SELECT id, username, role FROM users ORDER BY id", null).use { c ->
            while (c.moveToNext()) list.add(User(c.getLong(0), c.getString(1), c.getString(2)))
        }
        return list
    }

    fun deleteUser(id: Long): Boolean =
        writableDatabase.delete("users", "id=? AND role='user'", arrayOf(id.toString())) > 0

    // ---------- PRODUCTOS ----------
    private fun insertProduct(db: SQLiteDatabase, name: String, price: Double, stock: Int): Long {
        val cv = ContentValues().apply {
            put("name", name); put("price", price); put("stock", stock)
        }
        return db.insert("products", null, cv)
    }

    fun addProduct(name: String, price: Double, stock: Int): Boolean =
        insertProduct(writableDatabase, name, price, stock) != -1L

    fun getProducts(): List<Product> {
        val list = mutableListOf<Product>()
        readableDatabase.rawQuery("SELECT id, name, price, stock FROM products ORDER BY id DESC", null).use { c ->
            while (c.moveToNext()) list.add(Product(c.getLong(0), c.getString(1), c.getDouble(2), c.getInt(3)))
        }
        return list
    }

    fun deleteProduct(id: Long): Boolean =
        writableDatabase.delete("products", "id=?", arrayOf(id.toString())) > 0

    companion object {
        const val DB_NAME = "ti.db"
        const val DB_VERSION = 1
        const val ROLE_ADMIN = "admin"
        const val ROLE_USER = "user"
    }
}
