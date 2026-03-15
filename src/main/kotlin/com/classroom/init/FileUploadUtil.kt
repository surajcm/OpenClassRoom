package com.classroom.init

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

object FileUploadUtil {
    fun saveFile(uploadDir: String, fileName: String, multipartFile: MultipartFile) {
        val uploadPath = Path.of(uploadDir)

        if (!uploadPath.exists()) {
            Files.createDirectories(uploadPath)
        }

        multipartFile.inputStream.use { inputStream ->
            runCatching {
                val filePath = uploadPath.resolve(fileName)
                Files.copy(inputStream, filePath)
            }.getOrElse { e ->
                throw IOException("Could not save file: $fileName", e)
            }
        }
    }

    fun cleanDir(dir: String) {
        runCatching {
            Files.list(Path.of(dir)).use { stream ->
                stream.filter { !it.isDirectory() }
                    .forEach { it.deleteIfExists() }
            }
        }.onFailure {
            println("Could not clean directory: $dir")
        }
    }
}