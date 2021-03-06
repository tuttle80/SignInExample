package com.tuttle80.app.dionysus.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//@Entity(tableName = "user")
@Entity
class UserEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "VerifiedType") var verifiedType: String?,
    @ColumnInfo(name = "DateTime") var dateTime: Long,
    @ColumnInfo(name = "EMail") var email: String,
    @ColumnInfo(name = "Password") var password: String
){
    constructor(): this(null,"", 0,"", "")
}