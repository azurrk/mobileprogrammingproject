package com.example.mobileprogrammingproject.database


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters
import com.example.mobileprogrammingproject.dao.TransactionDao

import com.example.mobileprogrammingproject.dao.UserDao;
import com.example.mobileprogrammingproject.model.Transaction
import com.example.mobileprogrammingproject.model.User;

@Database(
    entities = [User::class, Transaction::class],
    version = 3,
)
@TypeConverters(Converters::class)
abstract class AppDatabase :RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
}
