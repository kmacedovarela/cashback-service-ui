package org.acme.cashback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.acme.customer.Customer;
import org.acme.expense.Expense;
import org.acme.util.PrettyStringsUtil;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

@Entity
@Cacheable
public class Cashback extends PanacheEntityBase {

    @Id
    @Column(name = "cashback_id")
    public Long cashback_id;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "customer_id")
    public Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal amount;

    public void updateAmountWith(BigDecimal earnedCashback) {
        this.amount = this.amount.add(earnedCashback);
    }

    public String getPrettyAmount() {
        return PrettyStringsUtil.formatMoney(getAmount());
    }

    public BigDecimal getAmount() {
        if(amount == null){
            amount = BigDecimal.ZERO;
        }

        return amount;
    }

    public Cashback(Long id, Customer customer, List<Expense> expenses, BigDecimal amount) {
        this.cashback_id = id;
        this.customer = customer;
        this.amount = amount;
    }

    public Cashback(Customer customer) {
        this.customer = customer;
    }

    public Cashback() {
    }

    public String prettyAmount() {
        return NumberFormat.getCurrencyInstance().format(this.amount);
    }

    @Override
    public String toString() {
        return "Cashback{" +
                "id=" + cashback_id +
                ", customer=" + customer +
                ", amount=" + amount +
                '}';
    }
}
