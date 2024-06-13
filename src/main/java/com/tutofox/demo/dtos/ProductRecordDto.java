package com.tutofox.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record ProductRecordDto(
        @NotBlank(message = "O nome do produto não pode estar em vazio")
        String name,

        @NotNull(message = "O valor do produto não pode estar em branco")
        BigDecimal value) {
}

