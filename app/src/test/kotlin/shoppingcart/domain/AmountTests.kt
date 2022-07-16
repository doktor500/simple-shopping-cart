package shoppingcart.domain

import kotlin.test.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AmountTests {
    @Test fun equals() {
        assertTrue { Amount(0.toBigDecimal()) == Amount(0.toBigDecimal()) }
        assertFalse { Amount(0.toBigDecimal()) == Amount(1.toBigDecimal()) }
        assertFalse { Amount(0.toBigDecimal()).equals(1.toBigDecimal()) }
        assertFalse { Amount(0.toBigDecimal()).equals(0) }
    }

    @Test fun hashcode() {
        assertTrue { Amount(0.toBigDecimal()).hashCode() == (Amount(0.toBigDecimal()).hashCode()) }
        assertFalse { Amount(0.toBigDecimal()).hashCode() == (Amount(1.toBigDecimal()).hashCode()) }
    }

    @ParameterizedTest
    @MethodSource("roundsAmount")
    fun roundsAmount(amount: Amount, expectedAmount: Amount) {
        assertEquals(expectedAmount, amount)
    }

    @ParameterizedTest
    @MethodSource("addsAmount")
    fun addsAmount(amount: Amount, amountToAdd: Amount, expectedAmount: Amount) {
        assertEquals(expectedAmount, amount.plus(amountToAdd))
    }

    @ParameterizedTest
    @MethodSource("multipliesAmounts")
    fun multipliesAmounts(amount: Amount, amountToMultiplyBy: Amount, expectedAmount: Amount) {
        assertEquals(expectedAmount, amount.times(amountToMultiplyBy))
    }

    @ParameterizedTest
    @MethodSource("multipliesAmountNTimes")
    fun multipliesAmountNTimes(amount: Amount, times: Int, expectedAmount: Amount) {
        assertEquals(expectedAmount, amount.times(times))
    }

    companion object {
        @JvmStatic
        fun roundsAmount() = listOf(
            Arguments.of(amount("0.999"), amount("1.00")),
            Arguments.of(amount("0.889"), amount("0.89")),
            Arguments.of(amount("0.881"), amount("0.88"))
        )

        @JvmStatic
        fun addsAmount() = listOf(
            Arguments.of(amount("0.75"), amount("0.75"), amount("1.50")),
            Arguments.of(amount("0.37"), amount("0.22"), amount("0.59"))
        )

        @JvmStatic
        fun multipliesAmounts() = listOf(
            Arguments.of(amount("5.25"), amount("0.75"), amount("3.94")),
            Arguments.of(amount("7.63"), amount("1.47"), amount("11.22")),
        )

        @JvmStatic
        fun multipliesAmountNTimes() = listOf(
            Arguments.of(amount("5.25"), 7, amount("36.75")),
            Arguments.of(amount("7.63"), 3, amount("22.89")),
        )
    }
}

fun amount(amount: String) = Amount(amount.toBigDecimal())