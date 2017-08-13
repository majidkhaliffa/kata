package fr.majid.kata.repository;

import static fr.majid.kata.builder.GenericBuilder.of;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import fr.majid.kata.model.Account;
import fr.majid.kata.model.Customer;

/**
 * 
 * @author mortada majid
 *
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

	@Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private Account expectedAccount;
    private Customer customer;
    private  String customerNumber;

    @Before
    public void init() {
    	customerNumber = "33007AC589";
    	 customer = of(Customer::new)
                .with(Customer::setNom, "guest")
                .with(Customer::setPrenom, "guest")
                .build();        
        expectedAccount = of(Account::new)
                 .with(Account::setNumero,customerNumber)
                 .with(Account::setSolde,530L)
                 .with(Account::setCustomer,customer)
                 .build();                
        testEntityManager.persist(customer);
        testEntityManager.persist(expectedAccount);
    }
    
    @Test
    public void should_fetch_account_by_numero() {
        Account resulting = accountRepository.findByNumero(customerNumber);

        assertThat(resulting)
                .isNotNull()
                .isEqualToComparingFieldByField(expectedAccount);
    }

    @Test
    public void should_Save_account() {
    	customerNumber = "33007AC5830";
   	 customer = of(Customer::new)
               .with(Customer::setNom, "guest")
               .with(Customer::setPrenom, "guest")
               .build();        
       Account account = of(Account::new)
                .with(Account::setNumero,customerNumber)
                .with(Account::setSolde,530L)
                .with(Account::setCustomer,customer)
                .build();                
       testEntityManager.persist(customer);
       Account expectedSaveAccount = accountRepository.save(account);

        assertThat(expectedSaveAccount)
                .isNotNull()
                .isEqualToComparingFieldByField(account);
    }

    
    @Test
    public void should_fetch_account_by_Customer() {
    	Account resulting = accountRepository.findByCustomer(customer);

        assertThat(resulting)
                .isNotNull()
                .isEqualToComparingFieldByField(expectedAccount);
    }

    
}

