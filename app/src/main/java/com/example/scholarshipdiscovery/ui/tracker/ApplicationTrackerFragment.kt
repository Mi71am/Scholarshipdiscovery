package com.example.scholarshipdiscovery.ui.tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scholarshipdiscovery.R
import com.example.scholarshipdiscovery.data.model.ApplicationRecord
import com.example.scholarshipdiscovery.databinding.FragmentApplicationTrackerBinding
import com.example.scholarshipdiscovery.ui.viewmodel.ScholarshipViewModel
import com.google.android.material.textfield.TextInputEditText

class ApplicationTrackerFragment : Fragment() {

    private var _binding: FragmentApplicationTrackerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScholarshipViewModel by activityViewModels()

    private val statusOptions = listOf("Planning to apply", "In progress", "Submitted", "Successful", "Unsuccessful")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApplicationTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ApplicationTrackerAdapter(
            onEditClick = { record -> showAddEditDialog(record) },
            onDeleteClick = { record -> viewModel.deleteApplication(record) }
        )

        binding.trackerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.trackerRecyclerView.adapter = adapter

        viewModel.trackedApplications.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)

            binding.planningCountTextView.text = list.count { it.status == "Planning to apply" }.toString()
            binding.inProgressCountTextView.text = list.count { it.status == "In progress" }.toString()
            binding.submittedCountTextView.text = list.count { it.status == "Submitted" || it.status == "Successful" }.toString()

            if (list.isEmpty()) {
                binding.trackerEmptyStateLayout.visibility = View.VISIBLE
                binding.trackerRecyclerView.visibility = View.GONE
            } else {
                binding.trackerEmptyStateLayout.visibility = View.GONE
                binding.trackerRecyclerView.visibility = View.VISIBLE
            }
        }

        binding.addApplicationFab.setOnClickListener {
            showAddEditDialog(null)
        }

        binding.addApplicationEmptyButton.setOnClickListener {
            showAddEditDialog(null)
        }
    }

    private fun showAddEditDialog(existingRecord: ApplicationRecord?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_edit_application, null)
        val titleEditText = dialogView.findViewById<TextInputEditText>(R.id.dialogTitleEditText)
        val deadlineEditText = dialogView.findViewById<TextInputEditText>(R.id.dialogDeadlineEditText)
        val notesEditText = dialogView.findViewById<TextInputEditText>(R.id.dialogNotesEditText)
        val statusSpinner = dialogView.findViewById<Spinner>(R.id.dialogStatusSpinner)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitleTextView)

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusOptions)
        statusSpinner.adapter = spinnerAdapter

        if (existingRecord != null) {
            dialogTitle.text = "Edit Application Record"
            titleEditText.setText(existingRecord.scholarshipTitle)
            deadlineEditText.setText(existingRecord.deadline)
            notesEditText.setText(existingRecord.notes)
            val idx = statusOptions.indexOf(existingRecord.status)
            if (idx >= 0) statusSpinner.setSelection(idx)
        } else {
            dialogTitle.text = "Add Application Record"
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = titleEditText.text.toString().trim()
                val deadline = deadlineEditText.text.toString().trim()
                val notes = notesEditText.text.toString().trim()
                val status = statusSpinner.selectedItem.toString()

                if (title.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter scholarship name.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (existingRecord != null) {
                    val updated = existingRecord.copy(
                        scholarshipTitle = title,
                        deadline = if (deadline.isEmpty()) "2026-12-31" else deadline,
                        status = status,
                        notes = notes
                    )
                    viewModel.updateApplication(updated)
                } else {
                    val newRecord = ApplicationRecord(
                        scholarshipTitle = title,
                        deadline = if (deadline.isEmpty()) "2026-12-31" else deadline,
                        dateAdded = "Today",
                        status = status,
                        notes = notes
                    )
                    viewModel.addApplication(newRecord)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
