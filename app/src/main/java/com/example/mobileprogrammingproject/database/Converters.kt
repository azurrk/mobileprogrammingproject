package com.example.mobileprogrammingproject.database

import androidx.room.TypeConverter
import com.example.mobileprogrammingproject.model.TransactionCategoryEnum
import java.util.Date

class Converters {

    // Date converters
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // TransactionCategoryEnum converters
    @TypeConverter
    fun fromTransactionCategory(category: TransactionCategoryEnum): String {
        return category.name
    }

    @TypeConverter
    fun toTransactionCategory(categoryString: String): TransactionCategoryEnum {
        return TransactionCategoryEnum.valueOf(categoryString)
    }
}