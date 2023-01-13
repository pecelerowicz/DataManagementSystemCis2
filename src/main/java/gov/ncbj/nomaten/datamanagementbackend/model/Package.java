package gov.ncbj.nomaten.datamanagementbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Package {// TODO hasStorage = false; hasMetadata = false (NOT AT THE SAME TIME).
    private String name;
    private boolean hasStorage;
    private boolean hasMetadata;
    private boolean isArchived;
    private String title;
    private String shortDescription;
    private LocalDateTime localDateTime;
}
