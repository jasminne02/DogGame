package com.example.doggame.datamanager

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDAO {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE username LIKE :username LIMIT 1")
    fun findByUsername(username: String): User

    @Query("UPDATE user SET username = :newUsername WHERE username LIKE :username")
    fun updateUserUsername(username: String, newUsername: String)

    @Query("UPDATE user SET password = :password WHERE username LIKE :username")
    fun updateUserPassword(username: String, password: String)

    @Query("UPDATE user SET avatarImage = :avatarImage WHERE username LIKE :username")
    fun updateUserAvatar(username: String, avatarImage: ByteArray)

    @Query("DELETE FROM user WHERE username = :username")
    fun deleteUser(username: String)

    @Update
    fun update(user: User)

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}
