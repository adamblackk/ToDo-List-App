package com.example.todoapp.fragment.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

     var toDoList = emptyList<ToDoData>()

    class MyViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(toDoData: ToDoData){
            binding.todoData=toDoData
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater=LayoutInflater.from(parent.context)
                val binding=RowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       // val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context))
        //return MyViewHolder(binding)
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem=toDoList[position]
        holder.bind(currentItem)

        /**
         Burayı data binding öncesi kullandım...
        holder.binding.titleTxt.text = toDoList[position].title
        holder.binding.descriptionTxt.text = toDoList[position].description

        holder.binding.rowBackground.setOnClickListener {
            val action =ListFragmentDirections.actionListFragmentToUpdateFragment(toDoList[position])
            holder.itemView.findNavController().navigate(action)

        }

        when (toDoList[position].priority) {
            Priority.HIGH -> holder.binding.priorityIndicator.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.red
                )
            )
            Priority.MEDIUM -> holder.binding.priorityIndicator.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.yellow
                )
            )
            Priority.LOW -> holder.binding.priorityIndicator.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.green
                )
            )
        }
         *
         */


    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    fun setData(toDoData:List<ToDoData>){
        val toDoDiffUtil=ToDoDiffUtil(toDoList,toDoData)
        val toDoDiffResult=DiffUtil.calculateDiff(toDoDiffUtil)
        this.toDoList=toDoData
        toDoDiffResult.dispatchUpdatesTo(this)

    }
}