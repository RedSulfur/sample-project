package com.spring.german.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "type")
    private String type = UserProfileType.USER.getUserProfileType();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(55, 113)
                .append(this.id).append(this.type).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        UserProfile other = (UserProfile) obj;
        return new EqualsBuilder().append(other.id, this.id)
                .append(other.type, this.type).isEquals();
    }

    @Override
    public String toString() {
        return "UserProfile [id=" + id + ",  type=" + type  + "]";
    }
}
