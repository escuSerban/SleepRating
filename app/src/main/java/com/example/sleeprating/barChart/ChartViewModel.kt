package com.example.sleeprating.barChart

import androidx.lifecycle.*
import com.example.sleeprating.database.SleepDatabaseDao
import com.example.sleeprating.database.SleepNight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ViewModel for ChartFragment.
 * @param sleepNightKey The key of the current night we are working on.
 */
class ChartViewModel(
    private val sleepNightKey: Long = 0L,
    dataSource: SleepDatabaseDao
) : ViewModel() {

    val database = dataSource

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val sleepRecords = MediatorLiveData<List<SleepNight>>()

    fun getSleepRecords() = sleepRecords

    init {
        uiScope.launch {
        sleepRecords.addSource(database.getAllNights(), sleepRecords::setValue)
        }
    }

    /**
     * When true immediately navigate back to the [SleepTrackerFragment]
     */
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    /**
     * Call this immediately after navigating to [SleepTrackerFragment]
     */
    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    fun goBack() {
        _navigateToSleepTracker.value = true
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