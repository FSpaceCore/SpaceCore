package com.fvbox.util.extension

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.fvbox.util.ContextHolder

import com.fvbox.util.Log

fun getString(id: Int, vararg args: Any): String {
    if (args.isEmpty()) {
        return ContextHolder.get().getString(id)
    }
    return ContextHolder.get().getString(id, *args)
}


fun getPixel(id: Int): Int {
    return ContextHolder.get().resources.getDimensionPixelOffset(id)
}

fun getDrawable(id: Int): Drawable {
    return ContextCompat.getDrawable(ContextHolder.get(), id)!!
}


val Int.dp: Float
    get() {
        val scale = ContextHolder.get().resources.displayMetrics.density
        return (this * scale + 0.5f)
    }
