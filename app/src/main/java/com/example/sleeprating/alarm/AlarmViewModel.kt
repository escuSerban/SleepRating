package com.example.sleeprating.alarm

import androidx.lifecycle.*
import kotlinx.coroutines.*

class AlarmViewModel(alarmText: String) : ViewModel() {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    /** Coroutine setup variables */
    private val _updateAlarmtext = MutableLiveData<String>()
    val updateAlarmText: LiveData<String>
     get() = _updateAlarmtext

    init {
        _updateAlarmtext.value = alarmText
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

    fun donePicking() {
        _showTimePicker.value = null
    }

    fun onAlarmSet() {
        _showTimePicker.value = true
    }

    fun doneCancelling() {
        _cancelAlarm.value = null
    }

    fun onAlarmCancel() {
        _cancelAlarm.value = true
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