package com.f8.turnera.util;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.config.TokenUtil;

public class OrganizationHelper {
    public static Long getOrganizationId(String token) {
        return Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
    }
}