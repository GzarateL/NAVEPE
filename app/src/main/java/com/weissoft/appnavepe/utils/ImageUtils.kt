// Ruta: com/weissoft/appnavepe/utils/ImageUtils.kt

package com.weissoft.appnavepe.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

fun saveImageToInternalStorage(context: Context, imageUri: Uri): Uri? {
    return try {
        // Obtener el bitmap de la imagen
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

        // Crear un archivo en el almacenamiento interno de la aplicación
        val filename = "profile_image.jpg"
        val file = File(context.filesDir, filename)

        // Escribir el bitmap en el archivo
        val fos: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()

        // Devolver la URI del archivo guardado
        Uri.fromFile(file)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
