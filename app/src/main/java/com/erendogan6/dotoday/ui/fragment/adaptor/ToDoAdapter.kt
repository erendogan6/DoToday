package com.erendogan6.dotoday.ui.fragment.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erendogan6.dotoday.data.entity.ToDo
import com.erendogan6.dotoday.databinding.TodoCardDesignBinding

class ToDoAdapter(private val dataSet: ArrayList<ToDo>,
                  private val onDeleteClicked: (Int) -> Unit,
                  private val onItemClicked: (Int) -> Unit) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>(){
    class ViewHolder(val binding: TodoCardDesignBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TodoCardDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = dataSet[position].title
        holder.binding.deleteIcon.setOnClickListener {
            onDeleteClicked(position)
        }
        holder.binding.root.setOnClickListener {
            onItemClicked(position)
        }
    }

    override fun getItemCount() = dataSet.size
}
