package com.duoc.madertimber.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SeleccionGuardada::class, UsuarioGuardado::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun seleccionDao(): SeleccionDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile private var instancia: AppDatabase? = null

        // Una sola base para toda la app, para no abrirla varias veces
        fun obtener(context: Context): AppDatabase = instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "madertimber.db"
            ).build().also { instancia = it }
        }
    }
}
