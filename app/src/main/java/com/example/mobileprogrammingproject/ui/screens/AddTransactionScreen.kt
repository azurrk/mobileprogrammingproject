import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mobileprogrammingproject.model.Transaction
import com.example.mobileprogrammingproject.model.TransactionCategoryEnum
import com.example.mobileprogrammingproject.ui.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import com.example.mobileprogrammingproject.ui.viewmodel.UserViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    userViewModel: UserViewModel,
    transactionViewModel: TransactionViewModel,
    onSave: (Transaction) -> Unit,
    onCancel: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf(TransactionCategoryEnum.FOOD) }
    val currentDate = remember { Calendar.getInstance() }

    var showCategoryDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Get categories based on transaction type
    val availableCategories = if (isExpense) {
        TransactionCategoryEnum.values().filter { it != TransactionCategoryEnum.INCOME }
    } else {
        listOf(TransactionCategoryEnum.INCOME)
    }

    // Update selected category when switching transaction type
    LaunchedEffect(isExpense) {
        selectedCategory = if (isExpense) {
            if (selectedCategory == TransactionCategoryEnum.INCOME) {
                TransactionCategoryEnum.FOOD
            } else {
                selectedCategory
            }
        } else {
            TransactionCategoryEnum.INCOME
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val amountValue = amount.toDoubleOrNull()

                            if (amountValue == null || amountValue <= 0) {
                                errorMessage = "Please enter a valid amount greater than 0"
                                showError = true
                                return@IconButton
                            }

                            if (description.trim().isEmpty()) {
                                errorMessage = "Please enter a description"
                                showError = true
                                return@IconButton
                            }

                            val finalAmount = if (isExpense) -amountValue else amountValue

                            val newTransaction = Transaction(
                                amount = finalAmount,
                                description = description.trim(),
                                category = selectedCategory.name,
                                date = currentDate.time,
                                isExpense = isExpense,
                                userId = userViewModel.loggedUser.value?.id ?: 1
                            )


                            transactionViewModel.addTransaction(newTransaction)
                            onSave(newTransaction)
                        },
                        enabled = amount.isNotEmpty() && description.isNotEmpty()
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Transaction type selector (Income/Expense)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = isExpense,
                    onClick = { isExpense = true },
                    label = { Text("Expense") },
                    leadingIcon = {
                        if (isExpense) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                FilterChip(
                    selected = !isExpense,
                    onClick = { isExpense = false },
                    label = { Text("Income") },
                    leadingIcon = {
                        if (!isExpense) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    }
                )
            }

            // Amount field with validation
            OutlinedTextField(
                value = amount,
                onValueChange = { newValue ->
                    // Only allow numbers and decimal point
                    if (newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                        amount = newValue
                        showError = false
                    }
                },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                isError = showError && (amount.toDoubleOrNull() == null || amount.toDoubleOrNull() == 0.0),
                supportingText = {
                    if (showError && (amount.toDoubleOrNull() == null || amount.toDoubleOrNull() == 0.0)) {
                        Text("Enter a valid amount greater than 0")
                    }
                }
            )

            // Description field
            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    showError = false
                },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showError && description.trim().isEmpty(),
                supportingText = {
                    if (showError && description.trim().isEmpty()) {
                        Text("Description is required")
                    }
                }
            )

            // Category selector
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCategoryDialog = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = selectedCategory.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Select category",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Date selector
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
                                .format(currentDate.time),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Change date",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // Category selection dialog
    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Select Category") },
            text = {
                Column {
                    availableCategories.forEach { category ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategory = category
                                    showCategoryDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                                color = if (selectedCategory == category) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                            if (selectedCategory == category) {
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoryDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDate.timeInMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            currentDate.timeInMillis = millis
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Error Snackbar
    if (showError && errorMessage.isNotEmpty()) {
        LaunchedEffect(showError) {
            kotlinx.coroutines.delay(3000)
            showError = false
            errorMessage = ""
        }
    }
}