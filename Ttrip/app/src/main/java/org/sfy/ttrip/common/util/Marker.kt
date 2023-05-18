package org.sfy.ttrip.common.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import org.sfy.ttrip.R
import java.io.File

fun makeMarkerImg(context: Context, uri: Uri, file: File): Bitmap {
    val bitmap = Bitmap.createBitmap(1500, 1700, Bitmap.Config.ARGB_8888)
    val c = Canvas(bitmap)

    val markerBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_marker_pin)
    val background: Drawable = BitmapDrawable(markerBitmap)

    val fin = getCircleBitmap(context, uri, file)
    val profileImg: Drawable = BitmapDrawable(fin)

    background.setBounds(0, 0, 1500, 1700)

    val centerX = (bitmap.width / 2) - (900 / 2)
    val centerY = (bitmap.height / 2) - (900 / 2) - 100

    profileImg.setBounds(centerX, centerY, centerX + 900, centerY + 900)

    background.draw(c)
    profileImg.draw(c)

    return bitmap
}

private fun getCircleBitmap(context: Context, uri: Uri, file: File): Bitmap {
    // 이미지를 가져옵니다.
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

    // 이미지를 회전시킵니다.
    val rotatedBitmap = getRotatedBitmap(bitmap, file)

    // 이미지를 원형태로 만들고 리턴.
    return cropCircular(rotatedBitmap)
}

fun getRotatedBitmap(bitmap: Bitmap, file: File): Bitmap {
    // 파일의 Exif 정보를 가져옵니다.
    val exif = ExifInterface(file.absolutePath)
    val orientation =
        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    // 이미지를 회전시킵니다.
    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun cropCircular(bitmap: Bitmap): Bitmap {
    val size = Math.min(bitmap.width, bitmap.height)
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint()
    paint.isAntiAlias = true
    paint.color = Color.WHITE

    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, (size - bitmap.width) / 2f, (size - bitmap.height) / 2f, paint)

    return output
}