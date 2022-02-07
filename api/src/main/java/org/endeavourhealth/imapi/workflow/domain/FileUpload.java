package org.endeavourhealth.imapi.workflow.domain;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FileUpload {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private States state;


}
