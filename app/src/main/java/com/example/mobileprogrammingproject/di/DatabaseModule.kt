package com.example.mobileprogrammingproject.di


import android.content.Context
import androidx.room.Room
import com.example.mobileprogrammingproject.dao.UserDao
import com.example.mobileprogrammingproject.database.AppDatabase
import com.example.mobileprogrammingproject.repository.UserRepository
import com.example.mobileprogrammingproject.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "lab_exam.db",
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun providesUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()


    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao
    ): UserRepository = UserRepositoryImpl(userDao)

}