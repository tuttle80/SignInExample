package com.tuttle80.app.dionysus.db

import android.content.Context
import android.util.Log
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

class AccountRepo {


    private fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
    private fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }

    fun addAccount(context: Context, email: String, password: String) {
        val newAccount = UserEntity()
        newAccount.verifiedType = "email"
        newAccount.dateTime = System.currentTimeMillis()
        newAccount.email = email
        newAccount.password = md5(password).toHex()

        var userDatabase = UserDatabase.getInstance(context)
        userDatabase?.userDao()?.insert(newAccount)
    }

    fun isExistAccount(context: Context, email: String) : Boolean {
        var userDatabase = UserDatabase.getInstance(context)

        val count = userDatabase?.userDao()?.isExistAccount(email) ?: 0
        return count == 1
    }

    fun isValidAccount(context: Context, email: String, password: String) : Boolean {
        // TestCse
//            val newUser = UserEntity()
//            newUser.verifiedType = "email"
//            newUser.dateTime = System.currentTimeMillis()
//            newUser.email = email
//
//            var userDatabase2 = UserDatabase.getInstance(context)
//        userDatabase2?.userDao()?.insert(newUser)

//        return false
//        addAccount(context, email, password)


        var userDatabase = UserDatabase.getInstance(context)
        val pwdMD5 = md5(password).toHex()

        val count = userDatabase?.userDao()?.isValidAccount(email, pwdMD5) ?: 0
        return count == 1
    }
}