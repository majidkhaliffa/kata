package fr.majid.kata.account.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fr.majid.kata.AccountService;

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
		accountService.depose(530L, accountNumber);
	}

}
