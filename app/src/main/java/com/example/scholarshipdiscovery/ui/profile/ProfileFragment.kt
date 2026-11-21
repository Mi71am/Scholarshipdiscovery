package com.example.scholarshipdiscovery.ui.profile

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.scholarshipdiscovery.data.model.UserProfile
import com.example.scholarshipdiscovery.databinding.FragmentProfileBinding
import com.example.scholarshipdiscovery.ui.auth.LoginActivity
import com.example.scholarshipdiscovery.ui.viewmodel.ScholarshipViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScholarshipViewModel by activityViewModels()

    private val fieldOptions = listOf("Engineering & IT", "Business", "Medicine", "Social Sciences", "Law", "Arts", "All")
    private val levelOptions = listOf("Undergraduate", "Postgraduate", "High School", "TVET")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fieldAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, fieldOptions)
        binding.fieldSpinner.adapter = fieldAdapter

        val levelAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, levelOptions)
        binding.educationLevelSpinner.adapter = levelAdapter

        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                binding.fullNameEditText.setText(profile.fullName)
                binding.emailEditText.setText(profile.email)
                binding.countryEditText.setText(profile.country)
                binding.institutionEditText.setText(profile.institution)
                binding.gpaEditText.setText(profile.gpa.toString())

                val fieldIdx = fieldOptions.indexOf(profile.fieldOfStudy)
                if (fieldIdx >= 0) binding.fieldSpinner.setSelection(fieldIdx)

                val levelIdx = levelOptions.indexOf(profile.educationLevel)
                if (levelIdx >= 0) binding.educationLevelSpinner.setSelection(levelIdx)
            }
        }

        binding.saveProfileButton.setOnClickListener {
            val name = binding.fullNameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val country = binding.countryEditText.text.toString().trim()
            val institution = binding.institutionEditText.text.toString().trim()
            val gpaStr = binding.gpaEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your name and email address.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gpaVal = gpaStr.toDoubleOrNull() ?: 3.0

            val updatedProfile = UserProfile(
                id = 1,
                fullName = name,
                email = email,
                country = if (country.isEmpty()) "Kenya" else country,
                institution = institution,
                fieldOfStudy = binding.fieldSpinner.selectedItem.toString(),
                educationLevel = binding.educationLevelSpinner.selectedItem.toString(),
                gpa = gpaVal
            )

            viewModel.saveUserProfile(updatedProfile)
            Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.logoutButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
