package com.hyhello.priceless.dataaccess.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 */
@Entity
@Table(name = "tb_favorite_record", schema = "hyhello")
public class FavoriteRecord {
    private int id;
    private String filename;
    private String format;
    private double size;
    private String host;
    private String comment;
    private String statusMsg;
    private byte status;
    private Timestamp upTime;
    private Timestamp downTime;
    private String baseUrl;
    private String bucket;
    private String cloudUrl;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "filename", nullable = false, length = 100)
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Basic
    @Column(name = "format", nullable = false, length = 20)
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Basic
    @Column(name = "size", nullable = false, precision = 0)
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Basic
    @Column(name = "host", nullable = false, length = 100)
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Basic
    @Column(name = "comment", nullable = false, length = 255)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "statusMsg", nullable = false, length = 50)
    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
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
    @Column(name = "upTime", nullable = false)
    public Timestamp getUpTime() {
        return upTime;
    }

    public void setUpTime(Timestamp upTime) {
        this.upTime = upTime;
    }

    @Basic
    @Column(name = "downTime", nullable = false)
    public Timestamp getDownTime() {
        return downTime;
    }

    public void setDownTime(Timestamp downTime) {
        this.downTime = downTime;
    }

    @Basic
    @Column(name = "baseUrl", nullable = false, length = 500)
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Basic
    @Column(name = "bucket", nullable = false, length = 50)
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Basic
    @Column(name = "cloudUrl", nullable = false, length = 500)
    public String getCloudUrl() {
        return cloudUrl;
    }

    public void setCloudUrl(String cloudUrl) {
        this.cloudUrl = cloudUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteRecord that = (FavoriteRecord) o;
        return id == that.id &&
                Double.compare(that.size, size) == 0 &&
                status == that.status &&
                Objects.equals(filename, that.filename) &&
                Objects.equals(format, that.format) &&
                Objects.equals(host, that.host) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(statusMsg, that.statusMsg) &&
                Objects.equals(upTime, that.upTime) &&
                Objects.equals(downTime, that.downTime) &&
                Objects.equals(baseUrl, that.baseUrl) &&
                Objects.equals(bucket, that.bucket) &&
                Objects.equals(cloudUrl, that.cloudUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filename, format, size, host, comment, statusMsg, status, upTime, downTime, baseUrl, bucket, cloudUrl);
    }
}
