package shoppingcart

import io.kotest.core.spec.style.FunSpec
import shoppingcart.application.Offer.TWO_FOR_ONE
import shoppingcart.application.ShoppingCart
import shoppingcart.domain.Amount
import shoppingcart.domain.Product
import shoppingcart.presenters.ShoppingCartPresenter
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShoppingCartAcceptanceSpec : FunSpec({
    test("prints receipt for empty cart") {
        assertEquals("Shopping cart is empty!", ShoppingCartPresenter(ShoppingCart()).generateReceipt())
    }

    test("prints receipt for cart with one product") {
        val product = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val shoppingCart = ShoppingCart().add(product)

        assertEquals(
            """
            1 x cornflakes @ 2.52 each
            Total = 2.52
            """.trimIndent(),
            ShoppingCartPresenter(shoppingCart).generateReceipt(),
        )
    }

    test("prints receipt for cart with different products") {
        val product1 = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val product2 = Product(name = "bread", price = Amount(9.98.toBigDecimal()))
        val shoppingCart = ShoppingCart().add(product = product1, quantity = 2).add(product = product2, quantity = 1)

        assertEquals(
            """
            2 x cornflakes @ 2.52 each
            1 x bread @ 9.98 each
            Total = 15.02
            """.trimIndent(),
            ShoppingCartPresenter(shoppingCart).generateReceipt(),
        )
    }

    test("prints receipt for cart with same product added multiple times") {
        val product1 = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val product2 = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val shoppingCart = ShoppingCart().add(product = product1).add(product = product2, quantity = 2)

        assertEquals(
            """
            3 x cornflakes @ 2.52 each
            Total = 7.56
            """.trimIndent(),
            ShoppingCartPresenter(shoppingCart).generateReceipt()
        )
    }

    test("prints receipt for cart with added and removed products") {
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
                2 x cornflakes @ 2.52 each
                1 x bread @ 9.98 each
                Total = 15.02
                """.trimIndent(),
                ShoppingCartPresenter(cart).generateReceipt()
            )
        }
    }

    test("prints receipt for cart that contains a 'two for one' offer") {
        val product1 = Product(name = "cornflakes", price = Amount(2.52.toBigDecimal()))
        val shoppingCart = ShoppingCart().add(product = product1, quantity = 4).apply(offer = TWO_FOR_ONE)

        assertEquals(
            """
            4 x cornflakes @ 2.52 each - Discount: 5.04
            Total = 5.04
            """.trimIndent(),
            ShoppingCartPresenter(shoppingCart).generateReceipt()
        )
    }
})