package com.hyhello.priceless.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Node {
    private String key;
    private boolean folder;
    private String title;
    private boolean expanded;
    private Node[] children;
    private Map<String, Object> data = new HashMap<>();

    @JsonIgnore
    private Integer parentId;
}
