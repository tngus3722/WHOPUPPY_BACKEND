package com.whopuppy.service.admin;

import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.service.UserService;

public interface AdminUserService extends UserService {
    String grantAuthority(AuthNumber authNumber);
    String deleteAuthority(AuthNumber authNumber);
}
