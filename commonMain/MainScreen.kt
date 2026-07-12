package com.max.cannabis

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    var flowerGrams by remember { mutableStateOf(7.0) }
    var extractGrams by remember { mutableStateOf(1.0) }
    var flowerUsesPerDay by remember { mutableStateOf(2.0) }
    var extractUsesPerDay by remember { mutableStateOf(1.0) }
    var daysUntilPaycheck by remember { mutableStateOf(14) }

    var moneySpent by remember { mutableStateOf(100.0) }
    var costPerGramFlower by remember { mutableStateOf(10.0) }
    var costPerGramExtract by remember { mutableStateOf(20.0) }
    var budgetUntilPaycheck by remember { mutableStateOf(150.0) }

    val stash = Stash(flowerGrams, extractGrams)
    val pattern = UsePattern(flowerUsesPerDay, extractUsesPerDay)
    val money = Money(moneySpent, costPerGramFlower, costPerGramExtract, budgetUntilPaycheck)

    val result = RationEngine.computePlan(stash, pattern, money, daysUntilPaycheck)

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Cannabis Moderation Planner", style = MaterialTheme.typography.h5)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberField("Flower g", flowerGrams) { flowerGrams = it }
                NumberField("Extract g", extractGrams) { extractGrams = it }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberField("Flower uses/day", flowerUsesPerDay) { flowerUsesPerDay = it }
                NumberField("Extract uses/day", extractUsesPerDay) { extractUsesPerDay = it }
            }

            NumberField("Days until paycheck", daysUntilPaycheck.toDouble()) {
                daysUntilPaycheck = it.toInt()
            }

            Divider()

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberField("Money spent", moneySpent) { moneySpent = it }
                NumberField("Budget until paycheck", budgetUntilPaycheck) { budgetUntilPaycheck = it }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberField("Cost/g flower", costPerGramFlower) { costPerGramFlower = it }
                NumberField("Cost/g extract", costPerGramExtract) { costPerGramExtract = it }
            }

            Divider()

            LiveResultsPanel(result, daysUntilPaycheck)
        }
    }
}

@Composable
fun NumberField(label: String, value: Double, onChange: (Double) -> Unit) {
    TextField(
        value = value.toString(),
        onValueChange = { s -> s.toDoubleOrNull()?.let(onChange) },
        label = { Text(label) },
        modifier = Modifier.width(160.dp)
    )
}

@Composable
fun LiveResultsPanel(result: PlanResult, daysUntilPaycheck: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Flower lasts: ${"%.1f".format(result.flowerDays)} days")
        Text("Extract lasts: ${"%.1f".format(result.extractDays)} days")
        Text("Overall sustainable: ${"%.1f".format(result.totalDays)} days")

        if (result.coversPaycheck)
            Text("✔ Safe until payday ($daysUntilPaycheck days)")
        else
            Text("⚠ Runs out around day ${"%.1f".format(result.totalDays)}")

        Text("Daily cost: $${"%.2f".format(result.dailyCost)}")
        Text("Total cost for duration: $${"%.2f".format(result.totalCostForDuration)}")

        if (result.staysWithinBudget)
            Text("✔ Within budget")
        else
            Text("⚠ Over budget by $${"%.2f".format(result.totalCostForDuration - result.staysWithinBudget)}")
    }
}
