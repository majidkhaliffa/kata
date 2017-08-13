package fr.majid.kata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.majid.kata.AccountService;
import fr.majid.kata.model.Account;

@RestController
@RequestMapping("/kata")
public class AccountController {

	@Autowired
	private AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/accounts/{accountNumero}")
	public Account update(@PathVariable String accountNumero, @RequestBody long amount) {
		return accountService.depose(amount, accountNumero);
	}
}