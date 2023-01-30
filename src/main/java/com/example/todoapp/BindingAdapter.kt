package com.example.todoapp

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.fragment.list.ListFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapter {

    companion object {
        @androidx.databinding.BindingAdapter("navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }
        @androidx.databinding.BindingAdapter("emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
                else -> {}
            }
        }
        @androidx.databinding.BindingAdapter("parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView: MaterialCardView,priority: Priority){
            when(priority){
                Priority.HIGH-> {cardView.setBackgroundColor(cardView.context.getColor(R.color.red))}
                Priority.MEDIUM-> {cardView.setBackgroundColor(cardView.context.getColor(R.color.yellow))}
                Priority.LOW-> {cardView.setBackgroundColor(cardView.context.getColor(R.color.green))}

            }
        }
        @androidx.databinding.BindingAdapter("sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: View,currentItem:ToDoData){
            view.setOnClickListener {
                val action=ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }

        }
    }
}