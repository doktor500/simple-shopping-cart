package shoppingcart.application

import arrow.core.Either
import arrow.core.Either.*
import shoppingcart.domain.Amount
import shoppingcart.domain.Product

data class ShoppingCart(val items: List<ShoppingCartItem> = emptyList()) {
    fun add(product: Product, quantity: Int = 1): ShoppingCart = ShoppingCart(addProduct(product, quantity))

    fun remove(product: Product, quantity: Int = 1): Either<Error, ShoppingCart> {
        return removeProduct(product, quantity).map { ShoppingCart(it) }
    }

    fun total(): Amount = items.sumOf { item -> item.subtotal() }

    private fun addProduct(product: Product, quantity: Int): List<ShoppingCartItem> {
        return when (val existingItem = findItemBy(product)) {
            null -> items.plus(ShoppingCartItem(product, quantity))
            else -> items.minus(existingItem).plus(existingItem.plus(quantity))
        }
    }

    private fun removeProduct(product: Product, quantity: Int): Either<Error, List<ShoppingCartItem>> {
        return when (val existingItem = findItemBy(product)) {
            null -> Left(Error("Product not found in the cart"))
            else -> removeCartItem(existingItem, quantity)
        }
    }

    private fun removeCartItem(item: ShoppingCartItem, quantity: Int): Either<Error, List<ShoppingCartItem>> {
        return when {
            quantity > item.quantity -> Left(Error("Quantity to remove is greater than the quantity in the cart"))
            quantity == item.quantity -> Right(items.minus(item))
            else -> Right(items.minus(item).plus(item.minus(quantity)))
        }
    }

    private fun findItemBy(product: Product): ShoppingCartItem? = items.find { item -> item.product == product }
}

private inline fun Iterable<ShoppingCartItem>.sumOf(selector: (ShoppingCartItem) -> Amount): Amount {
    return this.map { item -> selector(item) }.fold(Amount(0.0.toBigDecimal())) { sum, amount -> sum + amount }
}