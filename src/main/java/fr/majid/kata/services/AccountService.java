package fr.majid.kata.services;

import static fr.majid.kata.builder.GenericBuilder.of;

import java.util.Optional;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.majid.kata.AccountNotFoundException;
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

	@Transactional
	public Account depose(Amount amount, String accountNumber) throws AccountNotFoundException {
		Account account = findAccountByNumero(accountNumber);
		Account a = of(Account::new)
		 .with(Account::setId, account.getId())
           .with(Account::setNumero, account.getNumero())
           .with(Account::setSolde,(account.getSolde() + amount.getValue()))
           .with(Account::setCustomer,account.getCustomer())
           .build();
		Account updateAccount = accountRepository.save(a);
		return updateAccount;
	}
	
	private Account findAccountByNumero(String accountNumber) throws AccountNotFoundException {
		Account account = Optional.of(accountRepository.findByNumero(accountNumber))
				                            .orElseThrow(() -> new AccountNotFoundException("Account not found, please check your account numero"));
		return account;
	}
}
