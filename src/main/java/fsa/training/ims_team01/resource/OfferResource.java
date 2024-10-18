package fsa.training.ims_team01.resource;

import fsa.training.ims_team01.enums.commonEnum.OfferStatusEnum;
import fsa.training.ims_team01.exception.NotFoundException;
import fsa.training.ims_team01.model.dto.offerDto.OfferDetailDto;
import fsa.training.ims_team01.model.dto.offerDto.OfferFormDto;
import fsa.training.ims_team01.model.dto.offerDto.OfferListDto;
import fsa.training.ims_team01.model.entity.Offer;
import fsa.training.ims_team01.service.OfferService;
import fsa.training.ims_team01.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/offer")
public class OfferResource {

    private final OfferService offerService;

    public OfferResource(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public Page<OfferListDto> getOfferList(@PageableDefault Pageable pageable,
                                           @RequestParam(name = "status", required = false) OfferStatusEnum status,
                                           @RequestParam(name = "contractFromDate", required = false) LocalDate contractFromDate,
                                           @RequestParam(name = "contractToDate", required = false) LocalDate contractToDate,
                                           @RequestParam(name = "departmentId", required = false) Long departmentId,
                                           @RequestParam(name = "q", required = false) String q) {
        return offerService.getOfferList(pageable, q, status, departmentId,contractFromDate,contractToDate);
    }

    @GetMapping("/{offerId}")
    public Optional<OfferDetailDto> getOfferDetail(@PathVariable Long offerId) {
        return offerService.getOfferDetail(offerId);
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<?> createOffer(@RequestBody @Valid OfferFormDto offerFormDto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                System.out.println("Binding result has errors: " + bindingResult.getAllErrors());
                throw new NotFoundException(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            checkForm(offerFormDto);
            Offer savedOffer = offerService.create(offerFormDto);
            return new ResponseEntity<>(savedOffer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create offer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<?> updateOffer(@PathVariable("id") Long id, @RequestBody @Valid OfferFormDto offerFormDto, BindingResult bindingResult) {
        try {
            if (SecurityUtil.isRecruiter() && offerFormDto.getStatus() == OfferStatusEnum.APPROVED_OFFER ||
                    offerFormDto.getStatus() == OfferStatusEnum.REJECTED_OFFER) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            if (bindingResult.hasErrors()) {
                System.out.println("Binding result has errors: " + bindingResult.getAllErrors());
                throw new NotFoundException(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            checkForm(offerFormDto);
            Offer updatedOffer = offerService.update(id, offerFormDto);
            return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update offer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> approveOffer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(offerService.approveOffer(id), HttpStatus.OK);
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> rejectOffer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(offerService.rejectOffer(id), HttpStatus.OK);
    }

    @PutMapping("/markOfferCandidateSend/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> markOfferCandidateSend(@PathVariable("id") Long id) {
        return new ResponseEntity<>(offerService.markOfferCandidateSend(id), HttpStatus.OK);
    }

    @PutMapping("/accept/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> acceptOffer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(offerService.acceptOffer(id), HttpStatus.OK);
    }

    @PutMapping("/decline/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> declineOffer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(offerService.declineOffer(id), HttpStatus.OK);
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> cancelOffer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(offerService.cancelOffer(id), HttpStatus.OK);
    }

    private void checkForm(OfferFormDto offerFormDto) throws Exception {
        if (offerFormDto.getContractFromDate().isAfter(offerFormDto.getContractToDate())) {
            throw new Exception("From date must be before to date");
        }

    }
}
