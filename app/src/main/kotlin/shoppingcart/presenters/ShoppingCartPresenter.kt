package shoppingcart.presenters

import shoppingcart.domain.Offer.*
import shoppingcart.domain.ShoppingCart
import shoppingcart.domain.ShoppingCartItem
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
    private fun items(): List<String> = items.map { "${it.quantity} x ${ShoppingCartItemPresenter(it).present()}" }
}

private class ShoppingCartItemPresenter(private val shoppingCartItem: ShoppingCartItem) {
    fun present(): String {
        return ProductPresenter(shoppingCartItem.product).present() + DiscountPresenter(shoppingCartItem).present()
    }
}

private class ProductPresenter(private val product: Product) {
    fun present(): String = "${product.name} @ ${product.price} each"
}

private class DiscountPresenter(private val cartItem: ShoppingCartItem) {
    val originalPrice = cartItem.copy(offer = NO_OFFER).subtotal()

    fun present(): String {
        if (cartItem.offer == TWO_FOR_ONE) return " - Discount: ${originalPrice - cartItem.subtotal()}"
        return ""
    }
}