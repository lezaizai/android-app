package com.didlink.xingxing.security;

import com.didlink.xingxing.models.LoginAuth;

/**
 * Created by xingxing on 2017/4/9.
 */

public interface ILoginListener {
    public void loginResponse(LoginAuth auth);
}
