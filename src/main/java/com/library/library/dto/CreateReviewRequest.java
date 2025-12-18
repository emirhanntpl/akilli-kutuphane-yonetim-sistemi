package com.library.library.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotNull
    private String comment;
}
