package svfc_rdms.rdms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoList {

     @Id
     @SequenceGenerator(name = "todoList_Sequence", sequenceName = "todoList_Sequence", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todoList_Sequence")
     private long todoList_Id;
     private String todoMessage;
     private String dateDone;
     private String dateAdded;
     private boolean todoStatus; // done / not

     @ManyToOne
     @JoinColumn(name = "added_by", referencedColumnName = "userId")
     private Users user;
}
