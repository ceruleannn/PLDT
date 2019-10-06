package com.hyhello.priceless.dataaccess.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 */
@Entity
@Table(name = "tb_access_log", schema = "hyhello", catalog = "")
public class AccessLogEntity {
    private int id;
    private String url;
    private String title;
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
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        AccessLogEntity that = (AccessLogEntity) o;
        return id == that.id &&
                Objects.equals(url, that.url) &&
                Objects.equals(title, that.title) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, title, createTime);
    }
}
