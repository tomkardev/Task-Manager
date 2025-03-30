package com.example.taskmanagerapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.fragments.HomeFragmentDirections
import com.example.taskmanagerapp.models.TaskData
import com.example.taskmanagerapp.utils.ItemPosition
import com.example.taskmanagerapp.utils.TaskDiffCallbackDiffUtil
import com.google.android.material.chip.Chip
import java.util.Date
import java.util.Locale

class TasksAdapter  : androidx.recyclerview.widget.ListAdapter<TaskData, TasksAdapter.TasksViewHolder>(TaskDiffCallbackDiffUtil()){
    class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.titleText)
        val date = itemView.findViewById<TextView>(R.id.dueDate)

        val chip = itemView.findViewById<Chip>(R.id.chip)
        val status = itemView.findViewById<Chip>(R.id.status)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return TasksViewHolder(view)
    }


    private  var itemPositionListener: ItemPosition? = null

    fun setItemPositionListener(listener: ItemPosition) {
        itemPositionListener = listener
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val tasks = getItem(position)


        val context = holder.itemView.context
        holder.title.text = tasks.title
        val simpleDateFormat = java.text.SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())

        holder.date.text = simpleDateFormat.format(tasks.dueDate.time)

        when(tasks.priority){
            0 -> { holder.chip.chipBackgroundColor = ContextCompat.getColorStateList(holder.itemView.context, R.color.low)
            holder.chip.text = "Low"}
            1 -> { holder.chip.chipBackgroundColor = ContextCompat.getColorStateList(holder.itemView.context, R.color.medium)
            holder.chip.text = "Medium"}
            2 -> { holder.chip.chipBackgroundColor = ContextCompat.getColorStateList(holder.itemView.context, R.color.high)
            holder.chip.text = "High"}
        }

        holder.status.text = tasks.status

        holder.itemView.setOnClickListener {

            val action = HomeFragmentDirections.actionHomeFragmentToTaskDetailsFragment(tasks)

            holder.itemView.findNavController().navigate(action)


        }


    }



}