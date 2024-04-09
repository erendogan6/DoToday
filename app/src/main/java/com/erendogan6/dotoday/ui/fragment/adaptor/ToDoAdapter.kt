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

class ToDoAdapter(
    private val dataSet: ArrayList<ToDo>,
    private val onDeleteClicked: (ToDo) -> Unit,
    private val onItemClicked: (ToDo) -> Unit,
    private val onCircleClicked: (ToDo, View) -> Unit
) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TodoCardDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoItem = dataSet[position]

        with(holder.binding) {
            titleText.text = todoItem.title
            duedateText.apply {
                if (todoItem.dueDate != null) {
                    visibility = View.VISIBLE
                    text = dateFormat.format(todoItem.dueDate)
                } else {
                    visibility = View.GONE
                }
            }
            alarmIcon.visibility = if (todoItem.reminderDate != null) View.VISIBLE else View.GONE

            completeIcon.setImageResource(if (todoItem.isCompleted) R.drawable.check_circle_icon else R.drawable.circle_icon)
            deleteIcon.setOnClickListener { onDeleteClicked(todoItem) }
            root.setOnClickListener { onItemClicked(todoItem) }
            completeIcon.setOnClickListener { onCircleClicked(todoItem, holder.itemView) }

            val priorityColor = when (todoItem.priority) {
                "low" -> R.color.colorPriorityLow
                "medium" -> R.color.colorPriorityMedium
                "high" -> R.color.colorPriorityHigh
                else -> R.color.cardColor
            }
            priorityBar.setBackgroundResource(priorityColor)
        }
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newData: List<ToDo>) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: TodoCardDesignBinding) : RecyclerView.ViewHolder(binding.root)
}
