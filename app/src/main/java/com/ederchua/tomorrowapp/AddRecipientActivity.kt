package com.ederchua.tomorrowapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalTime

class AddRecipientActivity: AppCompatActivity() {
    private var userId = 0

    @SuppressLint("NewApi")
    var localTime: LocalTime = LocalTime.now()
    @SuppressLint("NewApi")
    var localDate: LocalDate = LocalDate.now()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipient_activity)

        val bundle = intent.extras
        if (bundle != null) {
            userId = bundle.getInt("userId")
        }

        val inputFullName = findViewById<EditText>(R.id.inputFullName)
        val inputPhoneNumber = findViewById<EditText>(R.id.inputPhoneNumber)
        val inputPriorityGroup = findViewById<AutoCompleteTextView>(R.id.inputPriorityGroup)

        if (bundle!!.containsKey("fullName")) inputFullName.setText(bundle.getString("fullName"))
        if (bundle!!.containsKey("phoneNumber")) inputPhoneNumber.setText(bundle.getString("phoneNumber"))
        if (bundle!!.containsKey("priorityGroup")) inputPriorityGroup.setText(bundle.getString("priorityGroup"))

        val btnConfirm = findViewById<FloatingActionButton>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            if (inputFullName.text.toString() == "") {
                Toast.makeText(this, "Please set your Full Name.", Toast.LENGTH_LONG).show()
            } else if (inputPhoneNumber.text.toString() == ""){
                Toast.makeText(this, "Please set your Phone Number.", Toast.LENGTH_LONG).show()
            } else if (inputPriorityGroup.text.toString() == ""){
                Toast.makeText(this, "Please set your Priority Group.", Toast.LENGTH_LONG).show()
            } else if (bundle!!.containsKey("id")){
                val recipient = Recipient()
                recipient.id = bundle.getInt("id")
                recipient.fullName = inputFullName.text.toString()
                recipient.phoneNumber = inputPhoneNumber.text.toString()
                recipient.priorityGroup = inputPriorityGroup.text.toString()

                SQLHelper(this).updateRecipient(recipient)
            } else {
                SQLHelper(this).addRecipient(
                    userId,
                    inputFullName.text.toString(),
                    inputPriorityGroup.text.toString(),
                    inputPhoneNumber.text.toString()
                )
            }
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}