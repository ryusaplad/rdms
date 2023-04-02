package svfc_rdms.rdms.Enums;


public enum College_Courses {
    BEC("Bachelor of Early Childhood"),
    BEEd("Bachelor of Elementary Education (Gen. Ed.)"),
    BPEd("Bachelor of Physical Education"),
    BPA("Bachelor of Public Administration"),
    BSA("Bachelor of Science in Accountancy"),
    BSHM("Bachelor of Science in Hospitality Management"),
    BSIT("Bachelor of Science in Information Technology"),
    BSEd_Eng("Bachelor of Secondary Education (English)"),
    BSEd_Fil("Bachelor of Secondary Education (Filipino)"),
    BSEd_Math("Bachelor of Secondary Education (Math)"),
    BSEd_Sci("Bachelor of Secondary Education (Science)"),
    BSEd_SocSci("Bachelor of Secondary Education (Social Studies)"),
    BSEd_Val("Bachelor of Secondary Education (Values)"),
    BTVTEd("Bachelor of Technical-Vocational Teacher Education");

    private final String degree;

    College_Courses(String degree) {
        this.degree = degree;
    }

    public String getDegree() {
        return degree;
    }
}
