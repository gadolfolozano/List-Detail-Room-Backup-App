package pe.gadolfolozano.listdetailroombackupapp.ui.util

import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


fun File.unzip(unzipLocationRoot: File? = null) {

    val rootFolder =
        unzipLocationRoot ?: File(parentFile?.absolutePath + File.separator + nameWithoutExtension)
    if (!rootFolder.exists()) {
        rootFolder.mkdirs()
    }

    ZipFile(this).use { zip ->
        zip.entries()
            .asSequence()
            .map {
                val outputFile = File(rootFolder.absolutePath + File.separator + it.name)
                ZipIO(it, outputFile)
            }
            .map {
                it.output.parentFile?.run {
                    if (!exists()) mkdirs()
                }
                it
            }
            .filter { !it.entry.isDirectory }
            .forEach { (entry, output) ->
                zip.getInputStream(entry).use { input ->
                    output.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
    }

}

fun File.zip(zipLocation: File? = null) {
    val zippedFile =
        zipLocation
            ?: File(parentFile?.absolutePath + File.separator + nameWithoutExtension + ".zip")
    if (zippedFile.exists()) {
        zippedFile.deleteRecursively()
    }

    val buffer = 2048

    val dest = FileOutputStream(zippedFile.absolutePath)
    val out = ZipOutputStream(BufferedOutputStream(dest))
    if (this.isDirectory) {
        zipSubFolder(out, this, this.parent?.length ?: 0)
    } else {
        val data = ByteArray(buffer)
        val fi = FileInputStream(this)
        val origin = BufferedInputStream(fi, buffer)
        val entry = ZipEntry(getLastPathComponent(this.absolutePath))
        entry.time = this.lastModified() // to keep modification time after unzipping
        out.putNextEntry(entry)
        var count = origin.read(data, 0, buffer)
        while (count != -1) {
            out.write(data, 0, count)
            count = origin.read(data, 0, buffer)
        }
    }
    out.close()
}

private fun zipSubFolder(
    out: ZipOutputStream, folder: File,
    basePathLength: Int
) {
    val buffer = 2048
    val fileList = folder.listFiles()
    fileList?.forEach { file ->
        if (file.isDirectory) {
            zipSubFolder(out, file, basePathLength)
        } else {
            val data = ByteArray(buffer)
            val unmodifiedFilePath = file.absolutePath
            val relativePath = unmodifiedFilePath
                .substring(basePathLength)
            val fi = FileInputStream(unmodifiedFilePath)
            val origin = BufferedInputStream(fi, buffer)
            val entry = ZipEntry(relativePath)
            entry.time = file.lastModified() // to keep modification time after unzipping
            out.putNextEntry(entry)
            var count: Int
            while (origin.read(data, 0, buffer).also { count = it } != -1) {
                out.write(data, 0, count)
            }
            origin.close()
        }
    }
}

private fun getLastPathComponent(filePath: String): String {
    val segments = filePath.split(File.separator.toRegex()).toTypedArray()
    return if (segments.isEmpty()) "" else segments[segments.size - 1]
}

data class ZipIO(val entry: ZipEntry, val output: File)