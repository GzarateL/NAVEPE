package com.weissoft.appnavepe.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_profile")
data class CarProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val driverName: String,
    val carNickname: String,
    val carModel: String,
    val plate: String,
    val color: String,
    val imageUri: String? = null // URI de la imagen como cadena
)
