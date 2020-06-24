package com.hyhello.priceless.dataaccess.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 */
@Entity
@Table(name = "tb_favorite_filter", schema = "hyhello", catalog = "")
public class FavoriteFilter {
    private int id;
    private int type;
    private String host;
    private String regular;
    private String chromePattern;
    private byte useProxy;
    private String remark;
    private byte status;
    private Timestamp createTime;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type", nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "host", nullable = false, length = 50)
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Basic
    @Column(name = "regular", nullable = false, length = 100)
    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    @Basic
    @Column(name = "chromePattern", nullable = false, length = 255)
    public String getChromePattern() {
        return chromePattern;
    }

    public void setChromePattern(String chromePattern) {
        this.chromePattern = chromePattern;
    }

    @Basic
    @Column(name = "useProxy", nullable = false)
    public byte getUseProxy() {
        return useProxy;
    }

    public void setUseProxy(byte useProxy) {
        this.useProxy = useProxy;
    }

    @Basic
    @Column(name = "remark", nullable = false, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "createTime", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteFilter that = (FavoriteFilter) o;
        return id == that.id &&
                type == that.type &&
                useProxy == that.useProxy &&
                status == that.status &&
                Objects.equals(host, that.host) &&
                Objects.equals(regular, that.regular) &&
                Objects.equals(chromePattern, that.chromePattern) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, host, regular, chromePattern, useProxy, remark, status, createTime);
    }
}
