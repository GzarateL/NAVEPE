package com.weissoft.appnavepe.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CarProfileDao {
    @Insert
    suspend fun insertProfile(carProfile: CarProfile)

    @Query("SELECT * FROM car_profile LIMIT 1")
    suspend fun getProfile(): CarProfile? // Define el tipo de retorno de forma explícita

    @Update
    suspend fun updateProfile(carProfile: CarProfile)

    @Query("DELETE FROM car_profile")
    suspend fun deleteProfile()
}


