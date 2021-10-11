package com.ederchua.tomorrowapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// here we have defined variables for our database

// below is variable for database name
private val DATABASE_NAME = "SCHEDULENOTIFYDB"

// below is the variable for database version
private val DATABASE_VERSION = 2


val TABLE_USERS = "users"
val COL_USERS_ID = "userID"
val COL_USERS_EMAIL = "email"
val COL_USERS_PASSWORD = "password"

val TABLE_RECIPIENT = "recipient"
val COL_RECIPIENT_ID = "recipientID"
val COL_RECIPIENT_FULLNAME = "fullName"
val COL_RECIPIENT_PRIORITY_GROUP = "priorityGroup"
val COL_RECIPIENT_PHONENUMBER = "phoneNumber"
val COL_RECIPIENT_DATE_TARGET = "dateTarget"
val COL_RECIPIENT_TIME_TARGET = "timeTarget"
val COL_RECIPIENT_MESSAGE_SENT = "messageSent"

class SQLHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        //Initiating Users Table
        val users =
            "CREATE TABLE $TABLE_USERS (" +
                    "$COL_USERS_ID INTEGER PRIMARY KEY, " +
                    "$COL_USERS_EMAIL TEXT NOT NULL," +
                    "$COL_USERS_PASSWORD TEXT NOT NULL)"
        db.execSQL(users)

        //Initiating Todos Table
        val recipient =
            "CREATE TABLE $TABLE_RECIPIENT (" +
                    "$COL_RECIPIENT_ID INTEGER PRIMARY KEY, " +
                    "$COL_RECIPIENT_FULLNAME TEXT NOT NULL," +
                    "$COL_RECIPIENT_PRIORITY_GROUP TEXT NOT NULL, " +
                    "$COL_RECIPIENT_PHONENUMBER TEXT NOT NULL," +
                    "$COL_RECIPIENT_DATE_TARGET TEXT NOT NULL, " +
                    "$COL_RECIPIENT_TIME_TARGET TEXT NOT NULL, " +
                    "$COL_RECIPIENT_MESSAGE_SENT INTEGER NOT NULL)"
        db.execSQL(recipient)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECIPIENT")
        onCreate(db)
    }

    fun isRegistered(email: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USERS_EMAIL = '$email'"
        val result = db.rawQuery(query, null)

        if (result.count <= 0) return false

        return true
    }

    @SuppressLint("Range")
    fun isValidCredentials(email: String, password: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USERS_EMAIL = '$email'"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        if (result.getString(result.getColumnIndex(COL_USERS_PASSWORD)).toString() == password) return true
        return false
    }

    fun register(email: String, password: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_USERS_EMAIL, email)
        contentValues.put(COL_USERS_PASSWORD, password)
        db.insert(TABLE_USERS, null, contentValues)
    }

    @SuppressLint("NewApi")
    fun addRecipient(
        recipientID: Int,
        fullName: String,
        priorityGroup: String,
        phoneNumber: String,
    ) {
        val r = Recipient()
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_RECIPIENT_FULLNAME, fullName)
        contentValues.put(COL_RECIPIENT_PRIORITY_GROUP, priorityGroup)
        contentValues.put(COL_RECIPIENT_PHONENUMBER, phoneNumber)
        contentValues.put(COL_RECIPIENT_DATE_TARGET, r.dateTarget)
        contentValues.put(COL_RECIPIENT_TIME_TARGET, r.timeTarget)
        contentValues.put(COL_RECIPIENT_MESSAGE_SENT, r.messageSent)
        db.insert(TABLE_RECIPIENT, null, contentValues)
        println("addRecipient(): CALLED")
        db.close()
    }

    fun updateRecipient(recipient: Recipient) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_RECIPIENT_FULLNAME, recipient.fullName)
        contentValues.put(COL_RECIPIENT_PRIORITY_GROUP, recipient.priorityGroup)
        contentValues.put(COL_RECIPIENT_PHONENUMBER, recipient.phoneNumber)
        contentValues.put(COL_RECIPIENT_DATE_TARGET, recipient.dateTarget)
        contentValues.put(COL_RECIPIENT_TIME_TARGET, recipient.timeTarget)
        contentValues.put(COL_RECIPIENT_MESSAGE_SENT, recipient.messageSent)
        db.update(TABLE_RECIPIENT, contentValues, "$COL_RECIPIENT_ID = ?", arrayOf(recipient.id.toString()))
        println("Updated recipient details: ${recipient.fullName}")
        db.close()
    }

    fun deleteRecipient(recipient: Recipient): Boolean {
        val db = this.writableDatabase
        return db.delete(TABLE_RECIPIENT, "$COL_RECIPIENT_ID=?", arrayOf(recipient.id.toString())) > 0
    }

    @SuppressLint("Range")
    fun getUserIdFromEmail(email: String): Int {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USERS_EMAIL = '$email'"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        return (result.getInt(result.getColumnIndex(COL_USERS_ID)))
    }

    @SuppressLint("Range")
    fun getRecipient(): ArrayList<Recipient> {
        val list = ArrayList<Recipient>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_RECIPIENT"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val recipient = Recipient()
                recipient.id = result.getInt(result.getColumnIndex(COL_RECIPIENT_ID))
                recipient.fullName = result.getString(result.getColumnIndex(COL_RECIPIENT_FULLNAME)).toString()
                recipient.priorityGroup = result.getString(result.getColumnIndex(COL_RECIPIENT_PRIORITY_GROUP)).toString()
                recipient.phoneNumber = result.getString(result.getColumnIndex(COL_RECIPIENT_PHONENUMBER)).toString()
                recipient.dateTarget = result.getString(result.getColumnIndex(COL_RECIPIENT_DATE_TARGET)).toString()
                recipient.timeTarget = result.getString(result.getColumnIndex(COL_RECIPIENT_TIME_TARGET)).toString()
                recipient.messageSent = result.getInt(result.getColumnIndex(COL_RECIPIENT_MESSAGE_SENT))

                println("RETRIEVING RECIPIENT ${recipient.id}")
                list.add(recipient)
            } while (result.moveToNext())
        }
        db.close()
        return list
    }



}


