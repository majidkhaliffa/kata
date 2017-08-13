package fr.majid.kata;

import org.springframework.stereotype.Service;

import fr.majid.kata.model.Account;

/**
 * 
 * @author mortada majid
 *
 */
@Service
public class AccountService {

	public Account  depose(long solde, String accountNumber) {
		return new Account();

	}	
}
