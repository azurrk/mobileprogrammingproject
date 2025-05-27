package com.example.mobileprogrammingproject.repository


import com.example.mobileprogrammingproject.model.TransactionCategoryEnum
import com.example.mobileprogrammingproject.model.Transaction

interface TransactionRepository: BaseRepository<Transaction> {
    suspend fun getTransactionsForUser(userId: Int): List<Transaction>
    suspend fun getExpensesForUser(userId: Int): List<Transaction>
    suspend fun getIncomesForUser(userId: Int): List<Transaction>
    suspend fun getTransactionsByCategory(userId: Int, category: TransactionCategoryEnum): List<Transaction>
    suspend fun deleteAllForUser(userId: Int)
}