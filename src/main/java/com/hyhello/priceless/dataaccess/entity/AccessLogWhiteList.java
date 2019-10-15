package com.hyhello.priceless.dataaccess.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 */
@Entity
@Table(name = "tb_access_log_white_list", schema = "hyhello", catalog = "")
public class AccessLogWhiteList {
    private int id;
    private String regular;
    private byte regType;
    private byte status;
    private Timestamp createTime;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "regular")
    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    @Basic
    @Column(name = "regType")
    public byte getRegType() {
        return regType;
    }

    public void setRegType(byte regType) {
        this.regType = regType;
    }

    @Basic
    @Column(name = "status")
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "createTime")
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
        AccessLogWhiteList that = (AccessLogWhiteList) o;
        return id == that.id &&
                regType == that.regType &&
                status == that.status &&
                Objects.equals(regular, that.regular) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regular, regType, status, createTime);
    }
}
