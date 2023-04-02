package svfc_rdms.rdms.Enums;

public enum PreSchool {
    KINDER_1("Kinder 1"),
    KINDER_2("Kinder 2"),
    GRADE_1("Grade 1"),
    GRADE_2("Grade 2"),
    GRADE_3("Grade 3"),
    GRADE_4("Grade 4"),
    GRADE_5("Grade 5"),
    GRADE_6("Grade 6")
;

    private final String grade;

    PreSchool(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }
}