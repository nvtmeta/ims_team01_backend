package fsa.training.ims_team01.schedule;


import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.model.dto.email.EmailSendInterviewSchedule;
import fsa.training.ims_team01.service.EmailService;
import fsa.training.ims_team01.service.InterviewScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@Component
public class InterviewSchedule {
    private final InterviewScheduleService interviewScheduleService;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 0 * * *") // runs at 8:00 AM every day
//    @Scheduled(initialDelay = 60000, fixedDelay = Long.MAX_VALUE)
    public void sendReminder() {
        List<EmailSendInterviewSchedule> emailSendInterviewSchedules = interviewScheduleService.findByScheduleDate(LocalDate.now());
        emailSendInterviewSchedules
                .forEach(emailSendInterviewSchedule -> {
                    emailService.sendReminderScheduleEmail(emailSendInterviewSchedule);
                    // update status
                    interviewScheduleService.updateInterviewStatus(emailSendInterviewSchedule.getId(), InterviewScheduleEnum.INVITED);
                });

    }
}
