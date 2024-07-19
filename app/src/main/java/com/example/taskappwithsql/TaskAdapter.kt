package com.example.taskappwithsql

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskappwithsql.databinding.ListTaskBinding

class TaskAdapter(
    val deleteTask : (Int) -> Unit,
    val editTask : (Task) -> Unit
): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){
    private var task : List<Task> = listOf()

    fun setTasks(tasks : List<Task>){
        this.task = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        val task = task[position]
        with(holder){
            binding.textTaskTitle.text = task.title
            binding.textTaskDescription.text = task.description
            binding.ivDelete.setOnClickListener {
                deleteTask(task.id)
            }
            binding.ivEdit.setOnClickListener {
                editTask(task)
            }
        }
    }
    override fun getItemCount(): Int{
        return task.size
    }
    class TaskViewHolder(val binding: ListTaskBinding): RecyclerView.ViewHolder(binding.root)
}
