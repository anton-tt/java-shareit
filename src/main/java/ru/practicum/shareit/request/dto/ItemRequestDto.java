package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {

    @Positive
    private long id;
    @NotBlank
    private String description;
    @NonNull
    private LocalDateTime created;
    private final long requestorId;

}