package shoppingcart.application

import shoppingcart.application.Offer.*
import shoppingcart.domain.Amount
import shoppingcart.domain.Product

data class ShoppingCartItem(val product: Product, val quantity: Int = 1, val offer: Offer = NO_OFFER) {
    fun subtotal(): Amount {
        return when (offer) {
            TWO_FOR_ONE -> product.price * (quantity / 2 + quantity % 2)
            else -> product.price * quantity
        }
    }

    fun plus(quantity: Int): ShoppingCartItem = ShoppingCartItem(product, this.quantity + quantity)
    fun minus(quantity: Int): ShoppingCartItem = ShoppingCartItem(product, this.quantity - quantity)
}