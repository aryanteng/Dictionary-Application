package com.example.dictionaryapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter (private val items: MutableList<Meaning>):
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var tvPos: TextView

    interface OnItemClickListener {
        fun onItemClick(item: Meaning)
    }
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: Meaning) {
            tvPos.text = item.partOfSpeech
            tvPos.setOnClickListener {
                listener?.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pos, parent, false)
        tvPos = view.findViewById(R.id.tv_pos)

        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

