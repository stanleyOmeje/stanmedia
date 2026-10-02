package com.stan.gateway.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpUtil {
    public static String resolveIp(HttpServletRequest request) {
        String merchantIp = RemoteIpHelper.getRemoteIpFrom(request);
        log.info("merchantIp: {}", merchantIp);
        if (merchantIp.contains(",")) {
            String[] duplicateIp = merchantIp.split(",");
            merchantIp = duplicateIp[0];
            log.info("Split merchantIp address is ...{}", merchantIp);
        }
        return merchantIp;
    }
}
