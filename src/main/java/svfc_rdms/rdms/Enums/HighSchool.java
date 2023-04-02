package svfc_rdms.rdms.Enums;

public enum HighSchool {
    GRADE_7("Grade 7"),
    GRADE_8("Grade 8"),
    GRADE_9("Grade 9"),
    GRADE_10("Grade 10");

    private final String grade;

    HighSchool(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }
}