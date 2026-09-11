fun main() {
   while(true) {
       println("╔═══════════════════════════════════════════════════════════════════════════════════════════════╗ ")
       println("║                               𝓢𝓒𝓗𝓞𝓛𝓐𝓡𝓢𝓗𝓘𝓟 𝓓𝓘𝓢𝓒𝓞𝓥𝓔𝓡𝓨 𝓢𝓨𝓢𝓣𝓔𝓜                                ║")
       println("╚═══════════════════════════════════════════════════════════════════════════════════════════════╝")

       println(
           "                                       Welcome...\n" +
                   "This system helps students find scholarships they are eligible for.\n\n" +
                   "Select an option to begin from:\n" +
                   "1. Register\n" +
                   "2. Learn more about the system\n" +
                   "3. Exit"
       )
       val option1 = readLine()!!.toInt()


       if (option1 == 1) {
           println("Enter your full name:")
           var fullName = readLine()
           println("Enter your course:")
           var course = readLine()

           println(
               "Hello $fullName, The following available Institutions offer scholarships for $course:\n" +
                       "University A\n" +
                       "College B\n" +
                       "University C\n" +
                       "Organization D\n"
           )

           var gpa = 0.0

           while (true) {
               println("Enter your GPA")
               gpa = readLine()!!.toDouble()

               when (gpa) {
                   in 1.0..2.5 -> {
                       println(
                           "Sorry, you are not eligible for any schlarship.\n" +
                                   "$gpa is below average"
                       )
                       break
                   }

                   in 2.51..3.5 -> {
                       println("You qualify for the following scholarships:\n University Access Scholarship\n")
                       break
                   }


                   in 3.51..4.0 -> {
                       println(
                           "Your gpa of $gpa qualifies you for the following scholarships:\n" +
                                   "                   Technology Education Scholarship\n" +
                                   "                   University Access Scholarship\n" +
                                   "                   Community Support Scholarship\n"
                       )
                       break
                   }

                   else -> {
                       println("Invalid gpa")
                       continue
                   }
               }
           }

           val scholarships = listOf(
               "Technology Education Scholarship",
               "University Access Scholarship",
               "Academic Excellence Scholarship",
               "Community Support Scholarship"
           )

           val applications = mutableListOf<String>()

           println("--------------------------------------------------------------------")
           println("                         SCHOLARSHIP OPTIONS")
           println("--------------------------------------------------------------------")
           println("1. View all scholarships")
           println("2. Apply for a scholarship")
           println("3. Return to main menu")

           val scholarshipOption = readLine()!!.toInt()

           if (scholarshipOption == 1) {

               println("\nAvailable Scholarships:")

               for ((index, scholarship) in scholarships.withIndex()) {
                   println("${index + 1}. $scholarship")
               }

               println("\nSelect 2 to apply for a scholarship or 3 to return to main menu:")
               val nextOption = readLine()!!.toInt()

               if (nextOption == 2) {
                   println("Enter the number of the scholarship you would like to apply for:")
                   val scholarshipNumber = readLine()!!.toInt()

                   if (scholarshipNumber in 1..scholarships.size) {

                       val selectedScholarship = scholarships[scholarshipNumber - 1]

                       applications.add("$fullName - $selectedScholarship")

                       println("----------------------------------------")
                       println("       APPLICATION SUBMITTED")
                       println("----------------------------------------")
                       println("Student: $fullName")
                       println("Course: $course")
                       println("GPA: $gpa")
                       println("Scholarship: $selectedScholarship")
                       println("----------------------------------------")
                       println("Your scholarship application has been recorded.")
                   } else {
                       println("Invalid scholarship selection.")
                   }
               } else if (nextOption == 3) {
                   continue
               }

           } else if (scholarshipOption == 2) {

               println("\nAvailable Scholarships:")

               for ((index, scholarship) in scholarships.withIndex()) {
                   println("${index + 1}. $scholarship")
               }

               println("Enter the number of the scholarship you would like to apply for:")
               val scholarshipNumber = readLine()!!.toInt()

               if (scholarshipNumber in 1..scholarships.size) {

                   val selectedScholarship = scholarships[scholarshipNumber - 1]

                   applications.add("$fullName - $selectedScholarship")

                   println("----------------------------------------")
                   println("       APPLICATION SUBMITTED")
                   println("----------------------------------------")
                   println("Student: $fullName")
                   println("Course: $course")
                   println("GPA: $gpa")
                   println("Scholarship: $selectedScholarship")
                   println("----------------------------------------")
                   println("Your scholarship application has been recorded.")



               } else {
                   println("Invalid scholarship selection.")
               }

           } else if (scholarshipOption == 3) {
               continue
           }





           } else if (option1 == 2) {
               println(
                   " This system helps students find scholarships they are eligible for." +
                           "After registering, students provide information such as their course and GPA\n" +
                           "The system then uses this information to identify schlarships that match\n" +
                           "their qualifications and eligibility requirements.\n"
               )

               println("Select 0 to go back:")
               var goBack = readLine()!!.toInt()
               if (goBack == 0) {
                   continue//this line will take u back to the beginning of the while loop
               }
           } else if (option1 == 3) {
           println("Thank you for using the Scholarship Discovery System.")
               break//this will end the program


           }

       }


   }



