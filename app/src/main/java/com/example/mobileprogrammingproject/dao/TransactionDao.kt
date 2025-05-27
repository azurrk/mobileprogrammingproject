package com.example.mobileprogrammingproject.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mobileprogrammingproject.model.Transaction
import com.example.mobileprogrammingproject.model.TransactionCategoryEnum

@Dao
interface TransactionDao : BaseDao<Transaction> {

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    suspend fun getTransactionsForUser(userId: Int): List<Transaction>

    @Query("SELECT * FROM transactions WHERE userId = :userId AND isExpense = 1 ORDER BY date DESC")
    suspend fun getExpensesForUser(userId: Int): List<Transaction>

    @Query("SELECT * FROM transactions WHERE userId = :userId AND isExpense = 0 ORDER BY date DESC")
    suspend fun getIncomesForUser(userId: Int): List<Transaction>

    @Query("SELECT * FROM transactions WHERE userId = :userId AND category = :category ORDER BY date DESC")
    suspend fun getTransactionsByCategory(userId: Int, category: TransactionCategoryEnum): List<Transaction>

    @Query("DELETE FROM transactions WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Int)
}
