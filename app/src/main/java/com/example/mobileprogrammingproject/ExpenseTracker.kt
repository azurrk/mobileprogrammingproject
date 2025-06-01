package com.example.mobileprogrammingproject

import android.app.Application
import android.util.Log
import com.example.mobileprogrammingproject.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ExpenseTracker: Application() {
    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            //Test you can run here
            //dummy query on database just to open the connection
            database.userDao().getUserById(1)

            /**
             * Testing workout with categories -- works
             */


        }
    }
}