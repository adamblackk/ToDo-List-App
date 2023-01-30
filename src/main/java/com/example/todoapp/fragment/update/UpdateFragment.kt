package com.example.todoapp.fragment.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.RecyclerData
import com.example.todoapp.data.models.RecyclerDataModel
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.databinding.FragmentUpdateBinding
import com.example.todoapp.viewmodel.RecyclerDataViewModel
import com.example.todoapp.viewmodel.SharedViewModel
import com.example.todoapp.viewmodel.ToDoViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val toDoViewModel: ToDoViewModel by viewModels()
    private val recyclerDataViewModel:RecyclerDataViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        binding.currentTitleEt.setText(args.currentItem.title)
        binding.currentDescriptionEt.setText(args.currentItem.description)
        binding.currentSpinnerEt.setSelection(sharedViewModel.parsePriorityToInt(args.currentItem.priority))
        binding.currentSpinnerEt.onItemSelectedListener = sharedViewModel.listener
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.save_menu -> updateItem()
                    R.id.delete -> confirmItemRemoval()

                    android.R.id.home->requireActivity().onBackPressed()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun confirmItemRemoval() {
        val builder=AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            toDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(),
            "Successfully removed.'${args.currentItem.title}'",
            Toast.LENGTH_SHORT).show()
            val id=args.currentItem.id
            val title=args.currentItem.title
            val description=args.currentItem.description
            val priority=args.currentItem.priority
            val date=args.currentItem.date

            val recyclerDataModel=RecyclerDataModel(id,title,priority,description,date)
            val recyclerData=RecyclerData(recyclerDataModel)
            recyclerDataViewModel.insertRecyclerData(recyclerData)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_->}
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure to remove '${args.currentItem.title}'?")
        builder.create().show()

    }

    private fun updateItem() {
        val title = binding.currentTitleEt.text.toString()
        val description = binding.currentDescriptionEt.text.toString()
        val mPriority = binding.currentSpinnerEt.selectedItem.toString()
        val dateFull= LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        val validation = sharedViewModel.verifyDataFromUser(title, description)
        if (validation) {
            //update current item
            val updateItem = ToDoData(
                args.currentItem.id,
                title,
                sharedViewModel.parsePriority(mPriority),
                description,
                dateFull
            )
            toDoViewModel.updateData(updateItem)
            Toast.makeText(requireContext(), "Successfully updated.", Toast.LENGTH_SHORT).show()
            //Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out al fields.", Toast.LENGTH_SHORT)
                .show()
        }
    }


}