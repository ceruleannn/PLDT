package com.hyhello.priceless.dto.resp;

import com.hyhello.priceless.dataaccess.entity.AccessLogWhiteList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccessLogWhiteListResponse extends Response {
    private List<AccessLogWhiteList> list;

    public AccessLogWhiteListResponse(int code, String msg, List<AccessLogWhiteList> list){
        super(code,msg);
        this.list = list;
    }
}
