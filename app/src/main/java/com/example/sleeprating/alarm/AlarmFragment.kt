package com.example.sleeprating.alarm

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sleeprating.R
import com.example.sleeprating.databinding.FragmentAlarmBinding
import java.util.*

/**
 * Fragment that displays our Alarm options and expands the [TimePicker]
 */
class AlarmFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding:  FragmentAlarmBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_alarm, container, false)

        // Initialization of ViewModel associated with this fragment.
        val alarmViewModel: AlarmViewModel by lazy {
            ViewModelProvider(requireActivity()).get(AlarmViewModel::class.java)
        }

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
                val calendar = Calendar.getInstance()
                // Set time (from milliseconds)
                calendar.timeInMillis = clockTime

                val dialog = TimePickerDialog(
                    activity,
                    alarmViewModel.getTimePickerListener(),
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
                alarmViewModel.cancelAlarm()
                alarmViewModel.doneCancelling()
            }
        })
        return binding.root
    }
}