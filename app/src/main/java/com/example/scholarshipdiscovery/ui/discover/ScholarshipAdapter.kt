package com.example.scholarshipdiscovery.ui.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.scholarshipdiscovery.R
import com.example.scholarshipdiscovery.data.model.Scholarship
import com.example.scholarshipdiscovery.databinding.ItemScholarshipBinding
import com.example.scholarshipdiscovery.utils.EligibilityStatus

class ScholarshipAdapter(
    private val savedIds: Set<Int>,
    private val checkEligibility: (Scholarship) -> EligibilityStatus,
    private val onItemClick: (Scholarship) -> Unit,
    private val onBookmarkClick: (Scholarship) -> Unit
) : ListAdapter<Scholarship, ScholarshipAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScholarshipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemScholarshipBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Scholarship) {
            binding.scholarshipImageView.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.bg_chip_soft)
                error(R.drawable.bg_chip_soft)
            }

            binding.titleTextView.text = item.title
            binding.providerTextView.text = item.provider
            binding.categoryChip.text = item.category
            binding.descriptionTextView.text = item.description
            binding.amountTextView.text = "KSh ${item.amount} • ${item.fundingType}"
            binding.deadlineTextView.text = "📅 ${item.deadline}"

            val status = checkEligibility(item)
            binding.eligibilityBadge.text = status.label

            val context = binding.root.context
            when (status) {
                EligibilityStatus.ELIGIBLE -> {
                    binding.eligibilityBadge.setBackgroundResource(R.drawable.bg_badge_eligible)
                    binding.eligibilityBadge.setTextColor(ContextCompat.getColor(context, R.color.eligible_green))
                }
                EligibilityStatus.NOT_ELIGIBLE -> {
                    binding.eligibilityBadge.setBackgroundResource(R.drawable.bg_badge_ineligible)
                    binding.eligibilityBadge.setTextColor(ContextCompat.getColor(context, R.color.error_red))
                }
                EligibilityStatus.NEEDS_MORE_INFO -> {
                    binding.eligibilityBadge.setBackgroundResource(R.drawable.bg_chip_soft)
                    binding.eligibilityBadge.setTextColor(ContextCompat.getColor(context, R.color.soft_grey))
                }
            }

            val isSaved = savedIds.contains(item.id)
            if (isSaved) {
                binding.bookmarkButton.setColorFilter(ContextCompat.getColor(context, R.color.teal))
            } else {
                binding.bookmarkButton.setColorFilter(ContextCompat.getColor(context, R.color.soft_grey))
            }

            binding.bookmarkButton.setOnClickListener { onBookmarkClick(item) }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Scholarship>() {
            override fun areItemsTheSame(oldItem: Scholarship, newItem: Scholarship): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Scholarship, newItem: Scholarship): Boolean = oldItem == newItem
        }
    }
}
