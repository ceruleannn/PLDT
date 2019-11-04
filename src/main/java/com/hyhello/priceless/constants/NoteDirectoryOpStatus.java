package com.hyhello.priceless.constants;

import lombok.AllArgsConstructor;

/**
 *
 */
@AllArgsConstructor
public enum NoteDirectoryOpStatus {
    SUCCESS(200, "success"),
    NOT_EXIST_NODE(500, "node not exist"),
    REMOVE_NOT_EMPTY_FOLDER(501, "can not remove a not empty folder");

    private int code;
    private String msg;
}
