package org.acme.customer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

@QuarkusTest
public class CustomerTest {

    private final static double SILVER_CASHBACK_PERCENTUAL = (2d/100d);
    private final static double GOLD_CASHBACK_PERCENTUAL = (4d/100d);
    private final static double PLATINUM_CASHBACK_PERCENTUAL = (8d/100d);

    @Test
    public void cashbackPercentualSilverStatusTest() {
        Assertions.assertEquals(SILVER_CASHBACK_PERCENTUAL, CustomerStatus.SILVER.getCashbackPercentage());
    }

    @Test
    public void cashbackPercentualGoldStatusTest() {
        Assertions.assertEquals(GOLD_CASHBACK_PERCENTUAL, CustomerStatus.GOLD.getCashbackPercentage());
    }

    @Test
    public void cashbackPercentualPlatinumStatusTest() {
        Assertions.assertEquals(PLATINUM_CASHBACK_PERCENTUAL, CustomerStatus.PLATINUM.getCashbackPercentage());
    }


}
