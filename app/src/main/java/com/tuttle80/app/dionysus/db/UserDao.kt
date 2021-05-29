package com.tuttle80.app.dionysus.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query


@Dao
interface UserDao {


    @Query("SELECT COUNT(*) FROM UserEntity")
    fun getCount(): LiveData<Int>

    @Insert(onConflict = REPLACE)
    fun insert(user: UserEntity)

    @Query("DELETE from UserEntity")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM UserEntity WHERE email LIKE (:email) AND password LIKE (:pwdMd5)")
    fun isValidAccount(email: String, pwdMd5: String) : Int

    @Query("SELECT COUNT(*) FROM UserEntity WHERE email LIKE (:email)")
    fun isExistAccount(email: String) : Int
    //


//    @Query("SELECT * FROM " + StudentEntry.TABLE_NAME)
//    fun getAllStudents(): LiveData<List<PhStudentEntity?>?>?

//    @Dao
//    interface PhStudentDao {
//        @get:Query("SELECT * FROM " + StudentEntry.TABLE_NAME)
//        val allStudents: List<Any?>?
//    }


//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User
//
//    @Insert
//    fun insertAll(vararg users: User)
//
//    @Delete
//    fun delete(user: User)

}