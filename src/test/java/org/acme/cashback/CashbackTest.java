package org.acme.cashback;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.customer.Customer;
import org.acme.customer.CustomerStatus;
import org.acme.expense.Expense;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

@QuarkusTest
public class CashbackTest {
    @Test
    public void updateAmountWithValueTest() {

        BigDecimal currentAmount = new BigDecimal(10);
        BigDecimal incomingAmount = new BigDecimal(90);

        Cashback cashback = new Cashback(1000L, new Customer(), new ArrayList<Expense>(), currentAmount);
        cashback.updateAmountWith(incomingAmount);
        Assertions.assertEquals(currentAmount.add(incomingAmount), cashback.getAmount());
    }

    @Test
    public void getPrettyAmountTest() {
        Expense expense = new Expense.Builder()
                .id(1L)
                .amount(new BigDecimal(100))
                .cashback(new Cashback())
                .customer(
                        new Customer.Builder()
                                .id(1L)
                                .status(CustomerStatus.SILVER.getStatus())
                                .build())
                .date(LocalDateTime.now()).build();

        String formattedResult = NumberFormat.getCurrencyInstance(Locale.US).format(expense.amount);

        Assertions.assertEquals(formattedResult, expense.getPrettyAmount());
    }

    @Test
    public void getPrettyDateTest() {
        Expense expense = new Expense.Builder()
                .id(1L)
                .amount(new BigDecimal(100))
                .cashback(new Cashback())
                .customer(
                        new Customer.Builder()
                                .id(1L)
                                .status(CustomerStatus.SILVER.getStatus())
                                .build())
                .date(LocalDateTime.now()).build();

        String formattedResult = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT).withLocale(Locale.US).format(expense.date);

        Assertions.assertEquals(formattedResult, expense.getPrettyDate());
    }
}
