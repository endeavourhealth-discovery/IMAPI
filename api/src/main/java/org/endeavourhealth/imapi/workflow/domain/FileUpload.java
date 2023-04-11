package org.endeavourhealth.imapi.workflow.domain;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
