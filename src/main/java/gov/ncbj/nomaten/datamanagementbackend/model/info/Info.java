package gov.ncbj.nomaten.datamanagementbackend.model.info;

import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrractometerInfo;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "info", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "info_name"} )})
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
    @Length(min = 1, max = 20)
    private String infoName;

    @Column(name = "access")
    @Enumerated(EnumType.STRING)
    private Access access;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "long_name")
    private String longName;

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "info_difr_id", referencedColumnName = "id")
    private DifrractometerInfo diffractometerInfo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "info_test_id", referencedColumnName = "id")
    private TestInfo testInfo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", infoName='" + infoName + '\'' +
                ", access=" + access +
                ", shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                '}';
    }

    public enum Access {
        PUBLIC, PROTECTED, PRIVATE
    }
}
