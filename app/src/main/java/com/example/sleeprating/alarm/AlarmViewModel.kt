package com.example.sleeprating.alarm

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Job
import java.util.*

/**
 * ViewModel for AlarmFragment.
 */
class AlarmViewModel(
    application: Application,
    handle: SavedStateHandle
) : AndroidViewModel(application) {

    companion object{
        private const val ALARM_KEY = "alarmText"
    }

    private val savedStateHandle = handle

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    // Required to manipulate the Alarm Manager.
    private val activity = application

    /** Coroutine setup variables */
    private var _alarmText: MutableLiveData<String> = savedStateHandle.getLiveData(ALARM_KEY)
    val alarmText: LiveData<String>
        get() = _alarmText

    init {
        _alarmText.value = savedStateHandle.get<String>(ALARM_KEY) ?: "No Alarm set"
    }

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    private val _showTimePicker = MutableLiveData<Boolean?>()
    val showTimePickerEvent: LiveData<Boolean?>
        get() = _showTimePicker

    private val _cancelAlarm = MutableLiveData<Boolean?>()
    val cancelAlarm: LiveData<Boolean?>
        get() = _cancelAlarm

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    fun onClose() {
        _navigateToSleepTracker.value = true
    }

    /**
     * Method used to handle the listener for TimePickerDialog
     * and also to update alarm status text.
     */
     fun getTimePickerListener() =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val cal = Calendar.getInstance()
            cal[Calendar.HOUR_OF_DAY] = hourOfDay
            cal[Calendar.MINUTE] = minute

            var timeText = "Alarm set for:"
            timeText += DateFormat.format(" HH:mm", cal.time)
            _alarmText.value = timeText

            startAlarm(cal)
        }

    fun donePicking() {
        _showTimePicker.value = null
    }

    /**
     * Here we set an AlarmManager to fire a notification alarm
     * once the desired time has been selected.
     */
    private fun startAlarm(c: Calendar) {
        val alarmManager =
            activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val alarmIntent = Intent(activity, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, 1, alarmIntent, 0)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    fun onAlarmSet() {
        _showTimePicker.value = true
    }

    /**
     * This implementation allows us to cancel the alarm
     * and to update alarm status text.
     */
    fun cancelAlarm() {
        val alarmManager =
            activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(activity, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, 1, intent, 0)
        alarmManager!!.cancel(pendingIntent)
        _alarmText.value = "Alarm cancelled"
    }

    fun onAlarmCancel() {
        _cancelAlarm.value = true
    }

    fun doneCancelling() {
        _cancelAlarm.value = null
    }

    /**
     * Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work.
     * onCleared() gets called when the ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}