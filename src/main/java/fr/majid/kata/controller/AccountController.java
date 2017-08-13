package fr.majid.kata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.majid.kata.AccountService;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;

@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;	

	@RequestMapping(method = RequestMethod.PUT, path = "/kata/accounts/{accountNumero}")
	public Account  handleUpdate(@PathVariable String accountNumero,  @RequestBody Amount value) {
		return accountService.depose(value, accountNumero);
	}
}