package com.example.dictionaryapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dictionaryapplication.databinding.ActivityDefinitionBinding
import com.example.dictionaryapplication.databinding.ActivityResultBinding

class DefinitionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDefinitionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDefinitionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val item = intent.getStringExtra("item")

        val definitionFragment = DefinitionFragment()
        val bundle = Bundle().apply {
            putString("item", item)

        }
        definitionFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_definition, definitionFragment)
        fragmentTransaction.commit()
    }
}