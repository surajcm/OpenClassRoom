package com.classroom.init

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.deleteIfExists

class FileUploadUtil {
    fun saveFile(uploadDir:String, fileName:String, multipartFile: MultipartFile) {
        val uploadPath = Paths.get(uploadDir)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
        try {(multipartFile.inputStream.use { inputStream ->
            val filePath = uploadPath.resolve(fileName)
            Files.copy(inputStream, filePath)
        })} catch (e: Exception) {
            throw IOException("Could not save file: $fileName", e)
        }
    }

    fun cleanDir(dir: String) {
        try {
            Files.list(Paths.get(dir)).forEach { file -> if (!Files.isDirectory(file)) file.deleteIfExists() }
        } catch (e: IOException) {
            println("Could not list directory: $dir")
        }
    }
}