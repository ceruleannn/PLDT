package com.hyhello.priceless.dto.resp;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Response {
    protected int code = 200;

    @NonNull
    protected String msg;
}
