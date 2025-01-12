package com.example.doggame.datamanager

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "user", indices = [Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "bestScore") val bestScore: Int?,
    @ColumnInfo(name = "avatarImage") val avatarImage: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (uid != other.uid) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (bestScore != other.bestScore) return false
        if (avatarImage != null) {
            if (other.avatarImage == null) return false
            if (!avatarImage.contentEquals(other.avatarImage)) return false
        } else if (other.avatarImage != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + (bestScore ?: 0)
        result = 31 * result + (avatarImage?.contentHashCode() ?: 0)
        return result
    }
}
