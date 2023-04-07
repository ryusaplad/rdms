package svfc_rdms.rdms.Enums;


public enum College_Courses {
    BEC("Bachelor of Early Childhood"),
    BEEd("Bachelor of Elementary Education (Gen. Ed.)"),
    BPEd("Bachelor of Physical Education"),
    BPA("Bachelor of Public Administration"),
    BSA("Bachelor of Science in Accountancy"),
    BSHM("Bachelor of Science in Hospitality Management"),
    BSIT("Bachelor of Science in Information Technology"),
    BSEdEng("Bachelor of Secondary Education (English)"),
    BSEdFil("Bachelor of Secondary Education (Filipino)"),
    BSEdMath("Bachelor of Secondary Education (Math)"),
    BSEdSci("Bachelor of Secondary Education (Science)"),
    BSEdSocSci("Bachelor of Secondary Education (Social Studies)"),
    BSEdVal("Bachelor of Secondary Education (Values)"),
    BTVTEd("Bachelor of Technical-Vocational Teacher Education");

    private final String degree;

    College_Courses(String degree) {
        this.degree = degree;
    }

    public String getDegree() {
        return degree;
    }
}
