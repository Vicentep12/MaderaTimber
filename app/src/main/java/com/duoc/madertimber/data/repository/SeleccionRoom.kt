package com.duoc.madertimber.data.repository

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

// Última línea, año y métrica que eligió el usuario en el dashboard
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

class SeleccionRepository(private val dao: SeleccionDao) {
    suspend fun leer(): SeleccionGuardada? = dao.leer()
    suspend fun guardar(seleccion: SeleccionGuardada) = dao.guardar(seleccion)
}
