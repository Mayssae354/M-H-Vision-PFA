package com.smartvision.service.face;

import com.smartvision.repository.global.FaceEncodingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaceRecognitionService {

    private final FaceEncodingRepository faceEncodingRepository;

    public void verifyFaceEncoding() {
        // AI/ML Integration logic to compare Byte arrays (encodings)
    }

    public void registerFaceEncoding() {
        // Logic to store new face encoding locally or to pgvector
    }
}
