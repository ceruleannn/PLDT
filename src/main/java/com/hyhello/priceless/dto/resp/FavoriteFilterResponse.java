package com.hyhello.priceless.dto.resp;

import com.hyhello.priceless.dataaccess.entity.AccessLogWhiteList;
import com.hyhello.priceless.dataaccess.entity.FavoriteFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FavoriteFilterResponse extends Response {
    private List<FavoriteFilter> list;

    public FavoriteFilterResponse(int code, String msg, List<FavoriteFilter> list){
        super(code,msg);
        this.list = list;
    }
}
