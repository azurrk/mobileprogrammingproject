import android.widget.Space
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    onAddTransaction: () -> Unit,
    onTransactionClick: (String) -> Unit
) {

    val transactions = remember {
        mutableStateListOf(
            Transaction(
                id = "1",
                amount = -45.99,
                description = "Grocery shopping",
                category = "Food",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.time
            ),
            Transaction(
                id = "2",
                amount = 1250.00,
                description = "Salary deposit",
                category = "Income",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -3) }.time
            ),
            Transaction(
                id = "3",
                amount = -35.50,
                description = "Gas station",
                category = "Transportation",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -3) }.time
            ),
            Transaction(
                id = "4",
                amount = -12.99,
                description = "Netflix subscription",
                category = "Entertainment",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -5) }.time
            ),
            Transaction(
                id = "5",
                amount = -85.00,
                description = "Electricity bill",
                category = "Utilities",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time
            )
        )
    }

    // Calculate total balance
    val totalBalance = transactions.sumOf { it.amount }
    val currencyFormatter = NumberFormat.getCurrencyInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactions") },
                actions = {
                    IconButton(onClick = { /* Filter or search functionality */ }) {
                        Icon(Icons.Default.Create, contentDescription = "Filter")
                    }
                    IconButton(onClick = { /* More options */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTransaction,
                icon = { Icon(Icons.Filled.Add, "") },
                text = { Text(text = "Add transaction") },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Balance card at top
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Current Balance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = currencyFormatter.format(totalBalance),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (totalBalance >= 0) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                    )
                }
            }


            if (transactions.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.AccountBox,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No transactions yet",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add your first transaction by clicking the + button",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            } else {
                // Group transactions by date
                val groupedTransactions = transactions.groupBy { transaction ->
                    val calendar = Calendar.getInstance()
                    calendar.time = transaction.date
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    calendar.time
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 88.dp) // Extra padding at bottom for FAB
                ) {
                    groupedTransactions.forEach { (date, transactionsForDate) ->
                        item {
                            DateHeader(date = date)
                        }

                        items(transactionsForDate) { transaction ->
                            TransactionItem(
                                transaction = transaction,
                                onClick = { onTransactionClick(transaction.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateHeader(date: Date) {
    val dateText = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(date)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dateText,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}





data class Transaction(
    val id: String,
    val amount: Double,
    val description: String,
    val category: String,
    val date: Date
)

@Preview(showBackground = true)
@Composable
fun TransactionListScreenPreview() {
    MaterialTheme {
        TransactionScreen(
            onAddTransaction = {},
            onTransactionClick = {}
        )
    }
}