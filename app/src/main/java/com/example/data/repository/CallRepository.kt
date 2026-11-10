package com.example.data.repository

import com.example.data.local.CallRecordDao
import com.example.data.local.CallRecordEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class CallRepository(private val callRecordDao: CallRecordDao) {
    fun getAllRecords(): Flow<List<CallRecordEntity>> = callRecordDao.getAllRecordsFlow()

    fun searchRecords(query: String): Flow<List<CallRecordEntity>> = callRecordDao.searchRecords(query)

    fun getTotalCount(): Flow<Int> = callRecordDao.getTotalCountFlow()

    fun getTodayCount(): Flow<Int> {
        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return callRecordDao.getTodayCountFlow(midnight)
    }

    fun getRecentCalls(): Flow<List<CallRecordEntity>> = callRecordDao.getRecentCallsFlow()

    suspend fun recordIncomingCall(
        rawNumber: String,
        matchedName: String?,
        action: String,
        isSpam: Boolean,
        notes: String = ""
    ): Long {
        return callRecordDao.insertCallRecord(
            CallRecordEntity(
                rawNumber = rawNumber,
                matchedName = matchedName,
                screeningAction = action,
                isSpamSuspected = isSpam,
                notes = notes
            )
        )
    }

    suspend fun deleteRecord(record: CallRecordEntity) = callRecordDao.deleteRecord(record)

    suspend fun clearHistory() = callRecordDao.clearAllRecords()
}
