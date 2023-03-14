package com.example.dictionaryapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.dictionaryapplication.databinding.FragmentDefinitionBinding
import com.example.dictionaryapplication.databinding.FragmentResultBinding
import com.google.gson.Gson
import org.json.JSONArray

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DefinitionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DefinitionFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var item: String
    private lateinit var binding: FragmentDefinitionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            item = it.getString("item").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDefinitionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDefinition.text = item

        val gson = Gson()
        val meaningObj = gson.fromJson(item, Meaning::class.java)

        if(meaningObj.partOfSpeech.isNotEmpty()){
            binding.tvPos.text = "Part of Speech: " + meaningObj.partOfSpeech
        }

        for(i in 0 until meaningObj.definitions.size){
            if(meaningObj.definitions[i].definition.isNotEmpty()){
                binding.tvDefinition.text = "Definition: " + meaningObj.definitions[i].definition
                break
            }
        }

        for(i in 0 until meaningObj.definitions.size){
            if(meaningObj.definitions[i].synonyms.isNotEmpty()){
                var synonyms = "Synonyms: "
                for(j in 0 until meaningObj.definitions[i].synonyms.size){
                    synonyms += if(j == meaningObj.definitions[i].synonyms.size - 1){
                        meaningObj.definitions[i].synonyms[j] + "."
                    } else{
                        meaningObj.definitions[i].synonyms[j] + ", "
                    }
                }
                binding.tvSynonyms.text = synonyms
                break
            }
        }

        for(i in 0 until meaningObj.definitions.size){
            if(meaningObj.definitions[i].antonyms.isNotEmpty()){
                var antonyms = "Antonyms: "
                for(j in 0 until meaningObj.definitions[i].antonyms.size){
                    antonyms += if(j == meaningObj.definitions[i].antonyms.size - 1){
                        meaningObj.definitions[i].antonyms[j] + "."
                    } else{
                        meaningObj.definitions[i].antonyms[j] + ", "
                    }
                }
                binding.tvAntonyms.text = antonyms
                break
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
         * @return A new instance of fragment DefinitionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DefinitionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}