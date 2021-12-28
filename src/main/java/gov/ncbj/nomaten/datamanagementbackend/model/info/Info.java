package gov.ncbj.nomaten.datamanagementbackend.model.info;

import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrInfo;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "info", uniqueConstraints = {@UniqueConstraint(name="unique_user_id_info_name", columnNames = {"user_id", "info_name"}),
                                           @UniqueConstraint(name="unique_info_difr_id", columnNames = {"info_difr_id"}),
                                           @UniqueConstraint(name="unique_info_test_id", columnNames = {"info_test_id"}),
})
//todo constraint at most one subinfo for one info
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Info {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Info name cannot be blank")
    @Column(name = "info_name")
    @Length(min = 1, max = 40)
    private String infoName;

    @Column(name = "access")
    @Enumerated(EnumType.STRING)
    private Access access;

    @Column(name = "title")
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @Column
    private LocalDateTime localDateTime;

    @Column
    private Boolean archived;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "info_difr_id", referencedColumnName = "id")
    private DifrInfo difrInfo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "info_test_id", referencedColumnName = "id")
    private TestInfo testInfo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = LAZY, cascade = {PERSIST, MERGE, DETACH, REFRESH})
    @JoinTable(
            name="project_info",
            joinColumns = @JoinColumn(name = "info_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"info_id", "project_id"})
    )
    private List<Project> projects = new LinkedList<>();

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", infoName='" + infoName + '\'' +
                ", access=" + access +
                ", title='" + title + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                '}';
    }

    public enum Access {
        PUBLIC, PROTECTED, PRIVATE
    }
}
