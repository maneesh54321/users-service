package org.service.user.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoForm {
    private Integer id;
    private String description;
    private Boolean completed;
}
