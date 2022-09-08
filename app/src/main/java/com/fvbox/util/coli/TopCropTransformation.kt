package com.fvbox.util.coli

import android.graphics.Bitmap
import android.graphics.Matrix
import coil.size.Size
import coil.size.pxOrElse
import coil.transform.Transformation


/**
 *
 * @description: top crop
 * @author: Jack
 * @create: 2022-07-05
 */
class TopCropTransformation : Transformation {
    override val cacheKey: String
        get() = javaClass.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val dstWidth = size.width.pxOrElse { 0 }
        val dstHeight = size.height.pxOrElse { 0 }
        val scale = dstWidth.toFloat() / input.width
        val matrix = Matrix()
        matrix.preScale(scale, scale)
        val scaleBitmap = Bitmap.createBitmap(input, 0, 0, input.width, input.height, matrix, false)
        return Bitmap.createBitmap(scaleBitmap, 0, 0, dstWidth, dstHeight)
    }
}
