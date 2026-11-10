package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts",
    indices = [Index(value = ["phoneNumber"], unique = true)]
)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val customNotes: String = "",
    val isVip: Boolean = false,
    val company: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
