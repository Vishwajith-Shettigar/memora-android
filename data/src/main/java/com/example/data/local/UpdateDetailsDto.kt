package com.example.data.local

import androidx.room.*
import com.example.data.local.entity.UpdateDetails

@Dao
interface UpdateDetailsDao {

  // Insert or update a single record (enforces one record at a time)
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateUpdateDetails(updateDetails: UpdateDetails)

  // Get the single record
  @Query("SELECT * FROM update_details LIMIT 1")
  suspend fun getUpdateDetails(): UpdateDetails?

  // Delete the single record.
  @Query("DELETE FROM update_details")
  suspend fun deleteUpdateDetails()
}
