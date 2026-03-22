package com.smartvision.smartvision_backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ValidateOtpRequest {

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, max = 6)
    private String code;

    @NotNull
    private com.smartvision.smartvision_backend.entity.global.OtpCode.OtpType type;
}