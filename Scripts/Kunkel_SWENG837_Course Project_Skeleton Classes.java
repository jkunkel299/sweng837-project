class Course {
    private int courseId;
    private String courseName;
    private String description;
    private int credits;
    private int numSections;
    private  frequency;
    private int sessionLength;
    private String prerequisites;
    private String school;
    private String startDate;
    private String endDate;

    public CourseInfo[] getCourseInfo(){
        // returns course information (above attributes) as an array
    } 

    private void archiveCourse(int courseId){
        // sends course data to the archive database
    }
}

class CourseSection extends Course {
    private int sectionId;
    private int seats;
    // array of days on which the course section takes place
    // array of timeslots in which the course section takes place
    private String professor;
    
    public SectionInfo[] getSectionInfo(){
        // returns section information (above attributes) as an array
    }

    public void assignClassroom(int sectionId, String school, int seats, int frequency, int sessionLength){
        // assigns classroom association to course section using logic defined in the activity diagram
    }

    public void updateProfessor(String professor){
        // changes the value of professor attribute when a professor assigns themselves to a course section
    }
}
