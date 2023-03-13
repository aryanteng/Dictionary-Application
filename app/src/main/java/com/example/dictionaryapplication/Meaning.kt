package com.example.dictionaryapplication

data class Meaning(
    val partOfSpeech: String,
    val definitions: MutableList<Definitions>
)