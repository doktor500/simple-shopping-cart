package shoppingcart.application

import shoppingcart.domain.Amount
import shoppingcart.domain.Product
import shoppingcart.domain.amount
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class ShoppingCartItemTests {
    @ParameterizedTest
    @MethodSource("subtotal")
    fun calculateSubtotal(quantity: Int, product: Product, expectedSubtotal: Amount) {
        val shoppingCartItem = ShoppingCartItem(product, quantity)
        assertEquals(expectedSubtotal, shoppingCartItem.subtotal())
    }

    @ParameterizedTest
    @MethodSource("addQuantity")
    fun addQuantity(product: Product, quantity: Int, addedQuantity: Int, expectedQuantity: Int) {
        val shoppingCartItem = ShoppingCartItem(product, quantity)
        assertEquals(expectedQuantity, shoppingCartItem.plus(addedQuantity).quantity)
    }

    @ParameterizedTest
    @MethodSource("removeQuantity")
    fun removeQuantity(product: Product, quantity: Int, removedQuantity: Int, expectedQuantity: Int) {
        val shoppingCartItem = ShoppingCartItem(product, quantity)
        assertEquals(expectedQuantity, shoppingCartItem.minus(removedQuantity).quantity)
    }

    companion object {
        @JvmStatic
        fun subtotal() = listOf(
            Arguments.of(2, Product("cornflakes", amount("2.52")), amount("5.04")),
            Arguments.of(3, Product("bread", amount("9.98")), amount("29.94"))
        )

        @JvmStatic
        fun addQuantity() = listOf(
            Arguments.of(Product("cornflakes", amount("2.52")), 1, 1, 2),
            Arguments.of(Product("bread", amount("9.98")), 3, 4, 7)
        )

        @JvmStatic
        fun removeQuantity() = listOf(
            Arguments.of(Product("cornflakes", amount("2.52")), 1, 1, 0),
            Arguments.of(Product("bread", amount("9.98")), 4, 3, 1)
        )
    }
}