package id.bpdlh.fdb.core.common.utils

import java.io.Serializable

/**
 * Created by hahn on 27/10/23.
 * Project: BPDLH App
 */

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) : Serializable {

    /**
     * Returns string representation of the [Quadruple] including its [first], [second], [third], and [fourth] values.
     */
    override fun toString(): String = "($first, $second, $third, $fourth)"
}