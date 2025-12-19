package com.library.library.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoadBalanceRequest(
        @DecimalMin(value = "1.0", message = "Yüklenecek tutar en az 1 TL olmalıdır.")
        Double amount,

        @NotBlank(message = "Kart numarası boş olamaz.")
        @Pattern(regexp = "\\d{16}", message = "Kart numarası 16 haneli olmalıdır.")
        String cardNumber,

        @NotBlank(message = "Son kullanma tarihi boş olamaz.")
        @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "Geçersiz tarih formatı (AA/YY).")
        String expiryDate,

        @NotBlank(message = "CVV boş olamaz.")
        @Pattern(regexp = "\\d{3}", message = "CVV 3 haneli olmalıdır.")
        String cvv
) {}
