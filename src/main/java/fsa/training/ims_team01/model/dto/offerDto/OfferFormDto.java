package fsa.training.ims_team01.model.dto.offerDto;

import fsa.training.ims_team01.enums.commonEnum.ContractTypeEnum;
import fsa.training.ims_team01.enums.commonEnum.OfferStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferFormDto {

    @NotNull(message = "{ME002.message}")
    private DropdownDto candidate;

    @NotNull(message = "{ME002.message}")
    private ContractTypeEnum contractType;

    @NotNull(message = "{ME002.message}")
    private DropdownDto position;

    @NotNull(message = "{ME002.message}")
    private DropdownDto level;

    @NotNull(message = "{ME002.message}")
    private DropdownDto approver;

    @NotNull(message = "{ME002.message}")
    private DropdownDto department;

    private DropdownDto interviewInfo;

    @NotNull(message = "{ME002.message}")
    private DropdownDto recruiterOwner;

    @NotNull(message = "{ME002.message}")
    private LocalDate contractFromDate;

    @NotNull(message = "{ME002.message}")
    private LocalDate contractToDate;

    @NotNull(message = "{ME002.message}")
    private LocalDate dueDate;
    ;
    @NotNull(message = "{ME002.message}")
    private BigDecimal basicSalary;

    private String note;

    private OfferStatusEnum status = OfferStatusEnum.WAITING_FOR_APPROVAL;

}
