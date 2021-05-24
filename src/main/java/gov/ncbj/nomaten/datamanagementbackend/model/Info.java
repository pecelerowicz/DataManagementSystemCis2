package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.CascadeType.ALL;
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
    private String infoName;

    @Column(name = "access")
    @Enumerated(EnumType.STRING)
    private Access access;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "long_name")
    private String longName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", name='" + infoName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public enum Access {
        PUBLIC, PROTECTED, PRIVATE
    }
}
