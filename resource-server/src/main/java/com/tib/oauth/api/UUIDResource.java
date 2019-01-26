package com.tib.oauth.api;

import com.tib.oauth.uuid.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UUIDResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UUIDResource.class);

    private final UUIDGenerator uuidGenerator;

    @Autowired
    public UUIDResource(UUIDGenerator uuidGenerator) {this.uuidGenerator = uuidGenerator;}

    @GetMapping(value = "/v1/api/uuid")
    public ResponseEntity<UUIDResult> getUUID(Principal principal) {
        logPrincipal(principal);
        String uuid = uuidGenerator.generate();
        UUIDResult result = new UUIDResult();
        result.setResult(uuid);
        return ResponseEntity.ok(result);
    }

    private void logPrincipal(Principal principal) {
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().forEach(x -> {
            LOGGER.info(x.getAuthority());
        });
    }

    public static class UUIDResult {
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
