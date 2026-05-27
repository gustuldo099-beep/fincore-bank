package com.fincore.notification.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PixRequest {
    @NotNull
    private Long senderAccountId;

    @NotBlank(message = "Chave PIX do destinatário é obrigatória")
    private String receiverKey;

    @NotNull @DecimalMin("0.01")
    private BigDecimal amount;

    private String description;
}
