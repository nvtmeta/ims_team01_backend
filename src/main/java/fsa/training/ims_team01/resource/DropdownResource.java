package fsa.training.ims_team01.resource;

import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.service.DropdownService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/dropdown")
public class DropdownResource {

    private final DropdownService dropdownService;

    public DropdownResource(DropdownService dropdownService) {
        this.dropdownService = dropdownService;
    }

    @GetMapping("/positions")
    public ResponseEntity<List<DropdownDto>> getPositions() {
         return ResponseEntity.status(HttpStatus.OK).body(dropdownService.findAllPositions());
    }

    //    skills
    @GetMapping("/skills")
    public ResponseEntity<List<DropdownDto>> getSkills() {
        return ResponseEntity.status(HttpStatus.OK).body(dropdownService.findAllSkills());
    }

    @GetMapping("/benefits")
    public ResponseEntity<List<DropdownDto>> getBenefits() {
        return ResponseEntity.status(HttpStatus.OK).body(dropdownService.findAllBenefits());
    }

    @GetMapping("/levels")
    public ResponseEntity<List<DropdownDto>> getLevels() {
        return ResponseEntity.status(HttpStatus.OK).body(dropdownService.findAllLevels());
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DropdownDto>> getDepartments() {
        return ResponseEntity.status(HttpStatus.OK).body(dropdownService.findAllDepartments());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<DropdownDto>> getRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(dropdownService.findAllRoles());
    }

}
