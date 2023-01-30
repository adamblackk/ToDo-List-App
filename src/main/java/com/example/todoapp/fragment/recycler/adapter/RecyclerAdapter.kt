package com.example.todoapp.fragment.recycler.adapter

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.view.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.RecyclerData
import com.example.todoapp.data.models.RecyclerDataModel
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.RecyclerRowLayoutBinding
import com.example.todoapp.util.SaveData
import com.example.todoapp.util.observeOnce
import com.example.todoapp.viewmodel.RecyclerDataViewModel
import com.example.todoapp.viewmodel.SharedViewModel
import com.example.todoapp.viewmodel.ToDoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.time.Duration.Companion.milliseconds

class RecyclerAdapter(
    private val requireActivity: FragmentActivity,
    private val recyclerDataViewModel: RecyclerDataViewModel,
    private val toDoViewModel: ToDoViewModel,
    private val sharedViewModel: SharedViewModel
    ):
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(),ActionMode.Callback {
    private lateinit var saveData: SaveData
    private lateinit var myShare: SharedPreferences
    private var multiSelection=false
    private lateinit var mActionMode: ActionMode
    private var selectedItem= arrayListOf<RecyclerData>()
    var recyclerList= emptyList<RecyclerData>()
    private lateinit var rootView:View
    private var myViewHolder= arrayListOf<MyViewHolder>()

    class MyViewHolder(val binding:RecyclerRowLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(recyclerData: RecyclerData){
            binding.recyclerData=recyclerData
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): RecyclerAdapter.MyViewHolder {
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding= RecyclerRowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolder.add(holder)
        rootView=holder.itemView.rootView


        val currentItem=recyclerList[position]
        holder.bind(currentItem)


        saveItemStateScroll(currentItem,holder)
        /**
         * Single Click Listener
         */
        holder.binding.rowLayout.setOnClickListener {
            if (multiSelection){
                applySelection(holder,currentItem)
            }

        }
        /**
         * Long Click Listener
         */
        holder.binding.rowLayout.setOnLongClickListener {
            requireActivity.startActionMode(this)
            if (!multiSelection){
                multiSelection=true
                applySelection(holder,currentItem)

            }else{
                applySelection(holder,currentItem)
            }


            return@setOnLongClickListener true
        }

    }



    private fun saveItemStateScroll(currentItem: RecyclerData,holder: MyViewHolder){
        if (selectedItem.contains(currentItem)){
            changeRecipesStyle(holder,R.color.dark_gray)
        }else{
            changeRecipesStyle(holder,R.color.light_gray)
        }

    }
    private fun applySelection(holder: MyViewHolder, currentItem: RecyclerData) {
        if (selectedItem.contains(currentItem)){
            changeRecipesStyle(holder, R.color.light_gray)
            selectedItem.remove(currentItem)
            applyActionModeTitle()
        }else{
            selectedItem.add(currentItem)
            changeRecipesStyle(holder,R.color.red)
            applyActionModeTitle()
        }

    }

    private fun applyActionModeTitle() {
        when(selectedItem.size){
            0 ->{
                mActionMode.finish()
               multiSelection=false
            }
            1 ->{
                mActionMode.title="${selectedItem.size} note selected"
            }
            else -> {
                mActionMode.title="${selectedItem.size} notes selected"
            }
        }
    }

    private fun changeRecipesStyle(holder:MyViewHolder,strokeColor:Int){

        holder.binding.cardviewLayout.strokeColor=
            ContextCompat.getColor(requireActivity,strokeColor)


    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(toDoData:List<RecyclerData>){
        this.recyclerList=toDoData
        notifyDataSetChanged()

    }
    override fun getItemCount(): Int {
        return recyclerList.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.contextual_menu,menu)
        mActionMode=actionMode!!
        //burası--
        requireActivity.window.statusBarColor=
            ContextCompat.getColor(requireActivity,R.color.commonDark)

        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
       return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId==R.id.delete_item_recycler){
            selectedItem.forEach {
                val a=it
                val toDoData=ToDoData(
                    a.recyclerDataModel.id,
                    a.recyclerDataModel.title,
                    a.recyclerDataModel.priority,
                    a.recyclerDataModel.description,
                    a.recyclerDataModel.date
                )
                toDoViewModel.insertData(toDoData)
                recyclerDataViewModel.deleteRecyclerData(it)

            }
            showSnackBar("${selectedItem.size} Recipe/s recalled.")
            //multiSelection=false
            selectedItem.clear()
            actionMode?.finish()
        }else{
            selectedItem.forEach{
                recyclerDataViewModel.deleteRecyclerData(it)
            }
            showSnackBar("${selectedItem.size} Recipe/s removed permanently.")
            selectedItem.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolder.forEach{
            changeRecipesStyle(it, R.color.light_gray)

        }
        multiSelection=false
        selectedItem.clear()

        applyStatusBarColor()

    }
    private fun showSnackBar(s: String) {
        Snackbar.make(rootView,s, Snackbar.LENGTH_SHORT).setAction("Okay"){}.show()
    }
    private fun applyStatusBarColor(){
        myShare=sharedViewModel.sharedPreferences(requireActivity.application)
        val selectedTheme=myShare.getInt("theme",0)
        saveData= SaveData(requireActivity)
        if (saveData.loadDarkModeState()){
            requireActivity.window.statusBarColor=
                ContextCompat.getColor(requireActivity,R.color.commonDark)
        }else{
            when(selectedTheme){
                1 -> {
                    requireActivity.window.statusBarColor=
                        ContextCompat.getColor(requireActivity,R.color.green)
                }
                2 -> {
                    requireActivity.window.statusBarColor=
                        ContextCompat.getColor(requireActivity,R.color.theme1ColorPrimaryDark)
                }
                3 -> {
                    requireActivity.window.statusBarColor=
                        ContextCompat.getColor(requireActivity,R.color.theme2ColorSecondary)
                }
                4 ->{
                    requireActivity.window.statusBarColor=
                        ContextCompat.getColor(requireActivity,R.color.theme3ColorPrimaryDark)
                }
                5 ->{
                    requireActivity.window.statusBarColor=
                        ContextCompat.getColor(requireActivity,R.color.theme1ColorPrimaryDark)
                }


            }
        }



    }
    fun clearContextualActionMode(){
        if (this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}