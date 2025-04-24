package com.example.ecommerce.cronjob;

import com.example.ecommerce.modules.user.repositories.BlacklistTokenRepo;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BlacklistTokenClean {
    @Autowired
    private BlacklistTokenRepo blacklistTokenRepo;

    private static final Logger logger = LoggerFactory.getLogger(BlacklistTokenClean.class);

    // Clean up Expired Blacklist Tokens every day at midnight
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        int deletedCount = blacklistTokenRepo.deleteByExpiryDateBefore(currentDateTime);
        logger.info("Deleted {} expired tokens", deletedCount);
    }
}
