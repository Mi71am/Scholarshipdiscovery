package com.example.scholarshipdiscovery.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.scholarshipdiscovery.R
import com.example.scholarshipdiscovery.data.model.ApplicationRecord
import com.example.scholarshipdiscovery.databinding.FragmentScholarshipDetailsBinding
import com.example.scholarshipdiscovery.ui.viewmodel.ScholarshipViewModel
import com.example.scholarshipdiscovery.utils.EligibilityStatus

class ScholarshipDetailsFragment : Fragment() {

    private var _binding: FragmentScholarshipDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ScholarshipDetailsFragmentArgs by navArgs()
    private val viewModel: ScholarshipViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScholarshipDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scholarship = args.scholarship

        binding.detailsImageView.load(scholarship.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.bg_chip_soft)
            error(R.drawable.bg_chip_soft)
        }

        binding.detailsTitleTextView.text = scholarship.title
        binding.detailsProviderTextView.text = scholarship.provider
        binding.detailsAmountTextView.text = "Award: KSh ${scholarship.amount} (${scholarship.fundingType})"
        binding.detailsDeadlineTextView.text = "Application Deadline: ${scholarship.deadline}"
        binding.detailsDescriptionTextView.text = scholarship.description
        binding.detailsBenefitsTextView.text = scholarship.benefits
        binding.detailsDocumentsTextView.text = scholarship.requiredDocuments

        val profile = viewModel.userProfile.value
        val levelMatch = if (scholarship.eligibleEducationLevel.equals("All", true) || scholarship.eligibleEducationLevel.equals(profile?.educationLevel, true)) "✅ Meets requirement (${scholarship.eligibleEducationLevel})" else "❌ Does not meet (${scholarship.eligibleEducationLevel} required)"
        val fieldMatch = if (scholarship.eligibleFieldOfStudy.equals("All", true) || scholarship.eligibleFieldOfStudy.contains(profile?.fieldOfStudy ?: "", true)) "✅ Meets requirement (${scholarship.eligibleFieldOfStudy})" else "❌ Does not meet (${scholarship.eligibleFieldOfStudy} required)"
        val gpaMatch = if ((profile?.gpa ?: 0.0) >= scholarship.minGpa) "✅ Meets requirement (Min GPA: ${scholarship.minGpa})" else "❌ Does not meet (Min GPA: ${scholarship.minGpa})"
        val countryMatch = if (scholarship.eligibleCountry.equals("Global", true) || scholarship.eligibleCountry.equals(profile?.country, true)) "✅ Meets requirement (${scholarship.eligibleCountry})" else "⚠️ Restricted to ${scholarship.eligibleCountry}"

        val reqText = "• Country Requirement: $countryMatch\n" +
                "• Education Level: $levelMatch\n" +
                "• Field of Study: $fieldMatch\n" +
                "• Academic GPA: $gpaMatch"
        binding.detailsRequirementsTextView.text = reqText

        val status = viewModel.checkEligibility(scholarship)
        binding.detailsEligibilityBadge.text = status.label

        when (status) {
            EligibilityStatus.ELIGIBLE -> {
                binding.detailsEligibilityBadge.setBackgroundResource(R.drawable.bg_badge_eligible)
                binding.detailsEligibilityBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.eligible_green))
            }
            EligibilityStatus.NOT_ELIGIBLE -> {
                binding.detailsEligibilityBadge.setBackgroundResource(R.drawable.bg_badge_ineligible)
                binding.detailsEligibilityBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.error_red))
            }
            EligibilityStatus.NEEDS_MORE_INFO -> {
                binding.detailsEligibilityBadge.setBackgroundResource(R.drawable.bg_chip_soft)
                binding.detailsEligibilityBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.soft_grey))
            }
        }

        viewModel.savedScholarshipIds.observe(viewLifecycleOwner) { savedIds ->
            if (savedIds.contains(scholarship.id)) {
                binding.saveScholarshipButton.text = "Remove from Saved"
                binding.saveScholarshipButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error_red))
            } else {
                binding.saveScholarshipButton.text = "Save Scholarship"
                binding.saveScholarshipButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal))
            }
        }

        binding.saveScholarshipButton.setOnClickListener {
            viewModel.toggleSaveScholarship(scholarship.id)
            Toast.makeText(requireContext(), "Saved status updated!", Toast.LENGTH_SHORT).show()
        }

        binding.addToTrackerButton.setOnClickListener {
            val record = ApplicationRecord(
                scholarshipTitle = scholarship.title,
                deadline = scholarship.deadline,
                dateAdded = "Today",
                status = "Planning to apply",
                notes = "Saved from scholarship details."
            )
            viewModel.addApplication(record)
            Toast.makeText(requireContext(), "Added to Application Tracker!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.applicationTrackerFragment)
        }

        binding.visitWebsiteButton.setOnClickListener {
            val url = if (scholarship.websiteUrl.startsWith("http")) scholarship.websiteUrl else "https://${scholarship.websiteUrl}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
