package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "info", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "name"} )})
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
    @Column(name = "name")
    private String name;

    @Column(name = "access")
    @Enumerated(EnumType.STRING)
    private Access access;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "long_name")
    private String longName;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public enum Access {
        PUBLIC, PROTECTED, PRIVATE
    }
}
