package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_records")
data class CallRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rawNumber: String,
    val matchedName: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val screeningAction: String,
    val isSpamSuspected: Boolean = false,
    val notes: String = ""
)
