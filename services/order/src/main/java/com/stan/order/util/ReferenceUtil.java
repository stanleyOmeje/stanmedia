package com.stan.order.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class ReferenceUtil {

    private static final Logger log = LoggerFactory.getLogger(ReferenceUtil.class);
    private static final String NODE_ID = resolveNodeId();
    private static final Object LOCK = new Object();

    private static long lastTimestamp = -1L;
    private static int sequence = 0;

    public static String generateInternalReference() {
        long currentTimestamp;
        int currentSeq;

        synchronized (LOCK) {
            currentTimestamp = System.currentTimeMillis();

            if (currentTimestamp < lastTimestamp) {
                currentTimestamp = lastTimestamp;
            }

            if (currentTimestamp == lastTimestamp) {
                sequence++;
                if (sequence > 999) {
                    while (currentTimestamp <= lastTimestamp) {
                        currentTimestamp = System.currentTimeMillis();
                    }
                    sequence = 0;
                    lastTimestamp = currentTimestamp;
                }
            } else {
                sequence = 0;
                lastTimestamp = currentTimestamp;
            }

            currentSeq = sequence;
        }

        return String.format("%d%s%03d", currentTimestamp, NODE_ID, currentSeq);
    }

    private static String resolveNodeId() {
        try {
            String podIndex = System.getenv("POD_INDEX");
            if (podIndex != null && podIndex.matches("\\d+")) {
                return String.format("%02d", Integer.parseInt(podIndex) % 100);
            }
            String hostname = InetAddress.getLocalHost().getHostName();
            return String.format("%02d", Math.abs(hostname.hashCode()) % 100);
        } catch (Exception e) {
            log.warn("Could not determine node ID, defaulting to 01", e);
            return "01";
        }
    }
}
