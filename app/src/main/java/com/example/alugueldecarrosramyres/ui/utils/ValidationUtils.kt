package com.example.alugueldecarrosramyres.ui.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object ValidationUtils {
    private val platePattern = "^[A-Z]{3}-?[0-9][A-Z0-9][0-9]{2}$".toRegex(RegexOption.IGNORE_CASE)
    private val localePtBR = Locale.forLanguageTag("pt-BR")

    fun isValidPlate(plate: String): Boolean {
        return platePattern.matches(plate.trim())
    }

    fun isValidDailyRate(rateStr: String): Boolean {
        val clean = rateStr.replace(",", ".").trim()
        val rate = clean.toDoubleOrNull()
        return rate != null && rate > 0.0
    }

    fun calculateDays(saidaTimestamp: Long, entregaTimestamp: Long): Int {
        val diffMillis = entregaTimestamp - saidaTimestamp
        val days = diffMillis / (1000L * 60L * 60L * 24L)
        val intDays = days.toInt()
        return if (intDays < 1) 1 else intDays
    }

    fun calculateRemainingDays(entregaTimestamp: Long): Int {
        val todayCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val entregaCal = Calendar.getInstance().apply {
            timeInMillis = entregaTimestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val diffMillis = entregaCal.timeInMillis - todayCal.timeInMillis
        return (diffMillis / (1000L * 60L * 60L * 24L)).toInt()
    }

    fun formatDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", localePtBR)
        return formatter.format(Date(timestamp))
    }

    fun formatCurrency(value: Double): String {
        val format = NumberFormat.getCurrencyInstance(localePtBR)
        return format.format(value)
    }
}
