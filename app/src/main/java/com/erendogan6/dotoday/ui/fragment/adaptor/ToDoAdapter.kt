package com.erendogan6.dotoday.ui.fragment.adaptor

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.databinding.TodoCardDesignBinding
import java.util.Locale

class ToDoAdapter(private val dataSet: ArrayList<ToDo>,
                  private val onDeleteClicked: (Int) -> Unit,
                  private val onItemClicked: (Int) -> Unit,
                  private val onCircleClicked: (ToDo, View) -> Unit) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TodoCardDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoItem = dataSet[position]

        with(holder.binding) {
            titleText.text = dataSet[position].title
            if (todoItem.dueDate != null) {
                duedateText.visibility = View.VISIBLE
                duedateText.text =
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(todoItem.dueDate)
            } else {
                duedateText.visibility = View.GONE
            }
            completeIcon.setImageResource(if (dataSet[position].isCompleted) R.drawable.check_circle_icon else R.drawable.circle_icon)
            deleteIcon.setOnClickListener { onDeleteClicked(position) }
            root.setOnClickListener { onItemClicked(position) }
            completeIcon.setOnClickListener { onCircleClicked(dataSet[position], holder.itemView) }
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(val binding: TodoCardDesignBinding) : RecyclerView.ViewHolder(binding.root)
}
