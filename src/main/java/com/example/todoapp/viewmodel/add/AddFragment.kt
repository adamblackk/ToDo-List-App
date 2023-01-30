package com.example.todoapp.viewmodel.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.FragmentAddBinding
import com.example.todoapp.viewmodel.SharedViewModel
import com.example.todoapp.viewmodel.ToDoViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddFragment : Fragment() {

    private val mToDoViewModel:ToDoViewModel by viewModels()
    private val sharedViewModel:SharedViewModel by viewModels()

    private  var _binding: FragmentAddBinding?=null
    private val binding get() =_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentAddBinding.inflate(inflater,container,false)

        binding.spinnerEt.onItemSelectedListener=sharedViewModel.listener

        binding.alarmBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_alarmFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost:MenuHost=requireActivity()
        menuHost.addMenuProvider(object :MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.menu_add->{
                        insertDataToDb()
                    }
                    android.R.id.home->requireActivity().onBackPressed()


                }


                return true
            }

        },viewLifecycleOwner,Lifecycle.State.RESUMED)

        takeDate()
    }

    private fun insertDataToDb() {
        val mTitle=binding.titleEt.text.toString()
        val mPriority=binding.spinnerEt.selectedItem.toString()
        val mDescription=binding.descriptionEt.text.toString()
        val dateFull= LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        val validation=sharedViewModel.verifyDataFromUser(mTitle,mDescription)
        if (validation){
            //insert data to database
            val newData=ToDoData(
                0,
                mTitle,
                sharedViewModel.parsePriority(mPriority),
                mDescription,
                dateFull
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(),"Successfully added.",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please fill out all fields.",Toast.LENGTH_SHORT).show()
        }


    }
    fun takeDate(){

        val saat= LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        println(saat)
    }



}