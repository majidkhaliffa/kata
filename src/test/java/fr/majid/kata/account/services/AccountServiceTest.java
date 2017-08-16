package fr.majid.kata.account.services;

import static fr.majid.kata.builder.GenericBuilder.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import javax.security.auth.login.AccountNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.model.Customer;
import fr.majid.kata.repository.AccountRepository;
import fr.majid.kata.services.AccountService;

/**
 * 
 * @author mortada majid
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@Spy
	@InjectMocks
	private AccountService accountService;

	@Mock
    private AccountRepository accountRepository;
	
	@Test
	public void should_make_a_deposit_in_a_given_account() throws AccountNotFoundException {
		String accountNumber = "3200666";			
		Customer custom = of(Customer::new)
                .with(Customer::setId,1L).build();		
		Account account = of(Account::new)
				.with(Account::setId,1L)
				.with(Account::setNumero,accountNumber)
				.with(Account::setCustomer,custom)
				.with(Account::setSolde,1000L).build();
		
		Account updatedAccount = of(Account::new)
				.with(Account::setId,1L)
				.with(Account::setNumero,accountNumber)
				.with(Account::setCustomer,custom)
				.with(Account::setSolde,1100L).build();
		
		
		doReturn(account).when(accountRepository).findByNumero(accountNumber);
        doReturn(updatedAccount).when(accountRepository).save(updatedAccount);
        
		Account actualAcount = accountService.depose(new Amount(100L), accountNumber);
		
		assertThat(actualAcount).isEqualToComparingFieldByField(updatedAccount);
	}

}
