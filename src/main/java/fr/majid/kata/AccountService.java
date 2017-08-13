package fr.majid.kata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.repository.AccountRepository;

/**
 * 
 * @author mortada majid
 *
 */
@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public Account depose(Amount amount, String accountNumber) {
		Account newAccount = accountRepository.findByNumero(accountNumber);
		return newAccount;

	}
}
