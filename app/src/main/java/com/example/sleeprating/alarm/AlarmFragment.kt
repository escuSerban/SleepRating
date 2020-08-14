package com.example.sleeprating.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sleeprating.R
import com.example.sleeprating.databinding.FragmentAlarmBinding
import java.util.*

class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding
    private lateinit var viewModelFactory: AlarmViewModelFactory
    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_alarm, container, false)

        val arguments = AlarmFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = AlarmViewModelFactory(arguments.alarmText)

        // Get a reference to the ViewModel associated with this fragment.
        alarmViewModel = ViewModelProvider(this, viewModelFactory).get(AlarmViewModel::class.java)

        binding.alarmViewModel = alarmViewModel
        binding.lifecycleOwner = this

        val clockTime = 31415926535 // It is:  15:38

        alarmViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AlarmFragmentDirections.actionAlarmFragmentToSleepTrackerFragment()
                )
                alarmViewModel.doneNavigating()
            }
        })

        alarmViewModel.showTimePickerEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                // Get instance of calendar
                val calendar = Calendar.getInstance()

                // Set time (from milliseconds)
                calendar.timeInMillis = clockTime

                // Create dialog and set hours, minutes and 24 hours mode
                val dialog = TimePickerDialog(
                    activity,
                    getTimePickerListener(),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                dialog.show()
                alarmViewModel.donePicking()
            }
        })

        alarmViewModel.cancelAlarm.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                cancelAlarm()
                alarmViewModel.doneCancelling()
            }
        })

        return binding.root
    }

    /**
     * Method used to handle the listener for TimePickerDialog
     * and also to update alarm status text.
     */
    private fun getTimePickerListener() =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

            val cal = Calendar.getInstance()
            cal[Calendar.HOUR_OF_DAY] = hourOfDay
            cal[Calendar.MINUTE] = minute

            // After selecting time - set text in TextView
            var timeText = resources.getString(R.string.alarm_set)
            timeText += DateFormat.format(" HH:mm", cal.time)
            binding.alarmStatus.text = timeText

            startAlarm(cal)
        }

    /**
     * Here we set an AlarmManager to fire a notification alarm
     * once the desired time has been selected.
     */
    private fun startAlarm(c: Calendar) {
        val alarmManager =
            activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val alarmIntent = Intent(activity, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, 1, alarmIntent, 0)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    /**
     * This implementation allows us to cancel the alarm
     * and to update alarm status text.
     */
    private fun cancelAlarm() {
        val alarmManager =
            activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(activity, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, 1, intent, 0)
        alarmManager!!.cancel(pendingIntent)
        binding.alarmStatus.text = resources.getString(R.string.alarm_canceled)
    }
}