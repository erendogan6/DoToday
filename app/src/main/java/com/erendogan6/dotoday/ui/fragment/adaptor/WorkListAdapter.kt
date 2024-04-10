package com.erendogan6.dotoday.ui.fragment.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.data.model.WorkList
import com.erendogan6.dotoday.databinding.WorklistCardDesignBinding

class WorkListAdapter(
    private val onDeleteClicked: (WorkList) -> Unit,
    private val onItemClicked: (Int) -> Unit,
    private val onEditClicked: (WorkList) -> Unit,
    private val calculateCompletionPercentage: (Int, (Int) -> Unit) -> Unit
) : RecyclerView.Adapter<WorkListAdapter.WorkListViewHolder>() {

    private var workLists: ArrayList<WorkList> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkListViewHolder {
        val binding = WorklistCardDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n") override fun onBindViewHolder(holder: WorkListViewHolder, position: Int) {
        val workList = workLists[position]
        holder.binding.apply {
            WorkListName.text = workList.name
            root.setOnClickListener {
                onItemClicked(workList.id)
            }
            WorkListDeleteIcon.setOnClickListener {
                onDeleteClicked(workList)
            }
            WorkListEditIcon.setOnClickListener {
                onEditClicked(workList)
            }
            calculateCompletionPercentage(workList.id) { completionPercentage ->
                circularProgress.progress = completionPercentage
                percantageText.setText("$completionPercentage +%")
                val color = when {
                    completionPercentage < 50 -> R.color.colorRed
                    completionPercentage in 50..74 -> R.color.colorYellow
                    else -> R.color.colorGreen
                }
                circularProgress.setIndicatorColor(ContextCompat.getColor(holder.itemView.context, color))
            }
        }
    }

    override fun getItemCount() = workLists.size

    fun submitList(newWorkLists: ArrayList<WorkList>) {
        workLists = newWorkLists
        notifyDataSetChanged()
    }


    class WorkListViewHolder(val binding: WorklistCardDesignBinding) : RecyclerView.ViewHolder(binding.root)
}
