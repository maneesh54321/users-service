package org.service.user.vo;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class TodoVO {
    @NonNull
    private Integer id;

    @NonNull
    private String description;

    @NonNull
    private LocalDate creationTime;

    @NonNull
    private Boolean completed;

    public String getCreationTime() {
        return creationTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
