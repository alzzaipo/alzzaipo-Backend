package com.alzzaipo.service;

import com.alzzaipo.domain.ipo.Ipo;
import com.alzzaipo.domain.ipo.IpoRepository;
import com.alzzaipo.domain.notification.criteria.NotificationCriteria;
import com.alzzaipo.domain.notification.criteria.NotificationCriteriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final IpoScraperService ipoScraperService;
    private final IpoRepository ipoRepository;
    private final EmailService emailService;
    private final NotificationCriteriaRepository notificationCriteriaRepository;

    @Transactional
    @Scheduled(cron = "0 23 00 ? * MON-FRI")
    public void scheduledTask() {
        int ipoUpdateCnt = 0, emailSendingCnt = 0;
        List<Ipo> ipoList = ipoScraperService.extractIpoEntitiesFromPage(1);
        List<Ipo> ipoListToSendNotification = new ArrayList<>();

        for(Ipo ipo : ipoList) {
            if(ipoRepository.findByStockCode(ipo.getStockCode()).isEmpty()) {
                ipoRepository.save(ipo);
                ipoListToSendNotification.add(ipo);
                ipoUpdateCnt++;
            }
        }

        for(Ipo ipo : ipoListToSendNotification) {
            List<NotificationCriteria> notificationCriteriaList = notificationCriteriaRepository.findAll();
            List<String> userEmailsWithRedundancy = new ArrayList<>();

            for(NotificationCriteria criteria : notificationCriteriaList) {
                if(ipo.getCompetitionRate() >= criteria.getMinCompetitionRate()
                        && ipo.getLockupRate() >= criteria.getMinLockupRate()) {
                    String userEmail = criteria.getMember().getEmailNotification().getEmail();
                    userEmailsWithRedundancy.add(userEmail);
                }
            }

            List<String> emailList = userEmailsWithRedundancy.stream().distinct().collect(Collectors.toList());
            emailSendingCnt += emailList.size();
            emailService.sendNewIpoNotificationEmail(ipo, emailList);
        }

        log.info("Scheduled Task Completed : " + ipoUpdateCnt + " IPOs Added / " + emailSendingCnt + " Emails Sent");
    }

}
