// --- GEMINI HEADER ---
package com.example.fourquiz.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fourquiz.data.network.FirestoreService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LeaderboardEntry(val email: String, val score: Int)

class LeaderboardViewModel(private val firestoreService: FirestoreService) : ViewModel() {

    private val _scores = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val scores: StateFlow<List<LeaderboardEntry>> = _scores

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchLeaderboard()
    }

    private fun fetchLeaderboard() {
        viewModelScope.launch {
            _isLoading.value = true
            val rawData = firestoreService.fetchLeaderboard()

            val mappedScores = rawData.map { data ->
                LeaderboardEntry(
                    email = (data["userId"] as? String)?.take(5) ?: "Player",
                    score = (data["score"] as? Long)?.toInt() ?: 0
                )
            }
            _scores.value = mappedScores
            _isLoading.value = false
        }
    }
}
// --- GEMINI FOOTER ---