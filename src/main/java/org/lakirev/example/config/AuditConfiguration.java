package org.lakirev.example.config;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.AuditingHandlerSupport;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditConfiguration {

    AuditingHandlerSupport auditingHandler;

    @PostConstruct
    public void configure() {
        auditingHandler.setModifyOnCreation(false);
    }

}
