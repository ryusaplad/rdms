package svfc_rdms.rdms.Enums;

public enum Shs_Courses {
    HUMSS("Humanities and Social Sciences (HUMSS)"),
    STEM("Science, Technology, Engineering & Mathematics (STEM)"),
    GASNCII("General Academic Strand"),
    SPORTS("Sports"),
    BHCNCII("Beauty/Hair Care (NCII)"),
    BPPNCII("Bread Pastry Production (NCII)"),
    COOKERYNCII("Cookery (NCII)"),
    DRESSMAKINGNCII("Dressmaking (NCII)"),
    FBSNCII("Food and Beverage Services (NCII)"),
    FOSNCII("Front Office Services (NCII)"),
    HAIRNCII("Hairdressing (NCII)"),
    HOUSENCII("Housekeeping (NCII)"),
    LGSNCII("Local Guiding Services (NCII)"),
    TAILORINGNCII("Tailoring (NCII)"),
    TOURISMNCII("Tourism Promotion Services (NCII)"),
    WELLNESSNCII("Wellness Massage (NCII)"),
    ANIMATIONNCII("Animation (NCII)"),
    COMPUTERHARDWARENCII("Computer Hardware Servicing (NCII)"),
    COMPROGNCII("Computer Programming (NCII)"),
    AUTONCII("Automotive Servicing (NCII)"),
    CESNCII("Consumer Electronics Servicing (NCII)"),
    EINCII("Electrical Installation Maintenance (NCII)"),
    IS("International Subject (IS)");

    private final String value;

    Shs_Courses(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}