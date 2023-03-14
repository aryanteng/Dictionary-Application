package com.example.dictionaryapplication

import android.content.Context
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionaryapplication.databinding.FragmentResultBinding
import com.google.gson.Gson
import org.json.JSONArray
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentResultBinding
    private lateinit var result: String
    private lateinit var audio: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            result = it.getString("result").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonArray = JSONArray(result)
        val jsonObject = jsonArray.getJSONObject(0)
        val word = jsonObject.getString("word")
        val phonetics = jsonObject.getJSONArray("phonetics")
        val meanings = jsonObject.getJSONArray("meanings")

        for(i in 0 until phonetics.length()){
            val phoneticObject = phonetics.getJSONObject(i)
            val audioUrl = phoneticObject.getString("audio")
            if(audioUrl.isNotEmpty()){
                audio = audioUrl
            }
        }

        binding.tvWord.text = word

        val meaningsList = mutableListOf<Meaning>()

        for (i in 0 until meanings.length()) {
            val meaningJsonObject = meanings.getJSONObject(i)
            val partOfSpeech = meaningJsonObject.getString("partOfSpeech")
            val definitions = meaningJsonObject.getJSONArray("definitions")
            val definitionsList = mutableListOf<Definitions>()
            for (j in 0 until definitions.length()){
                val definition = definitions.getJSONObject(j).optString("definition", "")
                val synonyms = definitions.getJSONObject(j).optJSONArray("synonyms")
                val antonyms = definitions.getJSONObject(j).optJSONArray("antonyms")
                val example = definitions.getJSONObject(j).optString("example", "")
                val synonymsList = mutableListOf<String>()
                val antonymsList = mutableListOf<String>()
                for (k in 0 until synonyms.length()) {
                    val synonym = synonyms.getString(k)
                    synonymsList.add(synonym)
                }
                for (k in 0 until antonyms.length()) {
                    val antonym = antonyms.getString(k)
                    antonymsList.add(antonym)
                }
                val definitionItem = Definitions(definition = definition, synonyms = synonymsList, antonyms = antonymsList, example = example)
                definitionsList.add(definitionItem)
            }
            meaningsList.add(Meaning(partOfSpeech, definitionsList))
        }

        Log.i("MEANING LIST", meaningsList.toString())

        val itemAdapter = ItemAdapter(meaningsList)
        binding.rvPos.adapter = itemAdapter
        binding.rvPos.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAudio.setOnClickListener {
            if(audio.isNotEmpty()){
                val audioURL = audio
                val asyncTask = DownloadAudioTask(requireContext(), audioURL)
                asyncTask.execute()
            }
        }

        itemAdapter.setOnBtnClickListener(object: ItemAdapter.OnBtnClickListener{
            override fun onBtnClick(item: Meaning) {
                val definitionFragment = DefinitionFragment()
                val bundle = Bundle().apply {
                    val gson = Gson()
                    val meaningString = gson.toJson(item)
                    putString("item", meaningString)
                }
                definitionFragment.arguments = bundle
                val fragmentManager = childFragmentManager
                definitionFragment.show(fragmentManager, "dialog")
            }
        })

    }

    class DownloadAudioTask(private val context: Context, private val audio: String) : AsyncTask<Void, Void, Boolean>() {
        private lateinit var mediaPlayer: MediaPlayer
        private val audioFileName = "downloadedAudio.mp3"
        override fun doInBackground(vararg params: Void?): Boolean {
            var input: InputStream? = null
            var output: OutputStream? = null
            try {
                val url = URL(audio)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    return false
                }
                input = connection.inputStream
                output = context.openFileOutput(audioFileName, Context.MODE_PRIVATE)
                val buffer = ByteArray(4096)
                var length: Int
                while (input.read(buffer).also { length = it } != -1) {
                    output.write(buffer, 0, length)
                }
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(context.getFileStreamPath(audioFileName).absolutePath)
                mediaPlayer?.prepare()
                return true
            } catch (e: Exception) {
                return false
            } finally {
                input?.close()
                output?.close()
            }
        }
        override fun onPostExecute(result: Boolean) {
            if (result) {
                Toast.makeText(context, "Downloaded successfully", Toast.LENGTH_SHORT).show()
                mediaPlayer?.start()
            } else {
                Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}