package com.whopuppy.domain.user;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class Authority {
    private Long id;
    private String authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
