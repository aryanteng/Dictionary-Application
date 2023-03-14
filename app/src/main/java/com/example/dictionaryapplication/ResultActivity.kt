package com.example.dictionaryapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dictionaryapplication.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val result = intent.getStringExtra("result")

        val resultFragment = ResultFragment()
        val bundle = Bundle().apply {
            putString("result", result)
        }
        resultFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_result, resultFragment)
        fragmentTransaction.commit()
    }
}