package svfc_rdms.rdms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "school_programs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolPrograms {
    @Id
    @SequenceGenerator(name = "sprogram_sequence", sequenceName = "sprogram_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sprogram_sequence")
    private long id;
    private String schoolLevel; 
    private String level;
    private String courseOrcategory;
    private String status;
}
