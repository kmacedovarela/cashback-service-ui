package org.acme.expense;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.acme.cashback.Cashback;
import org.acme.customer.Customer;
import org.acme.customer.CustomerStatus;
import org.acme.util.PrettyStringsUtil;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Cacheable
public class Expense extends PanacheEntityBase {
    //public and non-final attributes due to panache Proxy usage
    @Id
    @Column(name="sale_id")
    public Long sale_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    public Customer customer;

    public BigDecimal amount;

    @Column(name="earned_cashback")
    public BigDecimal earnedCashback;

    public LocalDateTime date;

    public Expense(Long id, Customer customer, Cashback cashback, BigDecimal amount, LocalDateTime date) {
        this.sale_id = id;
        this.customer = customer;
        this.amount = amount;
        this.date = date;
    }

    public Expense(Builder builder) {
        this.amount = builder.amount;
        this.customer = builder.customer;
        this.sale_id = builder.id;
        this.date = builder.date;
    }

    public Expense(){}

    public void updateEarnedCashbackValue()  {
        Expense expense = this.isPersistent() ? this : findById(sale_id);
        expense.earnedCashback = calculateAmountCashback();
        persist(expense);
    }

    public BigDecimal calculateAmountCashback(){
        BigDecimal percentage = BigDecimal.valueOf(CustomerStatus.get(this.customer.status).getCashbackPercentage());
        BigDecimal earnedCashback = percentage.multiply(this.getAmount());
        return earnedCashback;
    }

    public String getPrettyEarnedCashback(){
        return PrettyStringsUtil.formatMoney(getEarnedCashback());
    }
    public String getPrettyAmount() {return PrettyStringsUtil.formatMoney(getAmount());}
    public String getPrettyDate(){return PrettyStringsUtil.formatDate(this.date);}

    public BigDecimal getEarnedCashback() {return (this.earnedCashback == null) ? BigDecimal.ZERO : this.earnedCashback;    }
    public BigDecimal getAmount() {
        return (this.amount == null) ? BigDecimal.ZERO : this.amount;
    }

    public static class Builder {
        private Long id;
        private Customer customer;
        private Cashback cashback;
        private BigDecimal amount;
        private LocalDateTime date;
        private BigDecimal earnedCashback;

        public Builder(){ };

        public Expense.Builder Builder(){
            return this;
        }

        public Expense.Builder cashback(Cashback cashback) {
            this.cashback = cashback;
            return this;
        }

        public Expense.Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Expense.Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Expense.Builder earnedCashback(BigDecimal earnedCashback){
            this.earnedCashback= earnedCashback;
            return this;
        }

        public Expense.Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Expense.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Expense build() {
            return new Expense(this);
        }
    }
}
