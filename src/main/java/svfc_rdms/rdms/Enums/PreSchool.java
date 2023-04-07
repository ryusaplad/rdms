package svfc_rdms.rdms.Enums;

public enum PreSchool {
    KINDER1("Kinder 1"),
    KINDER2("Kinder 2"),
    GRADE1("Grade 1"),
    GRADE2("Grade 2"),
    GRADE3("Grade 3"),
    GRADE4("Grade 4"),
    GRADE5("Grade 5"),
    GRADE6("Grade 6")
;

    private final String grade;

    PreSchool(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }
}