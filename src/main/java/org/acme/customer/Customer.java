package org.acme.customer;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.acme.cashback.Cashback;
import org.acme.expense.Expense;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Entity
@Cacheable
public class Customer extends PanacheEntityBase {
    private static final Logger logger = Logger.getLogger(Customer.class.getName());

    //public and non-final attributes due to panache Proxy usage
    @Column(insertable = false, updatable = false)
    @Id
    public Long customer_id;

    public String name;
    public String status;

    @OneToOne
    @JoinColumn(name = "customer_id")
    public Cashback cashback;

    @OneToMany(mappedBy = "customer")
    private List<Expense> expenses;

    @Transient
    private static Integer pageCount;

    public Cashback getCashback() {
        if (cashback == null){
            cashback = new Cashback();
        }
        return cashback;
    }

    public Customer(Builder builder){
        this.customer_id = builder.customer_id;
        this.name = builder.name;
        this.status = builder.status;
        this.expenses = builder.expenses;
        this.cashback = builder.cashback;
    }

    public Customer(Long customer_id, Cashback cashback, List<Expense> expenses, String name, String status) {
        this.customer_id = customer_id;
        this.cashback = cashback;
        this.expenses = expenses;
        this.name = name;
        this.status = status;
    }

    public Customer(){}

    @Override
    public String toString() {
        return "Customer{" +
                "customer_id=" + customer_id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     *
     * This method can be used for paginated searches. It queries data based on the
     * page details. Additionally, it retrieves the total page count and sets
     * the value in the variable pageCount.
     *
     * @param pageIndex the current page used as the starting index for the search
     * @param pageSize number of results to be queried by page
     * @return list of customers
     */
    public static List<Customer> findAll(Integer pageIndex, Integer pageSize,String... name) {
        PanacheQuery<PanacheEntityBase> customersQuery;
        String param_name = name[0].toString();
        logger.info("Searching for: "+param_name+" pageSize "+pageSize+" pageIndex "+pageIndex);

        if (!param_name.isEmpty()){
            customersQuery = Customer.find("upper(name) like '%"+param_name.toUpperCase(Locale.ROOT)+"%'").page(Page.of(pageIndex, pageSize));
            logger.info("Find by name");
        }else{
            customersQuery = Customer.findAll().page(Page.of(pageIndex, pageSize));
            logger.info("Find all");
        }

        setPageCount(customersQuery.pageCount());
        return customersQuery.list();
    }

    public static Integer getPageCount() {
        return pageCount;
    }
    public static void setPageCount(Integer pageCount) {
        Customer.pageCount = pageCount;
    }

    public static class Builder {
        private Long customer_id;
        private String name;
        public  String status;
        private Cashback cashback;
        private List<Expense> expenses;

        public Builder(){ };

        public Builder id(Long customer_id) {
            this.customer_id = customer_id;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder cashback(Cashback cashback) {
            this.cashback = cashback;
            return this;
        }

        public Builder expenseList(List<Expense> expenses){
            this.expenses = expenses;
            return this;
        }

        public Builder addOneExpense(Expense expense){
            if (expenses == null){
                new ArrayList<Expense>();
            }else{
                expenses.add(expense);
            }
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }


}


