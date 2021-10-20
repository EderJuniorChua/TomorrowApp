package com.ederchua.tomorrowapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.apache.commons.lang3.StringUtils.isAlphanumeric
import org.apache.commons.validator.routines.EmailValidator

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        val inputEmail = findViewById<EditText>(R.id.txtRegisterEmail)
        val inputPassword = findViewById<EditText>(R.id.txtRegisterPassword)
        val inputPasswordConfirm = findViewById<EditText>(R.id.txtRegisterConfirmPassword)
        val db = SQLHelper(this)

        btnRegister.setOnClickListener{
            println("$inputPassword =?= $inputPasswordConfirm")
            if (EmailValidator.getInstance().isValid(inputEmail.text.toString())){
                if (isAlphanumeric(inputPassword.text.toString())){
                    if (isAlphanumeric(inputPasswordConfirm.text.toString())){
                        if (inputPassword.text.toString() == inputPasswordConfirm.text.toString()) {
                            if (!db.isRegistered(inputEmail.text.toString())) {
                                db.register(inputEmail.text.toString(), inputPassword.text.toString())
                                finish()
                            } else {
                                Toast.makeText(this, "User is already registered", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Email format is invalid", Toast.LENGTH_SHORT).show()
            }
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }




}