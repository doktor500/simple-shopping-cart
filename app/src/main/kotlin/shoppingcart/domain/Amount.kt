package shoppingcart.domain

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.math.BigDecimal
import java.math.RoundingMode

data class Amount(private val value: BigDecimal) {
    override fun toString(): String = value.setScale(2, RoundingMode.HALF_UP).toString()
    operator fun plus(amount: Amount): Amount = Amount(value + amount.value)
    operator fun times(times: Amount): Amount = Amount(value.times(times.value))
    operator fun times(times: Int): Amount = Amount(value.times(times.toBigDecimal()))

    override fun hashCode(): Int {
        return HashCodeBuilder().append(this.toString()).toHashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Amount -> EqualsBuilder().append(this.toString(), other.toString()).isEquals
            else -> false
        }
    }
}
