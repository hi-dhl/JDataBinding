package com.hi.dhl.jdatabinding

/**
 * <pre>
 *     author: dhl
 *     date  : 2020-02-03
 *     desc  :
 * </pre>
 */

inline fun <T, R> T.dowithTry(block: (T) -> R) {
    try {
        block(this)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}