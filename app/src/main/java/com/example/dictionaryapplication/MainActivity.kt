package com.example.dictionaryapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dictionaryapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.btnSearch.setOnClickListener {
            val input = binding.etWord.text
            if(input.isNotEmpty()){

            }
            else{
                Toast.makeText(this, "Please enter a word!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}