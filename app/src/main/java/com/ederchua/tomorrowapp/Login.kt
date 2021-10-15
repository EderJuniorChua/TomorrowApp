package com.ederchua.tomorrowapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class Login : AppCompatActivity() {

    private val PERMISSION_ALL = 1
    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.READ_PHONE_STATE,
        android.Manifest.permission.SEND_SMS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val inputLoginEmail = findViewById<EditText>(R.id.txtLoginEmail)
        val inputLoginPassword = findViewById<EditText>(R.id.txtLoginPassword)

        val db = SQLHelper(this)

        btnLogin.setOnClickListener{
            if (!db.isRegistered(inputLoginEmail.text.toString())){
                Toast.makeText(this, "User does not exist.", Toast.LENGTH_SHORT).show()
            } else if (!db.isValidCredentials(inputLoginEmail.text.toString(), inputLoginPassword.text.toString())){
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
            } else {
                permissionPrompt()
//                val intent = Intent(this, Home::class.java)
//                intent.putExtra("email", inputLoginEmail.text.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        btnRegister.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun permissionPrompt(){
        if (!hasPermissions(this, android.Manifest.permission.SEND_SMS, android.Manifest.permission.READ_PHONE_STATE)
        ) {
            println("Permissions not granted")
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }  else {
            println("Permissions granted, proceeding")
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}