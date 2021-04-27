package com.tuttle80.app.dionysus.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface UserDao {


    @Query("SELECT * FROM UserEntity")
    fun getAll(): List<UserEntity>

    @Insert(onConflict = REPLACE)
    fun insert(user: UserEntity)

    @Query("DELETE from UserEntity")
    fun deleteAll()


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