package com.example.data.repository

import com.example.data.local.ContactDao
import com.example.data.local.ContactEntity
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {
    fun getAllContacts(): Flow<List<ContactEntity>> = contactDao.getAllContactsFlow()

    fun searchContacts(query: String): Flow<List<ContactEntity>> = contactDao.searchContacts(query)

    suspend fun findContactByNumber(number: String): ContactEntity? {
        val cleaned = number.replace(Regex("[^0-9+]"), "")
        val direct = contactDao.findByNumber(cleaned)
        if (direct != null) return direct

        val digitsOnly = number.replace(Regex("[^0-9]"), "")
        if (digitsOnly.length >= 7) {
            val lastSeven = digitsOnly.takeLast(7)
            val partial = contactDao.findByEndingDigits(lastSeven)
            if (partial != null) return partial
        }
        return null
    }

    suspend fun saveContact(contact: ContactEntity): Long = contactDao.insertOrUpdateContact(contact)

    suspend fun deleteContact(contact: ContactEntity) = contactDao.deleteContact(contact)

    fun getCount(): Flow<Int> = contactDao.getContactsCountFlow()
}
