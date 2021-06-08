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

    @Column(name = "test_field_1")
    private String testField1;

    @Column(name = "test_field_2")
    private String testField2;

    @Column(name = "test_field_3")
    private String testField3;

    @Column(name = "test_field_4")
    private String testField4;

    @Column(name = "test_field_5")
    private String testField5;

}
