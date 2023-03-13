package com.example.dictionaryapplication

import android.content.Context
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dictionaryapplication.databinding.FragmentResultBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

        binding.btnAudio.setOnClickListener {
            val url = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3"
            val asyncTask = DownloadAudioTask(requireContext(), url)
            asyncTask.execute()
        }
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