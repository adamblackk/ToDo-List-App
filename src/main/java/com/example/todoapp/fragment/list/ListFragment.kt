package com.example.todoapp.fragment.list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.RecyclerData
import com.example.todoapp.data.models.RecyclerDataModel
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.fragment.list.adapter.ListAdapter
import com.example.todoapp.fragment.recycler.RecyclerFragment
import com.example.todoapp.util.SaveData
import com.example.todoapp.util.hideKeyBoard
import com.example.todoapp.util.observeOnce
import com.example.todoapp.viewmodel.RecyclerDataViewModel
import com.example.todoapp.viewmodel.SharedViewModel
import com.example.todoapp.viewmodel.ToDoViewModel
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule


class ListFragment : Fragment(),SearchView.OnQueryTextListener{
    private  var _binding: FragmentListBinding?=null
    private val binding get() =_binding!!
    private lateinit var recyclerData: RecyclerData
    private val toDoViewModel:ToDoViewModel by viewModels()
    private val sharedViewModel:SharedViewModel by viewModels()
    private val recyclerDataViewModel:RecyclerDataViewModel by viewModels()
    private val listAdapter: ListAdapter by lazy{ ListAdapter() }
    private lateinit var saveData: SaveData

    private lateinit var deletedItem:ToDoData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveData=SaveData(requireContext())
        if (saveData.loadDarkModeState()){
            requireContext().setTheme(R.style.DarkTheme_ToDoApp)
        }else{
            requireContext().setTheme(R.style.Theme_ToDoApp)
        }
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        saveData=SaveData(requireContext())
        if (saveData.loadDarkModeState()){
            requireContext().setTheme(R.style.DarkTheme_ToDoApp)
        }else{
            requireContext().setTheme(R.style.Theme_ToDoApp)
        }
        _binding=FragmentListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        binding.sharedViewmodel=sharedViewModel
            // bunu artık data binding ile yapıyorum..
       /* binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        */

