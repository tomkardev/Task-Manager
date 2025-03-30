package com.example.taskmanagerapp.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.adapter.TasksAdapter
import com.example.taskmanagerapp.databinding.FragmentHomeBinding
import com.example.taskmanagerapp.models.TaskData
import com.example.taskmanagerapp.utils.NetworkResult
import com.example.taskmanagerapp.utils.SwipeToDeleteCallback
import com.example.taskmanagerapp.viewmodels.TaskViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pieChart: PieChart
    val taskAdapter = TasksAdapter()
    var compTask: Int? = null
    var pending: Int? = null
    var totalTask: Int = 0
    var compPercent: Double? = null
    var pendPercent: Double? = null
    private val taskViewModel by activityViewModels<TaskViewModel>()
    private var itemList = mutableListOf<TaskData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel.getAllTasks()
        taskViewModel.getCompTasks()
        taskViewModel.getPendTasks()

        binding.fabBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }



        taskViewModel.getAllTaskLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    binding.constraintLayout.visibility = View.VISIBLE

                    binding.shimmerLayout.visibility = View.GONE


                    createPieChart()

                    val data = it.data
                    if (data != null) {
                        if (data.isNotEmpty()) {
                            setVisibility(true)
                            taskAdapter.submitList(data)
                            totalTask = data.size

                            itemList = data as MutableList<TaskData>

                        } else {
                            setVisibility(false)
                        }
                    }


                }

                is NetworkResult.Loading -> {
                    binding.constraintLayout.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.startShimmer()

                }

                is NetworkResult.Error -> {
                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                }

            }
        })

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        binding.recyclerView.layoutManager = linearLayoutManager

        binding.recyclerView.adapter = taskAdapter
        if (taskAdapter.getItemCount() > 0) {
            binding.recyclerView.scrollToPosition(0); // Scroll to the first item in the data
        }

        // Create the Spinner programmatically
        val spinner = binding.spinner

        // Populate the Spinner with data
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Set OnItemSelectedListener for the Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as String

                when (selectedItem) {
                    "All Tasks" -> {
                        taskViewModel.getAllTasks()
                    }

                    "By Priority" -> {
                        taskViewModel.getTaskByPriority()
                        taskAdapter.notifyDataSetChanged()
                    }

                    "By Alphabetically" -> {
                        taskViewModel.getTaskAplh()
                        taskAdapter.notifyDataSetChanged()
                    }

                    "By Due Date" -> {
                        taskViewModel.getTaskByDue()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.filterData.setOnClickListener {
            spinner.visibility = View.VISIBLE
            spinner.performClick()
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedItem = itemList.removeAt(position)

                val job = CoroutineScope(Dispatchers.Main).launch {
                    taskViewModel.deleteTask(deletedItem)
                    delay(3000)
                    taskAdapter.notifyDataSetChanged()
                }


                Snackbar.make(
                    binding.recyclerView,
                    "${deletedItem.title} deleted",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Undo") {
                        job.cancel()
                        itemList.add(position, deletedItem)
                        taskAdapter.notifyDataSetChanged()
                        binding.recyclerView.scrollToPosition(position)
                    }
                    .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)



    }

    override fun onResume() {
        super.onResume()

        createPieChart()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createPieChart() {

        pieChart = binding.pieChart

        // on below line we are setting user percent value,
        // setting description as enabled and offset for pie chart
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.setDragDecelerationFrictionCoef(0.95f)

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        pieChart.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)



        taskViewModel.getCompLiveData.observe(viewLifecycleOwner, Observer {
            compTask = it.size

            taskViewModel.getPendLiveData.observe(viewLifecycleOwner, Observer {
                pending = it.size

                fun calculatePercentage(part: Double): Double {
                    return if (totalTask >= 0) (part / totalTask) * 100 else 0.0
                }



                if (compTask != null) {
                    compPercent = calculatePercentage(compTask!!.toDouble())


                } else {
                    compPercent = 0.0
                }

                if (pending != null) {
                    pendPercent = calculatePercentage(pending!!.toDouble())

                } else {
                    pendPercent = 0.0
                }


                val entries: ArrayList<PieEntry> = ArrayList()

                entries.add(PieEntry(compPercent!!.toFloat()))
                entries.add(PieEntry(pendPercent!!.toFloat()))


                // on below line we are setting pie data set
                val dataSet = PieDataSet(entries, "Your Tasks")

                // on below line we are setting icons.
                dataSet.setDrawIcons(false)

                // on below line we are setting slice for pie
                dataSet.sliceSpace = 3f
                dataSet.iconsOffset = MPPointF(0f, 40f)
                dataSet.selectionShift = 5f

                // add a lot of colors to list
                val colors: ArrayList<Int> = ArrayList()
                colors.add(resources.getColor(R.color.sky_blue))
                colors.add(resources.getColor(R.color.dark_grey))

                // on below line we are setting colors.
                dataSet.colors = colors

                // on below line we are setting pie data set
                val data = PieData(dataSet)
                data.setValueFormatter(PercentFormatter())
                data.setValueTextSize(15f)
                data.setValueTypeface(Typeface.DEFAULT_BOLD)
                data.setValueTextColor(R.color.low)
                pieChart.setData(data)

                // undo all highlights
                pieChart.highlightValues(null)

                // loading chart
                pieChart.invalidate()

            })

        })


    }

    private fun setVisibility(flag: Boolean) {

        if (flag) {

            binding.noTaskdatalayout.root.visibility = View.GONE
            binding.constraintLayout.visibility = View.VISIBLE

        } else {
            binding.noTaskdatalayout.root.visibility = View.VISIBLE
            binding.constraintLayout.visibility = View.GONE
        }
    }


}