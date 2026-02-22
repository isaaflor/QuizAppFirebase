package com.example.fourquiz.data.local.converter

/** Gemini - início
Prompt: Act as a Senior Android Developer. I have an existing Android project with the namespace com.example.fourquiz. Task: Implement the local persistence layer. Git Start: Provide the command to create and switch to a branch named feature/local-data. Code: Create the Question and UserResult Entities, the DAO (with suspend functions), and the RoomDatabase class with a TypeConverter. Git End: Provide the commands to add all changes and commit with the message 'Add Room persistence layer'. Instructions: Use the namespace com.example.fourquiz. TELL ME EXACTLY the file names and the directory paths (relative to app/src/main/java/com/example/fourquiz/). FORMAT: Wrap all code blocks with: /** Gemini - início \n Prompt: [Insert Prompt Text Here] \n / \n [CODE] \n /* Gemini - final */
*/
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
/* Gemini - final */