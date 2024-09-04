package shoppingcart.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import kotlin.test.assertEquals

class ShoppingCartItemSpec : FunSpec({

    context("shopping cart item") {
        context("subtotal") {
            data class Data(val product: Product, val quantity: Int, val subtotal: Amount)

            withData<Data>(
                { "subtotal is ${it.subtotal} when it contains a product with price ${it.product.price} and quantity ${it.quantity}" },
                Data(product = aProduct(price = amount("4.99")), quantity = 2, subtotal = amount("9.98")),
                Data(product = aProduct(price = amount("7.43")), quantity = 2, subtotal = amount("14.86")),
                Data(product = aProduct(price = amount("4.99")), quantity = 3, subtotal = amount("14.97"))
            ) { (product, quantity, subtotal) ->
                val shoppingCartItem = ShoppingCartItem(product, quantity)
                assertEquals(subtotal, shoppingCartItem.subtotal())
            }
        }

        context("quantity increases") {
            data class Data(val quantity: Int, val increasedQuantity: Int, val expectedQuantity: Int)

            withData<Data>(
                { "quantity increases from ${it.quantity} to ${it.expectedQuantity} when adding quantity ${it.increasedQuantity}" },
                Data(quantity = 1, increasedQuantity = 1, expectedQuantity = 2),
                Data(quantity = 1, increasedQuantity = 2, expectedQuantity = 3),
                Data(quantity = 2, increasedQuantity = 2, expectedQuantity = 4),
            ) { (quantity, increasedQuantity, expectedQuantity) ->
                val shoppingCartItem = ShoppingCartItem(aProduct(), quantity)
                assertEquals(expectedQuantity, shoppingCartItem.plus(increasedQuantity).quantity)
            }
        }

        context("quantity decreases") {
            data class Data(val quantity: Int, val decreasedQuantity: Int, val expectedQuantity: Int)

            withData<Data>(
                { "quantity decreases from ${it.quantity} to ${it.expectedQuantity} when subtracting quantity ${it.decreasedQuantity}" },
                Data(quantity = 1, decreasedQuantity = 1, expectedQuantity = 0),
                Data(quantity = 2, decreasedQuantity = 1, expectedQuantity = 1),
                Data(quantity = 4, decreasedQuantity = 2, expectedQuantity = 2),
            ) { (quantity, decreasedQuantity, expectedQuantity) ->
                val shoppingCartItem = ShoppingCartItem(aProduct(), quantity)
                assertEquals(expectedQuantity, shoppingCartItem.minus(decreasedQuantity).quantity)
            }
        }

        context("applies 'two for one' offer") {
            data class Data(val price: Amount, val quantity: Int, val subtotal: Amount)

            withData<Data>(
                { "price is ${it.price}, quantity is ${it.quantity} and subtotal is ${it.subtotal} when 'two for one' offer is applied" },
                Data(price = amount("2.52"), quantity = 1, subtotal = amount("2.52")),
                Data(price = amount("2.52"), quantity = 2, subtotal = amount("2.52")),
                Data(price = amount("2.52"), quantity = 3, subtotal = amount("5.04")),
                Data(price = amount("2.52"), quantity = 4, subtotal = amount("5.04")),
                Data(price = amount("2.52"), quantity = 5, subtotal = amount("7.56")),
            ) { (price, quantity, subtotal) ->
                val shoppingCartItem = ShoppingCartItem(aProduct(price = price), quantity, Offer.TWO_FOR_ONE)
                assertEquals(subtotal, shoppingCartItem.subtotal())
            }
        }
    }
})
