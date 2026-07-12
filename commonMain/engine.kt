package com.max.cannabis

object RationEngine {

    private const val FLOWER_SERVING_GRAMS = 0.5
    private const val EXTRACT_SERVING_GRAMS = 0.1

    fun computePlan(
        stash: Stash,
        pattern: UsePattern,
        money: Money,
        daysUntilPaycheck: Int
    ): PlanResult {

        val flowerServings = stash.flowerGrams / FLOWER_SERVING_GRAMS
        val extractServings = stash.extractGrams / EXTRACT_SERVING_GRAMS

        val flowerDays = if (pattern.flowerUsesPerDay > 0)
            flowerServings / pattern.flowerUsesPerDay
        else Double.POSITIVE_INFINITY

        val extractDays = if (pattern.extractUsesPerDay > 0)
            extractServings / pattern.extractUsesPerDay
        else Double.POSITIVE_INFINITY

        val totalDays = minOf(flowerDays, extractDays)
        val coversPaycheck = totalDays >= daysUntilPaycheck

        val costFlowerTotal = stash.flowerGrams * money.costPerGramFlower
        val costExtractTotal = stash.extractGrams * money.costPerGramExtract

        val costPerFlowerServing = money.costPerGramFlower * FLOWER_SERVING_GRAMS
        val costPerExtractServing = money.costPerGramExtract * EXTRACT_SERVING_GRAMS

        val dailyCost =
            (pattern.flowerUsesPerDay * costPerFlowerServing) +
            (pattern.extractUsesPerDay * costPerExtractServing)

        val totalCostForDuration = dailyCost * totalDays
        val staysWithinBudget = totalCostForDuration <= money.budgetUntilPaycheck

        return PlanResult(
            flowerServings,
            extractServings,
            flowerDays,
            extractDays,
            totalDays,
            coversPaycheck,
            costFlowerTotal,
            costExtractTotal,
            costPerFlowerServing,
            costPerExtractServing,
            dailyCost,
            totalCostForDuration,
            staysWithinBudget
        )
    }
}
