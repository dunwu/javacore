package io.github.dunwu.javacore.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Query {

    private Long id;
    private String status;

    public Query(Long id, String status) {
        this.id = id;
        this.status = status;
    }

}