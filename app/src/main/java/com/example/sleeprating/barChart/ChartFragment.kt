package com.example.sleeprating.barChart

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sleeprating.R
import com.example.sleeprating.database.SleepDatabase
import com.example.sleeprating.databinding.FragmentChartBinding
import com.example.sleeprating.sleepdetail.SleepDetailFragmentArgs

/**
 * A [Fragment] subclass which contains our sleep stats.
 */
class ChartFragment : Fragment() {

    private lateinit var statsViewModel: ChartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentChartBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chart, container, false
        )

        setHasOptionsMenu(true)
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory
        val arguments = SleepDetailFragmentArgs.fromBundle(requireArguments())
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = ChartViewModelFactory(arguments.sleepNightKey, dataSource)

        statsViewModel = ViewModelProvider(this, viewModelFactory).get(ChartViewModel::class.java)

        binding.chartViewModel = statsViewModel
        binding.lifecycleOwner = this

        (activity as AppCompatActivity?)!!.supportActionBar?.title =
            resources.getString(R.string.stats_title)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        statsViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(
                    ChartFragmentDirections.actionChartFragmentToSleepTrackerFragment()
                )
                statsViewModel.doneNavigating()
            }
        })

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> statsViewModel.goBack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
