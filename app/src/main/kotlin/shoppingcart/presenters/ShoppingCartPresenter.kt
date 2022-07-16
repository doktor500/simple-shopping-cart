package shoppingcart.presenters

import shoppingcart.application.ShoppingCart
import shoppingcart.domain.Product

class ShoppingCartPresenter(private val shoppingCart: ShoppingCart) {
    fun generateReceipt(): String {
        return when {
            items.isEmpty() -> "Shopping cart is empty!"
            else -> summary().joinToString(System.lineSeparator())
        }
    }

    private val items get() = shoppingCart.items

    private fun summary(): List<String> = items().plus(total())
    private fun total(): String = "Total = ${shoppingCart.total()}"
    private fun items(): List<String> = items.map { "${it.quantity} x ${ProductPresenter(it.product).present()}" }
}

private class ProductPresenter(private val product: Product) {
    fun present(): String = "${product.name} - ${product.price} each"
}