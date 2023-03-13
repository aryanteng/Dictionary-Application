package com.example.dictionaryapplication

data class Definitions(
    val definition: String,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val example: String
)
