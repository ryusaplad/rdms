package svfc_rdms.rdms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "logs")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class GlobalLogs {
    @Id
    @SequenceGenerator(name = "logs_sequence", sequenceName = "logs_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logs_sequence")
    private long logsId;
    private String message; // add messages
    private String messageType; // logging
    private String dateAndTime; // what date created
    private String threatLevel; // low,medium,high,critical
    private String clientIpAddress;
    private String performedBy;

}
