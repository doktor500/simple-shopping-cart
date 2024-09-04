package shoppingcart.application

import org.junit.jupiter.api.Test
import shoppingcart.domain.Amount
import shoppingcart.domain.Product
import shoppingcart.domain.amount
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShoppingCartTests {
    @ParameterizedTest
    @MethodSource("total")
    fun addProduct(product: Product, expectedTotal: Amount) {
        val shoppingCart = ShoppingCart().add(product, 2)
        assertEquals(expectedTotal, shoppingCart.total())
    }

    @Test
    fun addAndRemoveProductSuccessfully() {
        val product = product(price = amount("4.99"))
        val shoppingCart = ShoppingCart().add(product, 1).remove(product, 1)

        assertTrue { shoppingCart.isRight() }
        shoppingCart.map { cart -> assertEquals(amount("0"), cart.total()) }
    }

    @Test
    fun removeInvalidProduct() {
        val product = product(price = amount("4.99"))
        val shoppingCart = ShoppingCart().remove(product, 1)

        assertTrue { shoppingCart.isLeft() }
        shoppingCart.mapLeft { error ->
            assertEquals("Product not found in the cart", error.message)
        }
    }

    @Test
    fun removeInvalidQuantity() {
        val product = product(price = amount("4.99"))
        val shoppingCart = ShoppingCart().add(product, 1).remove(product, 2)

        assertTrue { shoppingCart.isLeft() }
        shoppingCart.mapLeft { error ->
            assertEquals("Quantity to remove is greater than the quantity in the cart", error.message)
        }
    }

    @Test
    fun applyOffer() {
        val product = product(price = amount("4.99"))
        val shoppingCart = ShoppingCart().add(product, 2).apply(Offer.TWO_FOR_ONE)

        assertEquals(amount("4.99"), shoppingCart.total())
    }

    companion object {
        @JvmStatic
        fun total() = listOf(
            Arguments.of(product(price = amount("4.99")), amount("9.98")),
            Arguments.of(product(price = amount("7.43")), amount("14.86"))
        )

        private fun product(price: Amount) = Product("A product", price)
    }
}