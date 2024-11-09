package com.weissoft.appnavepe.room

class CarProfileRepository(private val dao: CarProfileDao) {
    suspend fun insertProfile(carProfile: CarProfile) = dao.insertProfile(carProfile)
    suspend fun getProfile(): CarProfile? = dao.getProfile()
    suspend fun updateProfile(carProfile: CarProfile) = dao.updateProfile(carProfile)
    suspend fun deleteProfile() = dao.deleteProfile()
}
