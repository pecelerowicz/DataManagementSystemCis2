package gov.ncbj.nomaten.datamanagementbackend.model;

import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "project", uniqueConstraints = {@UniqueConstraint(name="unique_owner_name_project_name", columnNames = {"owner_name", "project_name"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project implements Comparable<Project> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_name")
    @NotBlank(message = "Project name is required!")
    private String projectName;

    @Column(name = "description", length = 1024) // to be decided later
    @NotBlank(message = "Project description is required")
    private String description;

    @Column
    private LocalDateTime localDateTime;

    @Column
    private Boolean archived;

    @Column(name = "owner_name")
    @NotBlank(message = "Project owner_id is required")
    private String ownerName;

    @ManyToMany(mappedBy = "projects", fetch = LAZY, cascade = {PERSIST, MERGE, DETACH, REFRESH})
    private List<User> users = new LinkedList<>();

    @ManyToMany(mappedBy = "projects", fetch = LAZY, cascade = {PERSIST, MERGE, DETACH, REFRESH})
    private List<Info> infoList = new LinkedList<>();

    @Override
    public int compareTo(Project that) {
        return -this.localDateTime.compareTo(that.localDateTime);
    }
}
