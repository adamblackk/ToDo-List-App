package com.example.todoapp.fragment.recycler

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.databinding.FragmentRecyclerBinding
import com.example.todoapp.fragment.recycler.adapter.RecyclerAdapter
import com.example.todoapp.viewmodel.RecyclerDataViewModel
import com.example.todoapp.viewmodel.SharedViewModel
import com.example.todoapp.viewmodel.ToDoViewModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class RecyclerFragment : Fragment() {

    private  var _binding: FragmentRecyclerBinding?=null
    private val binding get() =_binding!!

    private val recyclerViewModel:RecyclerDataViewModel by viewModels()
    private val toDoViewModel:ToDoViewModel by viewModels()
    private val sharedViewModel:SharedViewModel by viewModels()

    private val recyclerAdapter:RecyclerAdapter by lazy {
        RecyclerAdapter(requireActivity(),
        recyclerViewModel,
        toDoViewModel,
        sharedViewModel)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentRecyclerBinding.inflate(inflater,container,false)

        setupRecyclerView()
        recyclerViewModel.getAllRecyclerData.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.setData(it)
        })

        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.recycler_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.delete_all_recycler_ıtem ->{
                        confirmRemoval()
                    }
                    R.id.recalL_all -> {recallAll()
                    findNavController().navigate(R.id.action_recyclerFragment_to_listFragment)
                        Toast.makeText(requireContext(),"All items recalled",Toast.LENGTH_SHORT).show()
                    }

                    android.R.id.home->requireActivity().onBackPressed()
                }
                return true
            }


        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    fun setupRecyclerView(){
        binding.callBackRecyclerview.layoutManager=LinearLayoutManager(requireContext())
        binding.callBackRecyclerview.adapter=recyclerAdapter
        binding.callBackRecyclerview.itemAnimator= SlideInUpAnimator().apply {
            addDuration=350
        }
    }
    private fun confirmRemoval() {
        val builder= AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            recyclerViewModel.deleteAllRecyclerDataItem()
            Toast.makeText(requireContext(),
                "Successfully removed everything!",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_recyclerFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_->}
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure to remove everything permanently?")
        builder.create().show()


    }
    fun recallAll(){
    recyclerViewModel.getAllRecyclerData.observe(viewLifecycleOwner, Observer {
        var id: Int = 0
        var title = ""
        var date = ""
        var description = ""
        var priority = Priority.HIGH
        var i:Int=0
        while (i < it.size){
            id=it[i].recyclerDataModel.id
            title=it[i].recyclerDataModel.title
            date=it[i].recyclerDataModel.date
            description=it[i].recyclerDataModel.description
            priority=it[i].recyclerDataModel.priority

            val toDoData=ToDoData(id,title,priority,description,date)
            toDoViewModel.insertData(toDoData)
            i +=1
        }
        recyclerViewModel.deleteAllRecyclerDataItem()
    })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}