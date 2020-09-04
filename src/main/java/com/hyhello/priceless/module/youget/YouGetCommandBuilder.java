package com.hyhello.priceless.module.youget;

import com.hyhello.priceless.constants.Const;
import com.hyhello.priceless.support.BeanSupport;
import org.springframework.util.StringUtils;

public class YouGetCommandBuilder {
    
    private String rootPath;
    private String fileUrl;
    private boolean useProxy;
    private boolean isInfo;
    private Integer itag;
    private String format;

    public static YouGetCommandBuilder newBuilder() {
        return new YouGetCommandBuilder();
    }

    public YouGetCommandBuilder isInfo(boolean isInfo) {
        this.isInfo = isInfo;
        return this;
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

    public YouGetCommandBuilder setItag(int itag) {
        this.itag = itag;
        return this;
    }

    public YouGetCommandBuilder setFormat(String format) {
        this.format = format;
        return this;
    }

    
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("you-get ");
        if (isInfo){
            builder.append("-i ");
        }

        if (itag != null && itag > 0){
            builder.append("--itag=").append(itag).append(" ");
        }

        if (!StringUtils.isEmpty(format)){
            builder.append("--format=").append(format).append(" ");
        }

        if (null != rootPath) {
            builder.append("-o ").append(rootPath).append(" ");
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
            builder.append(" ");
        }
        if (null != fileUrl) {
            builder.append(fileUrl);
        }
        
        return builder.toString();
    }
}
