package com.example

import android.app.Application
import com.example.data.local.AppDatabase
import com.example.data.repository.CallRepository
import com.example.data.repository.ContactRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.service.NotificationHelper

class ReceptionistApp : Application() {

    lateinit var database: AppDatabase
        private set
    lateinit var callRepository: CallRepository
        private set
    lateinit var contactRepository: ContactRepository
        private set
    lateinit var preferencesRepository: UserPreferencesRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = AppDatabase.getInstance(this)
        callRepository = CallRepository(database.callRecordDao())
        contactRepository = ContactRepository(database.contactDao())
        preferencesRepository = UserPreferencesRepository(this)

        NotificationHelper.createNotificationChannels(this)
    }

    companion object {
        lateinit var instance: ReceptionistApp
            private set
    }
}
