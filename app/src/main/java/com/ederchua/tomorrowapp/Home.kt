package com.ederchua.tomorrowapp

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.ederchua.tomorrowapp.fragments.RecipientsFragment
import com.ederchua.tomorrowapp.fragments.adapter.ViewPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class Home : AppCompatActivity() {

    private lateinit var email: String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setUpTabs()

//        val btnSend = findViewById<Button>(R.id.btnSend)
//        btnSend.setOnClickListener {
//            for (r in recipients){
//                if(r.messageSent == 0){
//                    sendMessage(r)
//                    r.messageSent = 1
//                }
//            }
//        }

        val btnLogout = findViewById<TextView>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        val bundle = intent.extras
        if (bundle != null) {
            email = bundle.getString("email").toString()
        }

        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        //val tvDate = findViewById<TextView>(R.id.tvDate)
        tvEmail.text = "Hello, " + email.split("@")[0] + "!"
//        tvDate.text = LocalTime
//                                .now(ZoneId.systemDefault())
//                                .format(DateTimeFormatter.ofPattern("HH:mm"))
//                                .toString()

        val btnAddRecipient = findViewById<FloatingActionButton>(R.id.btnAddRecipient)
        btnAddRecipient.setOnClickListener {
            val intent = Intent(this, AddRecipientActivity::class.java)
            intent.putExtra("userId", SQLHelper(this).getUserIdFromEmail(email))
            startActivity(intent)
            println("Added")
//            refreshRecipientList()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(RecipientsFragment(), "Unsent")
        adapter.addFragment(RecipientsFragment(), "Sent")
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = adapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)


    }

}