package com.max.cannabis

enum class FlowerPackage(val grams: Double) {
    ONE_GRAM(1.0),
    EIGHTH(3.5),
    QUARTER_OUNCE(7.0),
    HALF_OUNCE(14.0),
    OUNCE(28.0)
}

data class Stash(
    val flowerGrams: Double,
    val extractGrams: Double
)

data class UsePattern(
    val flowerUsesPerDay: Double,
    val extractUsesPerDay: Double
)

data class Money(
    val moneySpent: Double,
    val costPerGramFlower: Double,
    val costPerGramExtract: Double,
    val budgetUntilPaycheck: Double
)

data class PlanResult(
    val flowerServings: Double,
    val extractServings: Double,
    val flowerDays: Double,
    val extractDays: Double,
    val totalDays: Double,
    val coversPaycheck: Boolean,
    val costFlowerTotal: Double,
    val costExtractTotal: Double,
    val costPerFlowerServing: Double,
    val costPerExtractServing: Double,
    val dailyCost: Double,
    val totalCostForDuration: Double,
    val staysWithinBudget: Boolean
)
