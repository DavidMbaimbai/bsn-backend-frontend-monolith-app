package com.davymbaimbai.bsn.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest(
                          Integer id,
                          @NotNull(message = "100")
                          @NotEmpty(message = "100")
                          String title,
                          @NotNull(message = "102")
                          @NotEmpty(message = "102")
                          String authorName,
                          @NotNull(message = "103")
                          @NotEmpty(message = "103")
                          String isbn,
                          @NotNull(message = "104")
                          @NotEmpty(message = "104")
                          String synopsis,
                          boolean shareable) {
}
