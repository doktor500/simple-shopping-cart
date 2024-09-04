
package shoppingcart.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AmountSpec : FunSpec({
    test("implements equals") {
        assertTrue { Amount(0.toBigDecimal()) == Amount(0.toBigDecimal()) }

        assertFalse { Amount(0.toBigDecimal()) == Amount(1.toBigDecimal()) }
        assertFalse { Amount(0.toBigDecimal()).equals(1.toBigDecimal()) }
        assertFalse { Amount(0.toBigDecimal()).equals(0) }
    }

    test("calculates hashcode") {
        assertTrue { Amount(0.toBigDecimal()).hashCode() == (Amount(0.toBigDecimal()).hashCode()) }
        assertFalse { Amount(0.toBigDecimal()).hashCode() == (Amount(1.toBigDecimal()).hashCode()) }
    }

    test("rounds amount to two decimal places") {
        assertEquals(amount("1.00"), amount("0.999"))
        assertEquals(amount("0.89"), amount("0.889"))
        assertEquals(amount("0.88"), amount("0.881"))
    }

    context("can add amounts") {
        data class Data(val amount1: Amount, val amount2: Amount, val expectedAmount: Amount)

        withData<Data>(
            { "${it.amount1} + ${it.amount2} is equal to ${it.expectedAmount}" },
            Data(amount1 = amount("0.75"), amount2 = amount("0.75"), expectedAmount = amount("1.50")),
            Data(amount1 = amount("0.37"), amount2 = amount("0.22"), expectedAmount = amount("0.59")),
        ) { (amount1, amount2, expectedAmount) ->
            assertEquals(expectedAmount, amount1.plus(amount2))
        }
    }

    context("can subtract amounts") {
        data class Data(val amount1: Amount, val amount2: Amount, val expectedAmount: Amount)

        withData<Data>(
            { "${it.amount1} - ${it.amount2} is equal to ${it.expectedAmount}" },
            Data(amount1 = amount("0.75"), amount2 = amount("0.75"), expectedAmount = amount("0.0")),
            Data(amount1 = amount("0.37"), amount2 = amount("0.22"), expectedAmount = amount("0.15")),
        ) { (amount1, amount2, expectedAmount) ->
            assertEquals(expectedAmount, amount1.minus(amount2))
        }
    }

    context("can multiple an amount") {
        data class Data(val amount: Amount, val times: Int, val expectedAmount: Amount)

        withData<Data>(
            { "amount ${it.amount} x ${it.times} times is equal to ${it.expectedAmount}" },
            Data(amount = amount("5.25"), times = 7, expectedAmount = amount("36.75")),
            Data(amount = amount("7.63"), times = 3, expectedAmount = amount("22.89")),
        ) { (amount, times, expectedAmount) ->
            assertEquals(expectedAmount, amount.times(times))
        }
    }
})

fun amount(amount: String) = Amount(amount.toBigDecimal())