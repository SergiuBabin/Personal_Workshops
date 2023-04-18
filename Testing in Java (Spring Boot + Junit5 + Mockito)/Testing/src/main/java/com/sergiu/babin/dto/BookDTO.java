package com.sergiu.babin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record BookDTO(@Null Integer id, @NotBlank String title, @NotBlank String author, @PositiveOrZero Integer pages) {
}
