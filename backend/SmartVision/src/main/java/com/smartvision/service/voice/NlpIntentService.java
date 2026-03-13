package com.smartvision.service.voice;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NlpIntentService {

    public String determineIntent(String rawText) {
        // NLP logic to map text to intent (e.g., "Mark student absent")
        return "UNKNOWN_INTENT";
    }
}
