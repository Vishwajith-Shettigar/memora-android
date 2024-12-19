package com.example.data.repository

import com.example.data.local.UpdateDetailsDao
import com.example.data.local.entity.UpdateDetails
import javax.inject.Inject

interface UpdateDetailsRepository {
  suspend fun insertOrUpdate(updateDetails: UpdateDetails)
  suspend fun getUpdateDetails(): UpdateDetails?
  suspend fun deleteUpdateDetails()
}

class UpdateDetailsRepositoryImpl @Inject constructor(private val updateDetailsDao: UpdateDetailsDao) :UpdateDetailsRepository{

  override suspend fun insertOrUpdate(updateDetails: UpdateDetails) {
    updateDetailsDao.insertOrUpdateUpdateDetails(updateDetails)
  }

  override suspend fun getUpdateDetails(): UpdateDetails? {
    return updateDetailsDao.getUpdateDetails()
  }

  override suspend fun deleteUpdateDetails() {
    updateDetailsDao.deleteUpdateDetails()
  }
}
