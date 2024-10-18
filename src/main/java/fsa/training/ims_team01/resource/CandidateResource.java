package fsa.training.ims_team01.resource;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import fsa.training.ims_team01.exception.CandidateException;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateDetailDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateFormDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateListDto;
import fsa.training.ims_team01.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/candidate")
public class CandidateResource {

    private final CandidateService candidateService;

    public CandidateResource(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
//    todo : candidate with interview role
    public Page<CandidateListDto> getCandidates(
            @PageableDefault Pageable pageable,
            @RequestParam(name = "status", required = false) CandidateStatusEnum status,
            @RequestParam(name = "q", required = false) String q
    ) {
        return candidateService.getCandidateList(pageable, q, status);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> createCandidate(@RequestBody @Valid CandidateFormDto candidateCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("Binding result has errors: " + bindingResult.getAllErrors());
            throw new CandidateException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        System.out.println("candidateCreateDto: " + candidateCreateDto);
        String id = candidateService.createCandidate(candidateCreateDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/{candidateId}")
    public ResponseEntity<CandidateDetailDto> getCandidateById(@PathVariable Long candidateId) {
        Optional<CandidateDetailDto> candidate = candidateService.getCandidateById(candidateId);
        return candidate.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/status/{status}")
    public Page<DropdownDto> getCandidateByStatus(@PathVariable CandidateStatusEnum status, Pageable pageable) {
        return  candidateService.getCandidateByStatus(status, pageable);
    }

    @GetMapping("/status/no-ban")
    public Page<DropdownDto> getCandidateNoBan(Pageable pageable, @RequestParam(name = "q", required = false) String q
    ) {
        return candidateService.getCandidateNoBan(pageable, q);
    }
    @GetMapping("/email/{email}")
    public boolean isEmailExisted(@PathVariable String email) {
        return candidateService.isEmailExisted(email);
    }

    @PutMapping("/{candidateId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> updateCandidate(@PathVariable Long candidateId,
                                                  @RequestBody @Valid CandidateFormDto candidateCreateDto) {
        String id = candidateService.updateCandidate(candidateId, candidateCreateDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }


    @DeleteMapping("/{candidateId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> deleteCandidateById(@PathVariable Long candidateId) {
        String message = candidateService.deleteCandidateById(candidateId);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{candidateId}/ban")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> banCandidateById(@PathVariable Long candidateId) {
        String message = candidateService.banCandidateById(candidateId);
        return ResponseEntity.ok(message);
    }

}
