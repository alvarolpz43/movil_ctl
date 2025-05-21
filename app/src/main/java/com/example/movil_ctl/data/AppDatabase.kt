package com.example.movil_ctl.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movil_ctl.data.dao.ContratistaDao
import com.example.movil_ctl.data.dao.EquipoDao
import com.example.movil_ctl.data.dao.EspecieDao
import com.example.movil_ctl.data.dao.FincaDao
import com.example.movil_ctl.data.dao.NucleoDao
import com.example.movil_ctl.data.dao.OperadorDao
import com.example.movil_ctl.data.dao.RegistroDao
import com.example.movil_ctl.data.dao.TurnoDao
import com.example.movil_ctl.data.dao.ZonaDao
import com.example.movil_ctl.data.entities.ContratistaEntity
import com.example.movil_ctl.data.entities.EquipoEntity
import com.example.movil_ctl.data.entities.EspecieEntity
import com.example.movil_ctl.data.entities.FincasEntity
import com.example.movil_ctl.data.entities.NucleosEntity
import com.example.movil_ctl.data.entities.OperadorEntity
import com.example.movil_ctl.data.entities.RegistroEntity
import com.example.movil_ctl.data.entities.TurnoEntity
import com.example.movil_ctl.data.entities.ZonasEntity


@Database(
    entities = [
        ContratistaEntity::class,
        ZonasEntity::class,
        NucleosEntity::class,
        FincasEntity::class,
        EspecieEntity::class,
        TurnoEntity::class,
        EquipoEntity::class,
        OperadorEntity::class,
        RegistroEntity::class
    ],
    version = 2,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contratistaDao(): ContratistaDao
    abstract fun zonaDao(): ZonaDao
    abstract fun nucleoDao(): NucleoDao
    abstract fun fincaDao(): FincaDao
    abstract fun especieDao(): EspecieDao
    abstract fun turnoDao(): TurnoDao
    abstract fun equipoDao(): EquipoDao
    abstract fun operadorDao(): OperadorDao
    abstract fun registroDao(): RegistroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}