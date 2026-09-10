//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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

           println("Hello $fullName, The following available Institutions offer scholarships for $course:\n" +
                   "University A\n" +
                   "College B\n" +
                   "University C\n" +
                   "Organization D\n")
           while(true){
           println("Enter your GPA")
           var gpa = readLine()!!.toDouble()

           when(gpa) {
               in 1.0..2.5 -> println(
                   "Sorry, you are not eligible for any schlarship.\n" +
                           "$gpa is below average"
               )

               in 2.6..3.5 -> println("You qualify for 1 partial scholarship to College B\n")
               in 3.6..4.0 -> println(
                   "Your gpa of $gpa qualifies you for the following scholarships:\n" +
                           "                   Scholarship A to University A\n" +
                           "                   Scholarship D to Organization D\n"
               )

               else -> println("Invalid gpa")}
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
           break//this will end the program


       }

   }

}



