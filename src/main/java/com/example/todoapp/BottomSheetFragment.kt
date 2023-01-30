package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.VibrationAttributes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.databinding.FragmentBottomSheetBinding
import com.example.todoapp.util.SaveData
import com.example.todoapp.viewmodel.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragment : BottomSheetDialogFragment() {
    private  var _binding: FragmentBottomSheetBinding?=null
    private val binding get() =_binding!!
    private lateinit var saveData:SaveData

    override fun onCreate(savedInstanceState: Bundle?) {
        saveData= SaveData(requireContext())
        if (saveData.loadDarkModeState()){
            requireContext().setTheme(R.style.DarkTheme_ToDoApp)
        }else {
            requireContext().setTheme(R.style.Theme_ToDoApp)
        }
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        // Inflate the layout for this fragment
        _binding=FragmentBottomSheetBinding.inflate(inflater,container,false)



        if (saveData.loadDarkModeState()){
            binding.switch1.isChecked=true
        }
        val window=requireActivity().window
        val bar=requireActivity().actionBar


        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){

                //window.navigationBarColor=ContextCompat.getColor(requireContext(),R.color.dark_gray)
                //window.statusBarColor=ContextCompat.getColor(requireContext(),R.color.dark_gray)
                saveData.setDarkModeState(true)
                //findNavController().navigate(R.id.action_bottomSheetFragment_to_listFragment)
                val i=Intent(requireContext(),MainActivity::class.java)
                requireActivity().startActivity(i)
                requireActivity().finish()
            }else{
                //window.navigationBarColor=ContextCompat.getColor(requireContext(),R.color.green)
              // window.statusBarColor=ContextCompat.getColor(requireContext(),R.color.green)
                saveData.setDarkModeState(false)
                //findNavController().navigate(R.id.action_bottomSheetFragment_to_listFragment)
                val i=Intent(requireContext(),MainActivity::class.java)
                requireActivity().startActivity(i)
                requireActivity().finish()
            }

            }


        return binding.root
    }


}