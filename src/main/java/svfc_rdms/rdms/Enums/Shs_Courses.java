package svfc_rdms.rdms.Enums;

public enum Shs_Courses {
    HUMSS("Humanities and Social Sciences (HUMSS)"),
    STEM("Science, Technology, Engineering & Mathematics (STEM)"),
    GAS_NCII("General Academic Strand"),
    SPORTS("Sports"),
    BHC_NCII("Beauty/Hair Care (NCII)"),
    BPP_NCII("Bread Pastry Production (NCII)"),
    COOKERY_NCII("Cookery (NCII)"),
    DRESSMAKING_NCII("Dressmaking (NCII)"),
    FBS_NCII("Food and Beverage Services (NCII)"),
    FOS_NCII("Front Office Services (NCII)"),
    HAIR_NCII("Hairdressing (NCII)"),
    HOUSE_NCII("Housekeeping (NCII)"),
    LGS_NCII("Local Guiding Services (NCII)"),
    TAILORING_NCII("Tailoring (NCII)"),
    TOURISM_NCII("Tourism Promotion Services (NCII)"),
    WELLNESS_NCII("Wellness Massage (NCII)"),
    ANIMATION_NCII("Animation (NCII)"),
    COMPUTER_HARDWARE_NCII("Computer Hardware Servicing (NCII)"),
    COMPROG_NCII("Computer Programming (NCII)"),
    AUTO_NCII("Automotive Servicing (NCII)"),
    CES_NCII("Consumer Electronics Servicing (NCII)"),
    EI_NCII("Electrical Installation Maintenance (NCII)"),
    IS("International Subject (IS)");

    private final String value;

    Shs_Courses(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}