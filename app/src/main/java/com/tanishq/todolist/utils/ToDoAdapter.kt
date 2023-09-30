package com.tanishq.todolist.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tanishq.todolist.databinding.TaskitemBinding

class ToDoAdapter(private val list: MutableList<TaskData>) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){
    private var listener:ToDoAdapterClickInterface? = null
    fun setListener(listener:ToDoAdapterClickInterface){
        this.listener = listener
    }
    inner class ToDoViewHolder(val binding: TaskitemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = TaskitemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text = this.task
                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClicked(this)
                }
                binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClicked(this)
                }
                binding.checkBox.setOnCheckedChangeListener { compoundButton, b ->
                    if(b){
                        listener?.onCheckBoxClicked(this,this.task,binding.checkBox.isChecked)
                    }
                }
                binding.checkBox.isChecked = list[position].check=="true"

            }
        }
    }

    interface ToDoAdapterClickInterface{
        fun onDeleteTaskBtnClicked(taskData: TaskData)
        fun onEditTaskBtnClicked(taskData: TaskData)

        fun onCheckBoxClicked(taskData: TaskData,task:String,check:Boolean)

    }
}