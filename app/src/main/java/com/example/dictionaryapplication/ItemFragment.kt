package com.example.dictionaryapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dictionaryapplication.databinding.FragmentResultBinding
import com.example.dictionaryapplication.placeholder.PlaceholderContent
import com.google.gson.Gson
import org.json.JSONArray

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private var columnCount = 1
    private lateinit var result: String
    private var meanings: MutableList<Meaning> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            result = it.getString("result").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val jsonArray = JSONArray(result)
                val jsonObject = jsonArray.getJSONObject(0)
                val word = jsonObject.getString("word")
                val phonetics = jsonObject.getJSONArray("phonetics")
                val meanings = jsonObject.getJSONArray("meanings")
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
                adapter = MyItemRecyclerViewAdapter(meaningsList)

            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}