package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContactsFlow(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :query || '%' OR phoneNumber LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchContacts(query: String): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE phoneNumber = :number LIMIT 1")
    suspend fun findByNumber(number: String): ContactEntity?

    @Query("SELECT * FROM contacts WHERE phoneNumber LIKE '%' || :lastDigits LIMIT 1")
    suspend fun findByEndingDigits(lastDigits: String): ContactEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateContact(contact: ContactEntity): Long

    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    @Query("SELECT COUNT(*) FROM contacts")
    fun getContactsCountFlow(): Flow<Int>
}
