package com.example.fourquiz.data.local.converter


import androidx.room.TypeConverter

class Converters {
// Simple converter joining list with a custom delimiter.
// For production with complex data, consider using Gson or kotlinx.serialization.
private val separator = "|~|"

@TypeConverter
fun fromStringList(list: List<String>): String {
return list.joinToString(separator)
}

@TypeConverter
fun toStringList(data: String): List<String> {
if (data.isBlank()) return emptyList()
return data.split(separator)
}
}
