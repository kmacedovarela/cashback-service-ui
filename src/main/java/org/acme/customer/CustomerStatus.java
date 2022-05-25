package org.acme.customer;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum CustomerStatus {
    SILVER("silver", 0.02d),
    GOLD("gold", 0.04d),
    PLATINUM("platinum", 0.08d);

    private static final Map<String,CustomerStatus> CUSTOMER_STATUS_MAP;

    private final String status;
    private final double cashbackPercentage;

    CustomerStatus(String name, double cashbackPercentage) {
        this.status = name;
        this.cashbackPercentage = cashbackPercentage;
    }

    static {
        Map<String,CustomerStatus> map = new ConcurrentHashMap<String, CustomerStatus>();
        for (CustomerStatus instance : CustomerStatus.values()) {
            map.put(instance.getStatus().toLowerCase(),instance);
        }
        CUSTOMER_STATUS_MAP = Collections.unmodifiableMap(map);
    }

    public static CustomerStatus get (String name) {
        return CUSTOMER_STATUS_MAP.get(name.toLowerCase());
    }

    public String getStatus() {
        return status;
    }

    public double getCashbackPercentage() {
        return cashbackPercentage;
    }
}
