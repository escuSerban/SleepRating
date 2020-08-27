package com.example.sleeprating.barChart

import android.graphics.Color
import androidx.databinding.BindingAdapter
import com.example.sleeprating.database.SleepNight
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

private val ONE_MINUTE_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOUR_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

/**
 * Use this adapter for binding data on our [chart] in XML
 */
@BindingAdapter("barData")
fun setBarData(chart: BarChart, data: List<SleepNight>?) {
    val entries = mutableListOf<BarEntry>()
    data?.forEach { night ->
        val nightId = night.nightId.toString()
        entries.add(
            BarEntry(
                nightId.toFloat(),
                convertDurationFormat(night.startTimeMilli, night.endTimeMilli).toFloat()
            )
        )
    }
    val barSet = BarDataSet(entries, "sleep records")
    barSet.valueTextColor = Color.RED
    barSet.colors = ColorTemplate.PASTEL_COLORS.toList()
    val barData = BarData(barSet)
    barData.setValueFormatter(DurationFormatter())
    chart.data = barData
    chart.setVisibleXRange(10F, entries.size.toFloat())
    chart.notifyDataSetChanged()
    // Enables zooming in and out by gesture
    chart.setScaleEnabled(true)
    // Adds half of the bar width to each side of the x-axis range in order to
    // allow the bars of the barChart to be fully displayed
    chart.setFitBars(true)
    chart.description.text = "time span HH,mm,ss"
    chart.xAxis.setDrawLabels(false)
    chart.description.textColor = Color.RED
    chart.legend.textColor = Color.DKGRAY
    chart.axisLeft.axisLineColor = Color.RED
    chart.xAxis.axisLineColor = Color.BLUE
    chart.axisLeft.setDrawLabels(false)
    chart.axisRight.isEnabled = false
    chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    // Calling this method will refresh our barChart
    chart.invalidate()
}

/**
 * Converts [durationMilli] from milliseconds to seconds and also
 * updates the chart values when they reach a minute / an hour
 */
fun convertDurationFormat(startTimeMilli: Long, endTimeMilli: Long): String {
    val durationMilli = endTimeMilli - startTimeMilli
    return when {
        durationMilli < ONE_MINUTE_MILLIS -> (durationMilli /1000).toString()
        durationMilli < ONE_HOUR_MILLIS -> (durationMilli / 600).toString()
        else -> (durationMilli / 360).toString()
    }
}

/**
 * Returns a formatted value for our [durationMilli]
 */
class DurationFormatter : ValueFormatter() {
    private val format = DecimalFormat("##,##,##")
    override fun getBarLabel(barEntry: BarEntry?): String {
        return format.format(barEntry?.y)
    }
}