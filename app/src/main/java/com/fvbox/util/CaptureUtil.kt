package com.fvbox.util

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import com.fvbox.util.blurry.Blurry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


/**
 *
 * @Description:
 * @Author: Jack
 * @CreateDate: 2022/5/19 22:24
 */
object CaptureUtil {
    /**
     * 将View转换成Bitmap
     * userID Int -1表示背景
     */
    @Suppress("DEPRECATION")
    fun captureView(window: Window, view: View, userID: Int = -1) {
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val bitmap =
                    Bitmap.createBitmap(
                        view.width,
                        view.height,
                        Bitmap.Config.ARGB_8888,
                        false
                    )
                convertLayoutToBitmap(
                    window, view, bitmap
                ) { copyResult ->
                    if (copyResult == PixelCopy.SUCCESS) {
                        GlobalScope.launch(Dispatchers.IO) {
                            saveBitmap(bitmap, userID)
                        }
                    }
                }
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    //开启view缓存bitmap
                    view.isDrawingCacheEnabled = true
                    //设置view缓存Bitmap质量
                    view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
                    //获取缓存的bitmap
                    val cache = withContext(Dispatchers.Main) {
                        view.drawingCache
                    }
                    if (cache != null && !cache.isRecycled) {
                        saveBitmap(cache, userID)
                    }
                    //销毁view缓存bitmap
                    view.destroyDrawingCache()
                    //关闭view缓存bitmap
                    view.isDrawingCacheEnabled = false
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertLayoutToBitmap(
        window: Window, view: View, dest: Bitmap,
        listener: PixelCopy.OnPixelCopyFinishedListener
    ) {
        //获取layout的位置
        val location = IntArray(2)
        view.getLocationInWindow(location)
        //请求转换
        PixelCopy.request(
            window,
            Rect(
                location[0],
                location[1],
                location[0] + view.width,
                location[1] + view.height
            ),
            dest, listener, Handler(Looper.getMainLooper())
        )
    }


    private fun saveBitmap(bitmap: Bitmap?, userID: Int) {
        if (bitmap == null || bitmap.isRecycled) {
            return
        }
        runCatching {
            val blurryBitmap = if (userID == -1) {
                blurryBitmap(bitmap)
            } else {
                bitmap
            }

            val imgPath = getCapturePath(userID)
            val tmpPath = File("$imgPath.tmp")

            if (tmpPath.parentFile?.exists() == false) {
                tmpPath.parentFile?.mkdir()
            }

            val tmpOutputStream = FileOutputStream(tmpPath)
            blurryBitmap.compress(Bitmap.CompressFormat.JPEG, 50, tmpOutputStream)
            tmpOutputStream.close()

            imgPath.delete()
            tmpPath.renameTo(imgPath)

            bitmap.recycle()
            blurryBitmap.recycle()
        }
    }

    private fun blurryBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(0.4f, 0.4f)
        val newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return Blurry.with(ContextHolder.get())
            .radius(55)
            .from(newBitmap).bitmap()
    }

    /**
     * 获取截图路径
     * userID Int -1表示背景
     * @return File
     */
    fun getCapturePath(userID: Int = -1): File {
        val cacheDir = ContextHolder.get().externalCacheDir
        return File(cacheDir, "page/${userID}.jpg")
    }

    /**
     * 删除截图
     * @param userID Int
     */
    fun deleteCapture(userID: Int = -1) {
        runCatching {
            getCapturePath(userID).delete()
        }
    }
}
