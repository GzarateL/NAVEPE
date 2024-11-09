package com.weissoft.appnavepe.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CarProfile::class], version = 1, exportSchema = false)
abstract class CarProfileDatabase : RoomDatabase() {

    abstract fun carProfileDao(): CarProfileDao

    companion object {
        // Variable de instancia única de la base de datos
        @Volatile
        private var INSTANCE: CarProfileDatabase? = null

        // Método para obtener la instancia de la base de datos
        fun getDatabase(context: Context): CarProfileDatabase {
            // Verifica si la instancia ya existe. Si no, la crea
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarProfileDatabase::class.java,
                    "car_profile_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
