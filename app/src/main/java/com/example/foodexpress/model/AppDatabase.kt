package com.example.foodexpress.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Anotación que define la clase como una base de datos Room.
 * @param entities Un array de todas las clases de entidad (tablas) que pertenecen a esta base de datos.
 * @param version El número de versión de la base de datos. Debe incrementarse al cambiar el esquema.
 */
@Database(entities = [Usuario::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Declara una función abstracta que devuelve el DAO (Data Access Object) para la entidad Usuario.
     * Room se encargará de generar la implementación de este método.
     * @return Una instancia de UsuarioDao.
     */
    abstract fun usuarioDao(): UsuarioDao

    /**
     * `companion object` es similar a los miembros estáticos en Java.
     * Se usa aquí para implementar el patrón Singleton, asegurando que solo exista una instancia de la base de datos en toda la app.
     */
    companion object {
        /**
         * `@Volatile` asegura que el valor de la variable INSTANCE sea siempre el más actualizado y visible para todos los hilos.
         * Es crucial para la seguridad en entornos multihilo (thread safety).
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Método estático para obtener la instancia única (Singleton) de la base de datos.
         * @param context El contexto de la aplicación.
         * @return La instancia única de AppDatabase.
         */
        fun getDatabase(context: Context): AppDatabase {
            // Si la instancia ya existe, la devuelve directamente. Si no, entra en el bloque sincronizado.
            return INSTANCE ?: synchronized(this) {
                // `synchronized(this)` asegura que solo un hilo a la vez pueda ejecutar este bloque de código,
                // evitando que se creen múltiples instancias de la base de datos simultáneamente.

                // Construye la instancia de la base de datos usando el `databaseBuilder` de Room.
                val instance = Room.databaseBuilder(
                    context.applicationContext, // El contexto global de la aplicación.
                    AppDatabase::class.java,    // La clase de la base de datos.
                    "foodexpress_database.db" // El nombre del archivo de la base de datos en el dispositivo.
                ).build()

                // Asigna la instancia recién creada a la variable estática.
                INSTANCE = instance
                // Devuelve la instancia.
                instance
            }
        }
    }
}
