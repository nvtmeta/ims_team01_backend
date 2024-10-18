package fsa.training.ims_team01.resource;

import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleDetailDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleFormDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleListDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleSubmitDto;
import fsa.training.ims_team01.model.dto.userDto.UserAuthDto;
import fsa.training.ims_team01.service.InterviewScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/interview-schedule")
public class InterviewScheduleResource {
    private final InterviewScheduleService interviewScheduleService;

    public InterviewScheduleResource(InterviewScheduleService interviewScheduleService) {
        this.interviewScheduleService = interviewScheduleService;
    }

    @GetMapping
    public Page<InterviewScheduleListDto> getInterviewScheduleList(
            @PageableDefault Pageable pageable,
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "status", required = false) InterviewScheduleEnum status,
            @RequestParam(name = "interviewer", required = false) Long interviewerId)
    {
        return interviewScheduleService.getInterviewScheduleList(pageable, q, status, interviewerId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> createInterviewSchedule(@RequestBody InterviewScheduleFormDto interviewScheduleCreateDto) {
        String interview = interviewScheduleService.createInterviewSchedule(interviewScheduleCreateDto);
        return ResponseEntity.ok(interview);
    }

    @GetMapping("/{interviewScheduleId}")
    public ResponseEntity<InterviewScheduleDetailDto> getInterviewScheduleById(@PathVariable Long interviewScheduleId) {
        Optional<InterviewScheduleDetailDto> interview = interviewScheduleService.getInterviewScheduleById(interviewScheduleId);
        return interview.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{interviewScheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> updateInterviewSchedule(@PathVariable Long interviewScheduleId, @RequestBody InterviewScheduleFormDto interviewScheduleCreateDto) {
        String response = interviewScheduleService.updateInterviewSchedule(interviewScheduleId, interviewScheduleCreateDto);
        return ResponseEntity.ok().body(response);
    }


    //    cancel interview
    @PutMapping("/cancel/{interviewScheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> cancelInterviewSchedule(@PathVariable Long interviewScheduleId) {
        String response = interviewScheduleService.cancelInterviewSchedule(interviewScheduleId);
        return ResponseEntity.ok().body(response);
    }

    //    submit result
    @PutMapping("/submit/{interviewScheduleId}")
    @PreAuthorize("hasAnyRole('INTERVIEWER')")
    public ResponseEntity<String> submitResult(@PathVariable Long interviewScheduleId, @RequestBody InterviewScheduleSubmitDto interviewScheduleSubmitDto) {
        // Get the logged-in user's ID from the JWT token
        Long userId = ((UserAuthDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        // Check if the user is allowed to submit the result
        if (!interviewScheduleService.isAllowedToSubmitResult(interviewScheduleId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to submit the result.");
        }

        String response = interviewScheduleService.submitResult(interviewScheduleId, interviewScheduleSubmitDto);
        return ResponseEntity.ok().body(response);
    }

    // get all interviewers
    @GetMapping("/interviewers")
    public ResponseEntity<Page<DropdownDto>> getAllInterviewers(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(interviewScheduleService.getAllInterviewers(pageable));
    }


    //    get note by id
    @GetMapping("/note/{interviewScheduleId}")
    public ResponseEntity<String> getNoteById(@PathVariable Long interviewScheduleId) {
        Optional<String> response = interviewScheduleService.getNoteById(interviewScheduleId);
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // send reminder
    @PostMapping("/reminder/{interviewScheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECRUITER')")
    public ResponseEntity<String> sendReminder(@PathVariable Long interviewScheduleId) {
        interviewScheduleService.sendReminder(interviewScheduleId);
        return ResponseEntity.ok().body("Reminder sent successfully.");
    }


}
