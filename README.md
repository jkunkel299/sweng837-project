# SWENG837 Software System Design Course Project

Object-oriented software system design for Course Scheduling System<br><br>
Jessica Kunkel<br>
SU II 2024<br><br>

Presentation Video: https://youtu.be/0l93PW9V4O4<br><br>

Explanations for UML Diagrams and Design Components

## Use Case Diagrams

The primary actors in the Course Scheduling System are Students, Professors, and Course Administrators. Students participate in the "Student Enrollment" use case. Professors participate in the "Professor Chooses Section" use case. Course Administrators participate in the "Enrollment Override," "Manage Conflict," "Add New Course/Section," and "Remove Course/Section" use cases. The secondary actor Registrar provides information for the "Add New Course/Section" and "Remove Course/Section" use cases.

### Student Enrollment

The main success scenario for the Student Enrollment use case is as follows:
1.	Student searches for a class
2.	Student selects one section of the class to add to their schedule
3.	Class is added to schedule
<br>*Steps 1-3 are repeated until the student has added all desired classes to their schedule.*
4.	Student confirms class selections
5.	System enrolls students in their selected classes

### Enrollment Override

The main success scenario for the Enrollment Override use case is as follows:
1.	Course Administrator selects the student account from whom they received the override request
2.	Course Administrator selects the course rule to override
3.	System requests confirmation of the override
4.	Course Administrator confirms the override
5.	Enrollment rule is overridden in only this case
6.	Student is manually added to the class
7.	System notifies the Student of the successful enrollment

### Manage Conflict

The main success scenario for the Manage Conflict use case is as follows:
1.	Course Administrator selects the course section to manage
2.	Course Administrator finds a classroom that is unoccupied for the entire time slot
3.	Course Administrator confirms section change
4.	Section assignment is successfully changed
5.	System notifies students enrolled in that section of the change
6.	System notifies the professor of that section of the change

### Professor Chooses Section

The main success scenario for the Professor Chooses Section use case is as follows:
1.	Professor searches for a class
2.	Professor chooses an open section (time and location) to teach
3.	Class is added to teaching schedule
<br>*Steps 1-3 are repeated until the Professor has added all desired classes to their schedule.*
4.	Professor confirms teaching schedule
5.	Section data is updated with the Professor’s name

### Add New Course/Section

The main success scenario for the Add New Course/Section use case is as follows:
1.	Course Administrator initiates addition of a course or section
2.	Course Administrator adds information about the course provided by Registrar: course ID, description, required number of seats, number of credits, prerequisites, number of sections, etc.
3.	Course Administrator confirms course information
4.	System uses number of seats and course ID rules to assign the class to a classroom and time slot
5.	Course Administrator confirms scheduling

### Remove Course/Section

The main success scenario for the Remove Course/Section use case is as follows:
1.	Course Administrator selects a course or section to delete
2.	Course Administrator confirms the course or section being deleted
3.	System stores course/section data per course archiving rules
4.	System removes the course/section from available courses
5.	System removes the course/section from the schedules of any enrolled students and professors
6.	System notifies students and professors of deletion and need for attention

## Domain Model

The Domain Model begins at the University level. One University has many Buildings and Schools of Study. One to many Buildings belong to one School of Study. One Building contains one to many Classrooms, with attributes like building/location and number of seats. The Preferred Schools Rule has attributes like building, associated school, and preference level, and describes the assignment of Classrooms.<br><br>
Course has attributes like course ID, description, number of credits, number of sections, frequency, prerequisites, school priority, semester, and duration. One Course is extended by one to many Course Sections. Course Section has attributes like section ID, number of seats, and professor, and takes place during a Day/Time (described by day of week, start time, and end time). Course Section is added to Schedule.<br><br>
Student, with attributes like student ID, email, course history, major, and permissions, enrolls in one to many Course Sections. One Student builds one Schedule. <br><br>
Professor, with attributes like employee ID, email, school, qualifications, and permissions, teaches one to many Course Sections. One Professor builds one Schedule. <br><br>
Course Administrator, with attributes like employee ID, email, and permissions, manages one to many Courses and one to many Course Sections.

## Class Diagram

