package com.hyhello.priceless.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectoryNode {
    private String key;
    private String title;
    private boolean folder;
    private boolean expanded;
    private DirectoryNode[] children;
    private Map<String, Object> data = new HashMap<>();

    @JsonIgnore
    private String parentKey;
}
