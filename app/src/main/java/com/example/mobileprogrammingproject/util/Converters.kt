package com.example.mobileprogrammingproject.util

import androidx.room.TypeConverter
import com.example.mobileprogrammingproject.model.TransactionCategoryEnum
import java.util.Date


// used to support category enum and date in room database
class Converters {

    @TypeConverter
    fun fromCategory(category: TransactionCategoryEnum): String = category.name

    @TypeConverter
    fun toCategory(value: String): TransactionCategoryEnum = TransactionCategoryEnum.valueOf(value)

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(timestamp: Long): Date = Date(timestamp)
}