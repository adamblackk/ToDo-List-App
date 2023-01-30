package com.example.todoapp.fragment.alarm

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.adapters.TimePickerBindingAdapter.getHour
import androidx.databinding.adapters.TimePickerBindingAdapter.getMinute
import com.example.todoapp.MainActivity
import com.example.todoapp.databinding.FragmentAlarmBinding
import com.example.todoapp.util.AlarmReceiver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar


class AlarmFragment : BottomSheetDialogFragment() {

    private lateinit var picker:MaterialTimePicker
    private lateinit var calendar:Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private  var _binding: FragmentAlarmBinding?=null
    private val binding get() =_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        creatNotification()

        _binding=FragmentAlarmBinding.inflate(inflater,container,false)

        binding.selectTimeBtn.setOnClickListener {

            showTimePicker()
        }
        binding.setAlarmBtn.setOnClickListener {
            setAlarm()
        }
        binding.cancelAlarmBtn.setOnClickListener {

        }

        return binding.root
    }

    private fun setAlarm() {
        alarmManager= context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(),AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntent
        )

        Toast.makeText(requireContext(),"Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }


    private fun showTimePicker() {
        picker=MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(23)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()


        picker.show(requireActivity().supportFragmentManager,"alarm")
        picker.addOnPositiveButtonClickListener(View.OnClickListener {

                binding.selectedTime.text="${picker.hour}"+ ":"+"${picker.minute}"
        })


        calendar = Calendar.getInstance()
       val hour=calendar.get(Calendar.HOUR_OF_DAY)
        val minute=calendar.get(Calendar.MINUTE)


    }

    private fun creatNotification() {
        val name="TodoAppReminderChannel"
        val description="Channel for TodoApp alarm"
        val importance=NotificationManager.IMPORTANCE_HIGH
        val channel=NotificationChannel("alarm",name,importance)
        channel.description=description

        val notificationManager=requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}