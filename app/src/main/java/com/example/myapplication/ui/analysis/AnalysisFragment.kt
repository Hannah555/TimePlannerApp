package com.example.myapplication.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.model.ActivityData
import com.example.myapplication.data.remote.FirebaseHandlerImpl
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_analysis.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AnalysisFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnalysisFragment : Fragment(), AnalysisContract.View {
    // TODO: Rename and change types of parameters
    protected lateinit var rootview: View
    private lateinit var chart: CombinedChart
    private lateinit var barEntryArrayList: ArrayList<BarEntry>
    private lateinit var label: ArrayList<String>
    private lateinit var activityList: ArrayList<ActivityData>
    private lateinit var presenter: AnalysisContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_analysis, container, false)
        chart = rootview.findViewById(R.id.combineChart)

//        Set Presenter (setPresenter(HomePresenter(this, FirebaseHandlerImpl()))
        setPresenter(AnalysisPresenter(this, FirebaseHandlerImpl()))

        presenter.showDoneData()

        return rootview
    }

    override fun getDoneData(
        dataList: ArrayList<String>,
        barList: ArrayList<BarEntry>,
        lineList: ArrayList<Entry>
    ) {
        val barDataSet = BarDataSet(barList, "data")
        val lineDataSet = LineDataSet(lineList, "data")
        val barData = BarData(barDataSet)
        val lineData = LineData(lineDataSet)

        val combineData = CombinedData()
        combineData.setData(barData)
        combineData.setData(lineData)

        chart.data = combineData

//        Format chart
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.axisRight.setDrawLabels(false)
        chart.xAxis.setDrawGridLines(false)
        chart.setDrawBorders(false)
        chart.axisLeft.setDrawAxisLine(false)
        chart.axisRight.setDrawAxisLine(false)

//        Format x-axis
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
//        xAxis.labelRotationAngle = (+90).toFloat()
        xAxis.valueFormatter = IndexAxisValueFormatter(dataList)

        barDataSet.color = resources.getColor(R.color.colorAccent)

        chart.animateY(2000)
    }

    override fun setPresenter(presenter: AnalysisContract.Presenter) {
        this.presenter = presenter
    }

}