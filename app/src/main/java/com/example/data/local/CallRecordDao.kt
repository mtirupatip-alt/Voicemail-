package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CallRecordDao {
    @Query("SELECT * FROM call_records ORDER BY timestamp DESC")
    fun getAllRecordsFlow(): Flow<List<CallRecordEntity>>

    @Query("SELECT * FROM call_records WHERE rawNumber LIKE '%' || :query || '%' OR (matchedName IS NOT NULL AND matchedName LIKE '%' || :query || '%') ORDER BY timestamp DESC")
    fun searchRecords(query: String): Flow<List<CallRecordEntity>>

    @Query("SELECT COUNT(*) FROM call_records")
    fun getTotalCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM call_records WHERE timestamp >= :startOfDayTimestamp")
    fun getTodayCountFlow(startOfDayTimestamp: Long): Flow<Int>

    @Query("SELECT * FROM call_records ORDER BY timestamp DESC LIMIT 5")
    fun getRecentCallsFlow(): Flow<List<CallRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallRecord(record: CallRecordEntity): Long

    @Delete
    suspend fun deleteRecord(record: CallRecordEntity)

    @Query("DELETE FROM call_records")
    suspend fun clearAllRecords()
}
