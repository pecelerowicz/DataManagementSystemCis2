package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Project name is required!")
    private String name;

    private String description;

    @ManyToMany(mappedBy = "projects", fetch= LAZY , cascade = {PERSIST, MERGE, DETACH, REFRESH})
    private List<User> users = new LinkedList<>();

    public void addUser(User user) {
        if(users == null) {
            users = new LinkedList<>();
        }
        users.add(user);
    }

}
