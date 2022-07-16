package shoppingcart

import shoppingcart.presenters.ShoppingCartPresenter
import shoppingcart.application.ShoppingCart
import shoppingcart.domain.Amount
import shoppingcart.domain.Product
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShoppingCartAcceptanceTests {
    @Test
    fun shoppingCartEmpty() {
        assertEquals("Shopping cart is empty!", ShoppingCartPresenter(ShoppingCart()).generateReceipt())
    }

    @Test
    fun shoppingCartWithOneProductAdded() {
        val product = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val shoppingCart = ShoppingCart().add(product)

        assertEquals(
            """
            1 x cornflakes - 2.52 each
            Total = 2.52
            """.trimIndent(),
            ShoppingCartPresenter(shoppingCart).generateReceipt(),
        )
    }

    @Test
    fun shoppingCartWithDifferentProductsAdded() {
        val product1 = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val product2 = Product(name = "bread", price = Amount(9.98.toBigDecimal()))
        val shoppingCart = ShoppingCart().add(product = product1, quantity = 2).add(product = product2, quantity = 1)

        assertEquals(
            """
            2 x cornflakes - 2.52 each
            1 x bread - 9.98 each
            Total = 15.02
            """.trimIndent(),
            ShoppingCartPresenter(shoppingCart).generateReceipt(),
        )
    }

    @Test
    fun shoppingCartWithSameProductAddedMultipleTimes() {
        val product1 = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val product2 = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val shoppingCart = ShoppingCart().add(product = product1).add(product = product2, quantity = 2)

        assertEquals(
            """
            3 x cornflakes - 2.52 each
            Total = 7.56
            """.trimIndent(),
            ShoppingCartPresenter(shoppingCart).generateReceipt()
        )
    }

    @Test
    fun shoppingCartWithAddedAndRemoveProducts() {
        val product1 = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val product2 = Product(name = "bread", price = Amount(9.98.toBigDecimal()))
        val shoppingCart = ShoppingCart()
            .add(product = product1, quantity = 2)
            .add(product = product2, quantity = 2)
            .remove(product = product2, quantity = 1)

        assertTrue { shoppingCart.isRight() }

        shoppingCart.map { cart ->
            assertEquals(
                """
                2 x cornflakes - 2.52 each
                1 x bread - 9.98 each
                Total = 15.02
                """.trimIndent(),
                ShoppingCartPresenter(cart).generateReceipt()
            )
        }
    }
}
