import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.mobileprogrammingproject.model.Transaction
import com.example.mobileprogrammingproject.model.TransactionCategoryEnum
import java.sql.Date
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale




class TransactionPreviewParameterProvider : PreviewParameterProvider<Transaction> {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = dateFormat.parse("2024-01-01")!!
    override val values = sequenceOf(
        Transaction(
            userId = 1 ,
            amount = 250.00,
            description = "Grocery Shopping",
            date = date,
            isExpense = false,
            category = TransactionCategoryEnum.ENTERTAINMENT.toString()
        ),
        Transaction(
            userId = 2,
            amount = -1500.00,
            description = "Salary Deposit",
            date = date,
            isExpense = true,
            category = TransactionCategoryEnum.ENTERTAINMENT.toString()
        )
    )
}
@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance()
    val isExpense = transaction.amount < 0
    val dateFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = onClick,
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
            // Category icon
            val icon = when (transaction.isExpense) {
                true -> Icons.Default.KeyboardArrowDown
                false -> Icons.Default.KeyboardArrowUp
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                color = if (isExpense)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isExpense)
                            MaterialTheme.colorScheme.onErrorContainer
                        else
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Description and category
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.description.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Amount and time
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currencyFormatter.format(transaction.amount),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isExpense) Color(0xFFB71C1C) else Color(0xFF1B5E20),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dateFormatter.format(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


// Preview using PreviewParameter
@Preview(showBackground = true)
@Composable
fun TransactionItemPreview(
    @PreviewParameter(TransactionPreviewParameterProvider::class)
    transaction: Transaction
) {
    MaterialTheme {
        TransactionItem(
            transaction = transaction,
            onClick = { }
        )
    }
}