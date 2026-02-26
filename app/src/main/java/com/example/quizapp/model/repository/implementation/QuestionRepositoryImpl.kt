package com.example.quizapp.model.repository.implementation

import com.example.quizapp.model.Question
import com.example.quizapp.model.repository.QuestionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): QuestionRepository {
    override fun getQuestionsByCategory(categoryId: String): Flow<List<Question>>  = callbackFlow {
        val collection = db.collection("questions").whereEqualTo("categoryId", categoryId)
        val listener = collection.addSnapshotListener { snapshot, error ->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }

            if(snapshot != null){
                val items = snapshot.toObjects(Question::class.java)
                trySend(items).isSuccess
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun seedQuestions() {
        try {
            val questionsCollection = db.collection("questions")
            val categoriesCollection = db.collection("categories") // Nova referência

            // 1. Criar as Categorias primeiro
            val categories = listOf("Kotlin", "Geografia", "Matemática", "Ciências")
            for (catId in categories) {
                // Cria um documento onde o ID é o nome da categoria para evitar duplicatas
                categoriesCollection.document(catId).set(mapOf("id" to catId)).await()
            }

            // Busca as questões existentes para evitar duplicatas
            val snapshot = questionsCollection.get().await()
            val existingQuestionsTexts = snapshot.documents.mapNotNull {
                it.getString("text")
            }.toSet()

            val sampleQuestions = listOf(
                // KOTLIN
                Question(
                    categoryId = "Kotlin",
                    text = "Qual é a palavra-chave para declarar uma variável imutável em Kotlin?",
                    options = listOf("var", "val", "const", "let"),
                    correctAnswerIndex = 1
                ),
                Question(
                    categoryId = "Kotlin",
                    text = "Qual operador é usado para chamadas seguras contra NullPointerException?",
                    options = listOf("!!", "?:", "?.", "->"),
                    correctAnswerIndex = 2
                ),
                Question(
                    categoryId = "Kotlin",
                    text = "Qual construtor de corrotina bloqueia a thread atual?",
                    options = listOf("launch", "async", "runBlocking", "withContext"),
                    correctAnswerIndex = 2
                ),

                // GEOGRAFIA
                Question(
                    categoryId = "Geografia",
                    text = "Qual é a capital da Austrália?",
                    options = listOf("Sydney", "Melbourne", "Camberra", "Perth"),
                    correctAnswerIndex = 2
                ),
                Question(
                    categoryId = "Geografia",
                    text = "Qual é o maior continente em extensão territorial?",
                    options = listOf("América do Norte", "África", "Europa", "Ásia"),
                    correctAnswerIndex = 3
                ),
                Question(
                    categoryId = "Geografia",
                    text = "Qual é o maior oceano da Terra?",
                    options = listOf("Atlântico", "Índico", "Ártico", "Pacífico"),
                    correctAnswerIndex = 3
                ),

                // MATEMÁTICA
                Question(
                    categoryId = "Matemática",
                    text = "Quanto é 7 multiplicado por 8?",
                    options = listOf("54", "56", "64", "49"),
                    correctAnswerIndex = 1
                ),
                Question(
                    categoryId = "Matemática",
                    text = "Se 2x + 4 = 14, qual é o valor de x?",
                    options = listOf("3", "4", "5", "6"),
                    correctAnswerIndex = 2
                ),
                Question(
                    categoryId = "Matemática",
                    text = "Qual é a fórmula para a área de um círculo?",
                    options = listOf("π * r", "2 * π * r", "π * r²", "π² * r"),
                    correctAnswerIndex = 2
                ),

                // CIÊNCIAS
                Question(
                    categoryId = "Ciências",
                    text = "Qual é o maior planeta do nosso sistema solar?",
                    options = listOf("Terra", "Marte", "Saturno", "Júpiter"),
                    correctAnswerIndex = 3
                ),
                Question(
                    categoryId = "Ciências",
                    text = "Qual partícula subatômica possui carga elétrica positiva?",
                    options = listOf("Elétron", "Nêutron", "Próton", "Fóton"),
                    correctAnswerIndex = 2
                ),
                Question(
                    categoryId = "Ciências",
                    text = "Qual organela é conhecida como a usina de energia da célula?",
                    options = listOf("Núcleo", "Ribossomo", "Mitocôndria", "Complexo de Golgi"),
                    correctAnswerIndex = 2
                )
            )

            for (question in sampleQuestions) {
                if (!existingQuestionsTexts.contains(question.text)) {
                    questionsCollection.add(question).await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


