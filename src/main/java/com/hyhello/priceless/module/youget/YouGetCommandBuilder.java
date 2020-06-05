package com.hyhello.priceless.module.youget;

import com.hyhello.priceless.constants.Const;
import com.hyhello.priceless.support.BeanSupport;

public class YouGetCommandBuilder {
    
    private String rootPath;
    private String fileUrl;
    private boolean useProxy;

    public static YouGetCommandBuilder newBuilder() {
        return new YouGetCommandBuilder();
    }
    
    public YouGetCommandBuilder setRootPath(String rootPath) {
        this.rootPath = rootPath;
        return this;
    }
    
    public YouGetCommandBuilder setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }
    
    public YouGetCommandBuilder useProxy(boolean useProxy) {
        this.useProxy = useProxy;
        return this;
    }
    
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("you-get ");
        if (null != rootPath) {
            builder.append("-o " + rootPath + " ");
        }   
        if (useProxy) {
            String proxyType = BeanSupport.getCommonConfig().getYougetProxyType();
            String proxyAddress = BeanSupport.getCommonConfig().getYougetProxyAddress();
            if (Const.PROXY_PROTOCOL.HTTP.equals(proxyType)){
                builder.append("-x "); //http
            }else {
                builder.append("-s "); //socks
            }
            builder.append(proxyAddress);
        }
        if (null != fileUrl) {
            builder.append(fileUrl);
        }
        
        return builder.toString();
    }
}
