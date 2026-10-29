package com.duoc.madertimber.data.repository

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "seleccion_panel")
data class SeleccionGuardada(
    @PrimaryKey val id: Int = 1,
    val lineaId: String,
    val anio: Int,
    val metrica: String
)

@Dao
interface SeleccionDao {
    @Query("SELECT * FROM seleccion_panel WHERE id = 1")
    suspend fun leer(): SeleccionGuardada?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(seleccion: SeleccionGuardada)
}

@Database(entities = [SeleccionGuardada::class], version = 1, exportSchema = false)
abstract class SeleccionDatabase : RoomDatabase() {
    abstract fun seleccionDao(): SeleccionDao

    companion object {
        @Volatile private var instancia: SeleccionDatabase? = null

        fun obtener(context: Context): SeleccionDatabase = instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                context.applicationContext, SeleccionDatabase::class.java, "seleccion-panel.db"
            ).build().also { instancia = it }
        }
    }
}

class SeleccionRepository(private val dao: SeleccionDao) {
    suspend fun leer(): SeleccionGuardada? = dao.leer()
    suspend fun guardar(seleccion: SeleccionGuardada) = dao.guardar(seleccion)
}
