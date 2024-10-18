package fsa.training.ims_team01.model.dto.offerDto;

import fsa.training.ims_team01.enums.commonEnum.ContractTypeEnum;
import fsa.training.ims_team01.enums.commonEnum.OfferStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OfferDetailDto {


    private DropdownDto candidate;

    private ContractTypeEnum contractType;

    private DropdownDto position;

    private DropdownDto level;

    private DropdownDto approver;

    private DropdownDto department;

    private DropdownDto interviewInfo;

    private List<DropdownDto> interviewers;

    private DropdownDto recruiterOwner;

    private LocalDate contractFromDate;

    private LocalDate contractToDate;

    private LocalDate dueDate;

    private String interviewNotes;

    private BigDecimal basicSalary;

    private String note;

    private OfferStatusEnum status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public OfferDetailDto(ContractTypeEnum contractType, LocalDate contractFromDate, LocalDate contractToDate, LocalDate dueDate,
                          BigDecimal basicSalary, String note, OfferStatusEnum status, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.contractType = contractType;
        this.contractFromDate = contractFromDate;
        this.contractToDate = contractToDate;
        this.dueDate = dueDate;
        this.basicSalary = basicSalary;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

}
