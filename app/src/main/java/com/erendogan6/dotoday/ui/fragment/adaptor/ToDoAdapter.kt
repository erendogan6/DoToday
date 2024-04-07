package com.erendogan6.dotoday.ui.fragment.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.databinding.TodoCardDesignBinding

class ToDoAdapter(
    private val dataSet: ArrayList<ToDo>,
    private val onDeleteClicked: (Int) -> Unit,
    private val onItemClicked: (Int) -> Unit,
    private val onCircleClicked: (ToDo) -> Unit
) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TodoCardDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.titleText.text = dataSet[position].title

        if (dataSet[position].isCompleted) {
            holder.binding.completeIcon.setImageResource(R.drawable.check_circle_icon)
        } else {
            holder.binding.completeIcon.setImageResource(R.drawable.circle_icon)
        }

        holder.binding.deleteIcon.setOnClickListener {
            onDeleteClicked(position)
        }
        holder.binding.root.setOnClickListener {
            onItemClicked(position)
        }
        holder.binding.completeIcon.setOnClickListener {
            onCircleClicked(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(val binding: TodoCardDesignBinding) : RecyclerView.ViewHolder(binding.root)
}
