import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobileprogrammingproject.model.Transaction
import com.example.mobileprogrammingproject.ui.viewmodel.TransactionViewModel
import com.example.mobileprogrammingproject.ui.viewmodel.UserViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    transactionViewModel: TransactionViewModel,
    onNavigateToTransactions: () -> Unit = {},
    onAddTransaction: () -> Unit = {},
    onLogout: () -> Unit
) {
    val loggedUser by userViewModel.loggedUser.collectAsState()
    val transactions by transactionViewModel.transactions.collectAsState()
    val isLoading by transactionViewModel.isLoading.collectAsState()
    val currencyFormatter = NumberFormat.getCurrencyInstance()

    // Load transactions when user changes
    LaunchedEffect(loggedUser) {
        loggedUser?.let { user ->
            transactionViewModel.loadTransactions(user.id)
        }
    }

    // Calculate balance and recent transactions
    val totalBalance = transactions.sumOf { it.amount }
    val recentTransactions = transactions.sortedByDescending { it.date }.take(5)
    val expenseData = getWeeklyExpenseData(transactions)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Welcome, ${loggedUser?.fullName ?: "User"}!")
                },
                actions = {
                    IconButton(onClick = { onLogout() }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Balance Card
                item {
                    BalanceCard(
                        balance = totalBalance,
                        currencyFormatter = currencyFormatter
                    )
                }

                // Quick Actions
                item {
                    QuickActionsRow(
                        onAddTransaction = onAddTransaction,
                        onViewAllTransactions = onNavigateToTransactions
                    )
                }

                // Expense Chart
                item {
                    ExpenseChartCard(expenseData = expenseData)
                }

                // Recent Transactions Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Transactions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        if (recentTransactions.isNotEmpty()) {
                            TextButton(onClick = onNavigateToTransactions) {
                                Text("View All")
                            }
                        }
                    }
                }

                // Recent Transactions List
                if (recentTransactions.isEmpty()) {
                    item {
                        EmptyTransactionsCard(onAddTransaction = onAddTransaction)
                    }
                } else {
                    items(recentTransactions) { transaction ->
                        TransactionItem(transaction = transaction)
                    }
                }
            }
        }
    }
}

@Composable
fun BalanceCard(
    balance: Double,
    currencyFormatter: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currencyFormatter.format(balance),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = if (balance >= 0) Color(0xFF1B5E20) else Color(0xFFB71C1C)
            )
        }
    }
}

@Composable
fun QuickActionsRow(
    onAddTransaction: () -> Unit,
    onViewAllTransactions: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionButton(
                    icon = Icons.Default.Add,
                    text = "Add Transaction",
                    onClick = onAddTransaction
                )
                QuickActionButton(
                    icon = Icons.Default.List,
                    text = "All Transactions",
                    onClick = onViewAllTransactions
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.width(140.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ExpenseChartCard(expenseData: List<Pair<String, Double>>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Weekly Expenses",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            SimpleBarChart(data = expenseData)
        }
    }
}

@Composable
fun SimpleBarChart(data: List<Pair<String, Double>>) {
    val maxValue = data.maxOfOrNull { kotlin.math.abs(it.second) } ?: 1.0

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        data.forEach { (day, amount) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day,
                    modifier = Modifier.width(40.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(8.dp))

                val barWidth = if (maxValue > 0) (kotlin.math.abs(amount) / maxValue).toFloat() else 0f

                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth(barWidth.coerceIn(0f, 1f))
                        .background(
                            if (amount < 0) Color(0xFFEF5350) else Color(0xFF66BB6A),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun EmptyTransactionsCard(onAddTransaction: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No transactions yet",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Start tracking your finances by adding your first transaction",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAddTransaction) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Transaction")
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val currencyFormatter = NumberFormat.getCurrencyInstance()
    val dateFormatter = SimpleDateFormat("MMM d", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Icon(
                imageVector = if (transaction.isExpense) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Transaction details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${transaction.category} • ${dateFormatter.format(transaction.date)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Amount
            Text(
                text = currencyFormatter.format(transaction.amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (transaction.amount >= 0) Color(0xFF1B5E20) else Color(0xFFB71C1C)
            )
        }
    }
}


fun getWeeklyExpenseData(transactions: List<com.example.mobileprogrammingproject.model.Transaction>): List<Pair<String, Double>> {
    val calendar = Calendar.getInstance()
    val weekData = mutableMapOf<String, Double>()

    // Initialize with last 7 days
    for (i in 6 downTo 0) {
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, -i)
        val dayName = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
        weekData[dayName] = 0.0
    }

    // Add transaction data
    transactions.forEach { transaction ->
        calendar.time = transaction.date
        val dayName = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
        if (weekData.containsKey(dayName)) {
            weekData[dayName] = weekData[dayName]!! + transaction.amount
        }
    }

    return weekData.toList()
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        // Preview with mock data - you'd need to provide mock ViewModels
    }
}