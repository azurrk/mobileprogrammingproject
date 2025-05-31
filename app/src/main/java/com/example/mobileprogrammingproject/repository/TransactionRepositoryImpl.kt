package com.example.mobileprogrammingproject.repository

import com.example.mobileprogrammingproject.dao.TransactionDao
import com.example.mobileprogrammingproject.model.Transaction
import com.example.mobileprogrammingproject.model.TransactionCategoryEnum
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao): TransactionRepository{
    override suspend fun getTransactionsForUser(userId: Int): List<Transaction> {
        return transactionDao.getTransactionsForUser(userId)
    }

    override suspend fun getExpensesForUser(userId: Int): List<Transaction> {
        return transactionDao.getExpensesForUser(userId)
    }

    override suspend fun getIncomesForUser(userId: Int): List<Transaction> {
        return transactionDao.getIncomesForUser(userId)
    }

    override suspend fun getTransactionsByCategory(userId: Int, category: TransactionCategoryEnum): List<Transaction> {
        return transactionDao.getTransactionsByCategory(userId, category)
    }

    override suspend fun insert(entity: Transaction){
        return transactionDao.insert(entity)
    }

    override suspend fun update(entity: Transaction) {
        transactionDao.update(entity)
    }

    override suspend fun delete(entity: Transaction) {
        transactionDao.delete(entity)
    }

    override suspend fun deleteAllForUser(userId: Int) {
        transactionDao.deleteAllForUser(userId)
    }
}