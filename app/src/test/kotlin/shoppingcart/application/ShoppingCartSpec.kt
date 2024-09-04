package shoppingcart.application

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import shoppingcart.domain.Amount
import shoppingcart.domain.Product
import shoppingcart.domain.amount
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShoppingCartSpec : FunSpec({
    data class Data(val product: Product, val quantity: Int, val total: Amount)

    context("shopping cart") {
        withData<Data>(
            { "having a product with price ${it.product.price} and quantity ${it.quantity} total is ${it.total}" },
            Data(product = aProduct(price = amount("4.99")), quantity = 2, total = amount("9.98")),
            Data(product = aProduct(price = amount("7.43")), quantity = 2, total = amount("14.86")),
            Data(product = aProduct(price = amount("4.99")), quantity = 3, total = amount("14.97"))
        ) { (product, quantity, total) ->
            ShoppingCart().add(product, quantity).total() shouldBe total
        }

        test("can add and remove a product successfully") {
            val product = aProduct(price = amount("4.99"))
            val shoppingCart = ShoppingCart().add(product, 1).remove(product, 1)

            assertTrue { shoppingCart.isRight() }
            shoppingCart.map { cart -> assertEquals(amount("0"), cart.total()) }
        }

        test("returns an error when attempting to remove an invalid product") {
            val shoppingCart = ShoppingCart().remove(aProduct(), 1)

            assertTrue { shoppingCart.isLeft() }
            shoppingCart.mapLeft { error ->
                assertEquals("Product not found in the cart", error.message)
            }
        }

        test("returns an error when trying to remove a quantity greater than the quantity of the product in the cart") {
            val product = aProduct(price = amount("4.99"))
            val shoppingCart = ShoppingCart().add(product, 1).remove(product, 2)

            assertTrue { shoppingCart.isLeft() }
            shoppingCart.mapLeft { error ->
                assertEquals("Quantity to remove is greater than the quantity in the cart", error.message)
            }
        }

        test("can apply a 'two for one' offer") {
            val product = aProduct(price = amount("4.99"))
            val shoppingCart = ShoppingCart().add(product, 2).apply(Offer.TWO_FOR_ONE)

            assertEquals(amount("4.99"), shoppingCart.total())
        }
    }
})

fun aProduct(price: Amount = amount("4.99")) = Product("A product", price)
