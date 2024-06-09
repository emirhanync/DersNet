package com.example.otomat

data class Question(
    val question: String,
    val options: List<String>,
    val correctOption: Int
)