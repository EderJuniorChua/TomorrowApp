package com.ederchua.tomorrowapp

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime

class SMSHelper {


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage(recipient: Recipient, context: Context) {

        print("sendMessage(): sending message to $recipient.phoneNumber")

        val message = "(Bancal Vaccination Drive)\n" +
                "Dear ${recipient.fullName}, " +
                "As a priority group -  ${recipient.priorityGroup}, you were prioritize a vaccine slot. " +
                "Reply YES to Confirm"
//        "and NO to Decline your slot and continue the next time it's available. "
//                "If you wish to proceed, please come to your baranggay hall in the next 2 hours."
        val smsManager: SmsManager = SmsManager.getDefault()
        val sentPI: PendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), 0)
        smsManager.sendTextMessage(recipient.phoneNumber, null, message, sentPI, null)
//        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
        println("sendMessage(): Message sent!")
        recipient.messageSent = 1
        recipient.dateTarget = LocalDate.now().toString()
        recipient.timeTarget = LocalTime.now().plusHours(2).toString()
        SQLHelper(context).updateRecipient(recipient)
//        refreshRecipientList()
    }
}