        setupRecyclerview()
        toDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            sharedViewModel.checkIfDatabaseEmpty(it)
            listAdapter.setData(it)
            //custom animator için ekledim ama pek verimli olmadı
           // binding.recyclerview.scheduleLayoutAnimation()
        })

        //hide soft keyboard
        hideKeyBoard(requireActivity())
        /* data binding ile yapıldı bindingAdapter sınıfını inceleyebilirsin
        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })

         */

        return binding.root
    }


    private fun setupRecyclerview(){
        binding.recyclerview.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerview.adapter=listAdapter

        binding.recyclerview.itemAnimator= SlideInUpAnimator().apply {
            addDuration=350
        }



        //swipe to delete
        swipeToDelete(binding.recyclerview)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback= object :SwipeToDelete(){
            @SuppressLint("FragmentLiveDataObserve")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deletedItem=listAdapter.toDoList[viewHolder.adapterPosition]
                //deleteItem
                val deletedItemList= listOf(deletedItem)
                toDoViewModel.deleteItem(deletedItem)
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                //restore deleted ıtem
                restoreDeletedItem(viewHolder.itemView,deletedItem)
                toDoViewModel.viewModelScope.launch {
                    delay(2500)
                    toDoViewModel.getAllData.observeOnce(this@ListFragment, Observer {
                        it?.let {
                            if (!it.contains(deletedItem)){
                                val id=deletedItem.id
                                val title = deletedItem.title
                                val priority = deletedItem.priority
                                val description = deletedItem.description
                                val date = deletedItem.date
                                val recyclerDataModel=RecyclerDataModel(id,title,priority,description,date)
                                recyclerData = RecyclerData(recyclerDataModel)
                                recyclerDataViewModel.insertRecyclerData(recyclerData)
                                val i =Intent(Intent(requireContext(),MainActivity::class.java))
                                requireActivity().startActivity(i)
                                requireActivity().finish()

                            }
                        }


                    })
                }


            }
        }
        val itemTouchHelper=ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }
    fun insertRecyclerData(recyclerDataItem: RecyclerData){

    }

    private fun restoreDeletedItem(view:View,deletedItem:ToDoData){

        val snackBar=Snackbar.make(
            view,
            "Deleted ${deletedItem.title}",Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Undo"){

            toDoViewModel.insertData(deletedItem)

            //stagger uyguladıktan sonra prblem oldu o yuzden asagıdaki kodu sildim
            //listAdapter.notifyItemChanged(position)
        }
        snackBar.show()

    }


    /*  data binding ile yapıldı bindingAdapter sınıfını inceleyebilirsin
    private fun showEmptyDatabaseViews(emptyDatabase:Boolean) {
        if (emptyDatabase){
            binding.noDataImageView.visibility=View.VISIBLE
            binding.noDataTextView.visibility=View.VISIBLE
        }else{
            binding.noDataImageView.visibility=View.INVISIBLE
            binding.noDataTextView.visibility=View.INVISIBLE
        }
    }

     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost:MenuHost=requireActivity()
        menuHost.addMenuProvider(object :MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu,menu)

                val search=menu.findItem(R.id.menu_search)
                val searchView=search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled=true
                searchView?.setOnQueryTextListener(this@ListFragment)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.delete_all-> {
                        confirmRemoval()
                              }
                    R.id.menu_priority_high -> toDoViewModel.sortByHighPriority.observe(viewLifecycleOwner,
                        Observer { listAdapter.setData(it) })
                    R.id.menu_priority_low -> toDoViewModel.sortByLowPriority.observe(viewLifecycleOwner,
                        Observer { listAdapter.setData(it) })
                    R.id.change_mode->{
                        findNavController().navigate(R.id.action_listFragment_to_bottomSheetFragment)
                    }
                    R.id.change_color ->{
                        findNavController().navigate(R.id.action_listFragment_to_entryActivity)
                        //requireActivity().finish()
                    }
                    R.id.call_back ->{
                       recyclerDataViewModel.getAllRecyclerData.observeOnce(viewLifecycleOwner, Observer {
                           print("oklklk")
                           if (it.isNotEmpty()){
                               findNavController().navigate(R.id.action_listFragment_to_recyclerFragment)
                           }else{
                               Toast.makeText(requireContext(),"çöp kutusu boş",Toast.LENGTH_SHORT).show()
                           }
                       })

                    }

                    android.R.id.home->requireActivity().onBackPressed()
                }
                return true
            }

        },viewLifecycleOwner,Lifecycle.State.RESUMED)
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!=null){
            searchTroughDatabase(query)
        }
        return true
    }



    override fun onQueryTextChange(query: String?): Boolean {
        if (query!=null){
            searchTroughDatabase(query)
        }
        return true
    }
    private fun searchTroughDatabase(query: String) {
        val searchQuery="%$query%"

        toDoViewModel.searchData(searchQuery).observeOnce(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.setData(it)
            }
        })
    }
    // Show AlertDialog to Comfirm removal all from datatable


    private fun confirmRemoval() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            toDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
                var id: Int = 0
                var title = ""
                var date = ""
                var description = ""
                var priority = Priority.HIGH
                var i:Int=0
                while (i < it.size){

                    id=it[i].id
                    title=it[i].title
                    date=it[i].date
                    priority=it[i].priority
                    description=it[i].description

                    val recyclerDataModel = RecyclerDataModel(id, title, priority, description, date)
                    val recyclerData = RecyclerData(recyclerDataModel)
                    recyclerDataViewModel.insertRecyclerData(recyclerData)
                    i += 1
                }

            })
            toDoViewModel.deleteAllItem()
            Toast.makeText(
                requireContext(),
                "Successfully removed everything!",
                Toast.LENGTH_SHORT
            ).show()
            val i=Intent(Intent(requireContext(),MainActivity::class.java))
            requireActivity().startActivity(i)
            requireActivity().finish()

        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Everything ?")
        builder.setMessage("Are you sure to remove everything  ?")
        builder.create().show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
     _binding=null
    }




}