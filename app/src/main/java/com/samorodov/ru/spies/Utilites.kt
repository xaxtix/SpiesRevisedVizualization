package com.samorodov.ru.spies

import android.content.Context

fun dp(context: Context, value: Float): Float {
    return context.resources.displayMetrics.density * value
}

fun gcd(a: Int, b: Int): Int {
    var a = a
    var b = b
    while (b != 0) {
        val tmp = a % b
        a = b
        b = tmp
    }
    return a
}