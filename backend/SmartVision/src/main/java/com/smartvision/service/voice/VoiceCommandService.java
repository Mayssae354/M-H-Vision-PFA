package com.smartvision.service.voice;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoiceCommandService {

    private final NlpIntentService nlpIntentService;
    
    public void processAudioCommand() {
        // Convert Speech-to-Text, then call NlpIntentService
    }
}