The Class Diagram is derived from and extends the Domain Model.<br><br>
The Abstract User class generalizes the use classes, pulling out common attributes like ID, email, and account permissions. These account permissions have their own datatype of boolean expressions describing whether users can override enrollment, edit courses, or create schedules. In general, Student and Professor classes have the "Create Schedule" permission, while Course Administrators have the "Enroll Override" and "Edit Course" permissions. The User class has methods that allow users to search for courses, add and remove courses from schedules, and enroll and unenroll in courses. Student extends User, with additional attributes like course history, major, and maximum number of credits. Professor extends User, with additional attributes like school and qualifications. Course Administrator extends User and has additional methods that allow the course admin to override enrollment (get user information, override), create new courses, remove courses, manage conflicts (get classrooms, as a search for an available location and time), and confirm all changes being made. The User class as a whole is dependent on Course Schedules.<br><br>
Course has attributes like course ID, name, description, number of credits, number of sections, frequency of occurrence (how many sessions in a week), session length (the amount of time a session takes, e.g., 50 minutes), prerequisites, school (which school in the university the course is a part of), start date, and end date. Course has a public method to return all of its information and attributes, and a private method (archiveCourse) to send all its data to an archive database so that the information isn't lost completely, but also isn't able to be enrolled in.<br><br>
Course Section extends Course, with additional attributes section ID, number of seats, days (days of the week the course section takes place on), time slot (a list of the times at which the course section takes place), and professor (that teaches the course section). Course Section inherits the getCourseInfo method, and has its own public methods getSectionInfo (returns section-specific information), assignClassroom (a course section assigns itself to a classroom upon creation), and updateProfessor (when a professor registers themself to teach a course section, it updates the course section's professor attribute). Course Sections take place in classrooms, given the classroom's availability.<br><br>
Students and professors add course sections to their course schedules. Course Schedule has attributes describing the total number of credits and the list of classes it contains. Whenever there are changes to courses and course sections that are part of course schedules, an email and/or system notification is sent to affected users. Course Schedule is associated with Course Rules, which are used to evaluate the validity of the schedule.
The classes TimeSlot and Day are used to describe course sections and classroom availabilities, and the dayName attribute uses an enumeration of days of the week that courses can take place on.

## Sequence Diagrams

### Student Enrollment

The student searches for a course, invoking getCourseList to the Course Database, which gets the course info and section info and displays the information to the student. The student adds a selected course to their schedule, and this process repeats until the student has selected all the courses they want. The student enrolls in the classes in their schedule, and if all of the enrollment criteria are met, the course schedule confirms enrollment of the student, invoking incrementSeats on the course section and reducing the number of available seats. 

### Enrollment Override

The course administrator searches for student information in the student database, and selects a student. The student's course schedule, from the course schedule object through the student database, is displayed to the course administrator. The course administrator overrides the course rule, changing the rule in question. The course admin then enrolls the student in the course and the schedule confirms enrollment of the student, invoking incrementSeats on the course section and reducing the number of available seats. The course schedule notifies the student of the change. 

### Manage Conflict

The course administrator searches for a course, invoking getCourseList to the Course Database, which gets the course info and section info and displays the information to the course administrator. Separately, the course administrator searches for classrooms through the Classroom University Database to find an available location and time for the course section. The course admin makes the changes to the location and or time of the course section, and the section information is changed in any affected course schedules. The students and professors with the changed course section in their schedules are notified of the change.

### Professor Chooses Section

The professor searches for a course, invoking getCourseList to the Course Database, which gets the course info and section info and displays the information to the professor. The professor adds a selected course to their schedule, and this process repeats until the student has selected all the courses they want. The professor enrolls in the classes in their schedule, and if no course sections overlap in their occurrence, the course schedule confirms enrollment of the professor, invoking updateProfessor on the course section and updating the course section's professor attribute.

### Add New Course/Section

The course administrator invokes addCourse to create a new course object. The course admin enters course information and confirms those changes. The professor enters course section information and confirms those changes. The course section assigns itself to a classroom, and a location and time slot is assigned to the course section. This course section process repeats until all course sections are added.

### Remove Course/Section

The course administrator searches for a course, invoking getCourseList to the Course Database, which gets the course info and section info and displays the information to the course administrator. The course administrator invokes removeCourse, and the Course invokes archiveCourse to send the course data to the Archive database. The course admin confirms the removal of the course. The course is then removed from the course database and from course schedules. Course Schedule sends notifications of the course removal to any affected students and professors. The deletion of the course is confirmed to the admin.

## Activity Diagram

The activity diagram shows how the assignClassroom method works.<br><br>
There is an input to the method of the course section's school, number of seats, frequency, and session length. The system searches for an available classroom utilizing the classroom database, filtering by the course section's school matching the classroom's school preference. A classroom has a list of 3 schools within the university that have priority to use a classroom (schoolPriority). The system checks whether any available classrooms' school preference match the school, and if so the classroom is added to a list of potential classrooms. If not, the system checks the iteration of the loop and iterates if i<2. If the classroom is added to a list of potential classrooms, the system checks the iteration (i<2?) and either iterates (i<2) or moves to the next decision point (i>=2). This loop occurs three times, one for each index of schoolPreference. <br><br>
At each step hereafter, the system checks if the list of classrooms has any entries, and applies another filter to the list. If at any point the list of potential classrooms doesn't have any entries, the system throws an error and notifies the course administrator so they can manage the conflict.<br><br>
First, the system filters the list of classrooms by the number of seats, where the number of seats in a classroom must be greater than or equal to the number of seats in the course section. Then, the system filters by the availability of the classroom. This filtering uses frequency (how many times per week a CourseSection takes place) and sessionLength as comparators, where a Classroom must have availability for the given frequency and sessionLength (amount of time a course session should take place in).<br><br>
Finally, the system checks to see if there are enough classrooms in the list to assign a classroom and time slot to each course section. If all steps are successful, each course section is assigned a classroom (location and time slot) that doesn't conflict with another class in the same location at the same time.

## State Diagram

The state diagram shows the potential states of a Student object.<br><br>
The Student begins as "Not enrolled." When a course is selected for the schedule, the Student is "Tentative" (tentatively enrolled). The system checks if the Student meets the enrollment criteria. If the student meets the criteria, Student is "Allowed to Enroll," but if the criteria are not met the student is "Blocked." From "Blocked" a student can search for another course or section and become "Not enrolled," or the student can get an administrator override and become "Enrolled." From "Allowed to Enroll," the student confirms their choice to enroll in courses and becomes "Enrolled," and the state machine ends.

## Component Diagram

The component diagram shows the data flow and requirements between user interfaces for each user type, major components, and information stores (database).<br><br>
The Course Search component is utilized in nearly all use cases and has data exchanges with all user types. The Course Search component utilizes the Course Database and provides course information to the Schedule component.<br><br>
The Student Schedule Builder and Professor Schedule Builder UIs utilize the Course Search component and Schedule component to search for courses and build their schedules. <br><br>
The Course Administration UI utilizes the Course Search component, which provides course information to the Course Management module, e.g., to manage location conflicts. The Course Administration UI also utilizes the Course Management component to add new courses or delete courses. The Course Management component requests and provides classroom data to the ClassroomUniv Database, provides updated course information to the Course Database, and provides deleted course information to the Archive Database.<br><br>
The Course Administration UI utilizes the Student Management Component to override enrollment information for students and enroll them in courses. The Student Management Component interfaces with the Students Database, which in turn interfaces with the Schedule component for the student's schedule.

## Deployment Diagram
Deployment of the Course Scheduling System is pictured here using AWS. An API gateway acts as a single point of access for students, professors, and course administrators to access the system. The API gateway enforces access control policies so that, for example, a student can’t access the course management component, but only the schedule and course search components. The API gateway also partners with Amazon CloudWatch alarms to inform university system administrators of issues.<br><br>
The system utilizes elastic load balancers to manage traffic to instances of virtual machines that contain the scheduling component, course search component, student management component, and course management component. Those components, as part of the system design, send system and/or email notifications to users when changes are made to their schedules.<br><br>
The system utilizes Oracle as its database provider.<br><br>
The system also uses an S3 bucket for long-term storage of data for the classroom, students, and course databases, while the archive database would use the glacier bucket, as its resources would not be accessed as frequently.
