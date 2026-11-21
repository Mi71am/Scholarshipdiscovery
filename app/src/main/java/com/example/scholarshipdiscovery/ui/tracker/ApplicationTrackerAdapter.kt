package com.example.scholarshipdiscovery.ui.tracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scholarshipdiscovery.R
import com.example.scholarshipdiscovery.data.model.ApplicationRecord
import com.example.scholarshipdiscovery.databinding.ItemApplicationRecordBinding

class ApplicationTrackerAdapter(
    private val onEditClick: (ApplicationRecord) -> Unit,
    private val onDeleteClick: (ApplicationRecord) -> Unit
) : ListAdapter<ApplicationRecord, ApplicationTrackerAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemApplicationRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemApplicationRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ApplicationRecord) {
            binding.titleTextView.text = item.scholarshipTitle
            binding.deadlineTextView.text = "Deadline: ${item.deadline} (Added ${item.dateAdded})"
            binding.notesTextView.text = if (item.notes.isEmpty()) "No personal notes." else item.notes
            binding.statusBadgeTextView.text = item.status

            val context = binding.root.context
            when (item.status) {
                "Successful" -> {
                    binding.statusBadgeTextView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.soft_teal)
                    binding.statusBadgeTextView.setTextColor(ContextCompat.getColor(context, R.color.eligible_green))
                }
                "Unsuccessful" -> {
                    binding.statusBadgeTextView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_red)
                    binding.statusBadgeTextView.setTextColor(ContextCompat.getColor(context, R.color.error_red))
                }
                "Submitted" -> {
                    binding.statusBadgeTextView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.soft_teal)
                    binding.statusBadgeTextView.setTextColor(ContextCompat.getColor(context, R.color.teal))
                }
                else -> {
                    binding.statusBadgeTextView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.off_white)
                    binding.statusBadgeTextView.setTextColor(ContextCompat.getColor(context, R.color.deep_navy))
                }
            }

            binding.editButton.setOnClickListener { onEditClick(item) }
            binding.deleteButton.setOnClickListener { onDeleteClick(item) }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ApplicationRecord>() {
            override fun areItemsTheSame(oldItem: ApplicationRecord, newItem: ApplicationRecord): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ApplicationRecord, newItem: ApplicationRecord): Boolean = oldItem == newItem
        }
    }
}
