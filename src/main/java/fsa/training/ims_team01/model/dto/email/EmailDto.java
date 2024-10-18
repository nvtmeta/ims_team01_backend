package fsa.training.ims_team01.model.dto.email;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmailDto {
    private List<String> emailToList;
    private List<String> emailCcList;
    private List<String> emailBccList;
    private String subject;
    private String body;
}