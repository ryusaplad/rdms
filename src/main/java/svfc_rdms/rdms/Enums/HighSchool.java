package svfc_rdms.rdms.Enums;

public enum HighSchool {
    GRADE7("Grade 7"),
    GRADE8("Grade 8"),
    GRADE9("Grade 9"),
    GRADE10("Grade 10");

    private final String grade;

    HighSchool(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }
}