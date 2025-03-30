package com.example.taskmanagerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.databinding.FragmentAddTaskBinding
import com.example.taskmanagerapp.databinding.FragmentHomeBinding
import com.example.taskmanagerapp.models.TaskData
import com.example.taskmanagerapp.viewmodels.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: Date? = null

    private val taskViewModel by activityViewModels<TaskViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val priority_list = resources.getStringArray(R.array.priority_list)

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.priority_dropdown, priority_list)
        val autocompleteTxt = binding.autoCompleteTextView
        autocompleteTxt.setAdapter(arrayAdapter)


        binding.selectDateTxt.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.select_a_date))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            val fragmentManager = parentFragmentManager

            datePicker.show(fragmentManager, "datePicker")

            datePicker.addOnPositiveButtonClickListener {
                val simpleDateFormat =
                    java.text.SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())

                binding.selectDateTxt.text = simpleDateFormat.format(Date(it).time)


                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it
                selectedDate = calendar.time

            }


        }

        binding.submitBtn.setOnClickListener {
            val title = binding.titleEdt.text.toString()
            val description = binding.descEdt.text.toString()
            val date = selectedDate
            var priority: Int? = null
            when (binding.autoCompleteTextView.text.toString()) {
                getString(R.string.low) -> {
                    priority = 0
                }

                getString(R.string.medium) -> {
                    priority = 1
                }

                getString(R.string.high) -> {
                    priority = 2
                }

            }

            if (title.isNotEmpty() && selectedDate != null && priority != null) {

                taskViewModel.addTask(
                    TaskData(
                        null,
                        title,
                        description,
                        priority,
                        selectedDate!!,
                        "Pending"
                    )
                )

                findNavController().navigate(R.id.action_addTaskFragment_to_homeFragment)

                taskViewModel.getAllTasks()


            } else if (selectedDate == null) {
                Toast.makeText(context, "${getString(R.string.select_a_date)}", Toast.LENGTH_SHORT)
                    .show()
            } else if (title.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    "${getString(R.string.pls_enter_title)}",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }


    }

}