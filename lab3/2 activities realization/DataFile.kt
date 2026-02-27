package com.am24.lab3android

import android.content.Context
import android.widget.Toast
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class DataFile(private val context: Context) {

    fun readFile(filename: String): String? {
        try {
            val inputStream = context.openFileInput(filename)

            val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))

            val stringBuilder = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line).append('\n')
                line = reader.readLine()
            }
            inputStream.close()
            return stringBuilder.toString()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun saveToFile(filename: String, data: String) {
        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
            Toast.makeText(context, "Your data is saved in $filename file", Toast.LENGTH_SHORT).show()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun clearFile(filename: String, data: String = "") {
        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
            Toast.makeText(context, "Your data is cleared in $filename file", Toast.LENGTH_SHORT).show()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}