package gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo;

import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "info_test")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "testInfo")
    private Info info;

    @Column(name = "test_field")
    private String testField;

}
