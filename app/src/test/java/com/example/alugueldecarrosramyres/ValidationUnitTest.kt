package com.example.alugueldecarrosramyres

import com.example.alugueldecarrosramyres.ui.utils.ValidationUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationUnitTest {

    @Test
    fun testPlateValidation() {
        assertTrue(ValidationUtils.isValidPlate("ABC-1234"))
        assertTrue(ValidationUtils.isValidPlate("abc1234"))
        assertTrue(ValidationUtils.isValidPlate("ABC1D23"))
        assertTrue(ValidationUtils.isValidPlate("abc1d23"))
        assertFalse(ValidationUtils.isValidPlate("AB-1234"))
        assertFalse(ValidationUtils.isValidPlate("ABCD-123"))
        assertFalse(ValidationUtils.isValidPlate(""))
    }

    @Test
    fun testDailyRateValidation() {
        assertTrue(ValidationUtils.isValidDailyRate("150.00"))
        assertTrue(ValidationUtils.isValidDailyRate("150,00"))
        assertTrue(ValidationUtils.isValidDailyRate("50"))
        assertFalse(ValidationUtils.isValidDailyRate("0"))
        assertFalse(ValidationUtils.isValidDailyRate("-10"))
        assertFalse(ValidationUtils.isValidDailyRate("abc"))
    }

    @Test
    fun testDaysCalculation() {
        val start = 1700000000000L
        val end = start + (1000L * 60L * 60L * 24L * 5L)
        val days = ValidationUtils.calculateDays(start, end)
        assertEquals(5, days)
    }
}
