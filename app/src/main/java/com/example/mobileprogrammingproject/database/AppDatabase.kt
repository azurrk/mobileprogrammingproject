package com.example.mobileprogrammingproject.database


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mobileprogrammingproject.dao.UserDao;
import com.example.mobileprogrammingproject.model.User;

@Database(
    entities = [User::class],
    version = 3,
)
abstract class AppDatabase :RoomDatabase(){
    abstract fun userDao(): UserDao
}
