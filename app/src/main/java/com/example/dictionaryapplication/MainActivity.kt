package com.example.dictionaryapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.dictionaryapplication.databinding.ActivityMainBinding
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


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
                val connMgr =
                    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connMgr.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected) {
                    sendDictionaryRequest(input.toString())
                } else {
                    Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Please enter a word!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    class DictionaryAsyncTask(private val activity: Activity) : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String?): String {
            val url = URL(urls[0])
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val inputStream = BufferedInputStream(urlConnection.inputStream)
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()

                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
                return stringBuilder.toString()
            } finally {
                urlConnection.disconnect()
            }
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d("Dictionary API Response", (result ?: "") as String)
            val intent = Intent(activity, ResultActivity::class.java)
            intent.putExtra("result", result)
            activity.startActivity(intent)
        }
    }
    private fun sendDictionaryRequest(word: String) {
        val url = "https://api.dictionaryapi.dev/api/v2/entries/en_US/$word"
        val asyncTask = DictionaryAsyncTask(this)
        asyncTask.execute(url)
    }

}