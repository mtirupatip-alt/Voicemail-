package com.example.service

import android.telecom.Call
import android.telecom.CallScreeningService
import com.example.ReceptionistApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ReceptionistCallScreeningService : CallScreeningService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onScreenCall(callDetails: Call.Details) {
        val rawHandle = callDetails.handle?.schemeSpecificPart ?: "Private Number"
        val isIncoming = callDetails.callDirection == Call.Details.DIRECTION_INCOMING

        if (!isIncoming) {
            allowCallSilently(callDetails)
            return
        }

        serviceScope.launch {
            val app = ReceptionistApp.instance
            val isEnabled = app.preferencesRepository.isServiceActive.first()
            val notificationsAllowed = app.preferencesRepository.areNotificationsEnabled.first()

            if (!isEnabled) {
                allowCallSilently(callDetails)
                return@launch
            }

            // Look up caller in local CRM
            val matchedContact = app.contactRepository.findContactByNumber(rawHandle)
            val callerDisplayName = matchedContact?.name ?: rawHandle
            val isVip = matchedContact?.isVip ?: false
            val action = if (isVip) "VIP Priority" else "Screened & Logged"

            // Log securely in local Room database
            app.callRepository.recordIncomingCall(
                rawNumber = rawHandle,
                matchedName = matchedContact?.name,
                action = action,
                isSpam = false,
                notes = if (isVip) "VIP contact detected: ${matchedContact?.customNotes}" else ""
            )

            // Platform guarantee: Never block or disrupt calls without explicit permission.
            // Allow the incoming call to ring normally.
            val response = CallResponse.Builder()
                .setDisallowCall(false)
                .setRejectCall(false)
                .setSilenceCall(false)
                .setSkipCallLog(false)
                .setSkipNotification(false)
                .build()

            respondToCall(callDetails, response)

            if (notificationsAllowed) {
                NotificationHelper.showCallScreenedNotification(
                    this@ReceptionistCallScreeningService,
                    callerDisplayName,
                    action
                )
            }
        }
    }

    private fun allowCallSilently(callDetails: Call.Details) {
        val response = CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSilenceCall(false)
            .build()
        respondToCall(callDetails, response)
    }
}
