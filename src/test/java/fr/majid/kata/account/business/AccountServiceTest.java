package fr.majid.kata.account.business;

import static fr.majid.kata.builder.GenericBuilder.of;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fr.majid.kata.AccountService;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.model.Customer;

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

	@Test
	public void should_make_a_deposit_in_a_given_account() {
		String accountNumber = "3200666";			
		Customer custom = of(Customer::new)
                .with(Customer::setId,1L).build();		
		Account expectedAccount = of(Account::new)				
				.with(Account::setNumero,accountNumber)
				.with(Account::setCustomer,custom)
				.with(Account::setSolde,530L).build();
		
		Account actualAcount = accountService.depose(new Amount(530L), accountNumber);
		
		assertThat(actualAcount).isEqualToComparingFieldByField(expectedAccount);
	}

}
