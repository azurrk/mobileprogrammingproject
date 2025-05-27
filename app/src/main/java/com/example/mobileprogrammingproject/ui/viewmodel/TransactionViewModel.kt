package com.example.mobileprogrammingproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileprogrammingproject.model.Transaction
import com.example.mobileprogrammingproject.model.TransactionCategoryEnum
import com.example.mobileprogrammingproject.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> get() = _transactions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun loadTransactions(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _transactions.value = transactionRepository.getTransactionsForUser(userId)
            _isLoading.value = false
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.insert(transaction)
            loadTransactions(transaction.userId)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.update(transaction)
            loadTransactions(transaction.userId)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.delete(transaction)
            loadTransactions(transaction.userId)
        }
    }

    fun loadByCategory(userId: Int, category: TransactionCategoryEnum) {
        viewModelScope.launch {
            _isLoading.value = true
            _transactions.value = transactionRepository.getTransactionsByCategory(userId, category)
            _isLoading.value = false
        }
    }

    fun loadExpenses(userId: Int) {
        viewModelScope.launch {
            _transactions.value = transactionRepository.getExpensesForUser(userId)
        }
    }

    fun loadIncomes(userId: Int) {
        viewModelScope.launch {
            _transactions.value = transactionRepository.getIncomesForUser(userId)
        }
    }
}