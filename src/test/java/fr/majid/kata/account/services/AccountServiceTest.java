package fr.majid.kata.account.services;

import static fr.majid.kata.builder.GenericBuilder.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.security.auth.login.AccountNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fr.majid.kata.exception.SoldeInsuffisantException;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.model.Customer;
import fr.majid.kata.repository.AccountRepository;
import fr.majid.kata.services.AccountService;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
/**
 *
 * @author mortada majid
 * @email majid.mortada@gmail.com
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
        
		Account actualAcount = accountService.depose(new Amount(100L), account);
		
		assertThat(actualAcount).isEqualToComparingFieldByField(updatedAccount);
	}

	@Test
	public void should_withdraw_from_account() throws Exception {
		String accountNumber = "3200666";
		Customer custom = of(Customer::new)
                .with(Customer::setId,1L).build();
		Account account = of(Account::new)
				.with(Account::setId,1L)
	            .with(Account::setNumero,accountNumber)
	            .with(Account::setCustomer,custom)
	            .with(Account::setSolde,1000L)
	            .build();
		Account accountUpdate = of(Account::new)
				.with(Account::setId,1L)
	            .with(Account::setNumero,accountNumber)
	            .with(Account::setCustomer,custom)
	            .with(Account::setSolde,500L)
	            .build();
		doReturn(accountUpdate).when(accountRepository).save(accountUpdate);
		
		Account accountWithNewDeposit = accountService.withdraw(new Amount(500L), account);
		
		verify(accountRepository, times(1)).save(Mockito.any(Account.class));
		assertThat(accountWithNewDeposit).isEqualToComparingFieldByField(accountUpdate);

	    }

	@Test
	public void should_not_authorize_customer_to_make_withdraw_if_his_solde_is_less_then_amount() {
		String accountNumber = "3200666";
		Customer custom = of(Customer::new)
                .with(Customer::setId,1L).build();
		Account account = of(Account::new)
				.with(Account::setId,1L)
	            .with(Account::setNumero,accountNumber)
	            .with(Account::setCustomer,custom)
	            .with(Account::setSolde,1000L)
	            .build();

	        assertThatThrownBy(() -> accountService.withdraw(new Amount(1001L), account))
	                .isInstanceOf(SoldeInsuffisantException.class);
	    }

}
