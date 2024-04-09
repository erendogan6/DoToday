package com.erendogan6.dotoday.ui.fragment.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erendogan6.dotoday.data.model.WorkList
import com.erendogan6.dotoday.databinding.WorklistCardDesignBinding

class WorkListAdapter(
    private val onDeleteClicked: (WorkList) -> Unit, private val onItemClicked: (Int) -> Unit, private val onEditClicked: (WorkList) -> Unit
) : RecyclerView.Adapter<WorkListAdapter.WorkListViewHolder>() {

    private var workLists: ArrayList<WorkList> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkListViewHolder {
        val binding = WorklistCardDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkListViewHolder, position: Int) {
        val workList = workLists[position]
        holder.binding.WorkListName.text = workList.name
        holder.binding.root.setOnClickListener {
            onItemClicked(workList.id)
        }
        holder.binding.WorkListDeleteIcon.setOnClickListener {
            onDeleteClicked(workList)
        }
        holder.binding.WorkListEditIcon.setOnClickListener {
            onEditClicked(workList)
        }
    }

    override fun getItemCount() = workLists.size

    fun submitList(newWorkLists: ArrayList<WorkList>) {
        workLists = newWorkLists
        notifyDataSetChanged()
    }


    class WorkListViewHolder(val binding: WorklistCardDesignBinding) : RecyclerView.ViewHolder(binding.root)
}
