package fr.majid.kata.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.majid.kata.AccountNotFoundException;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.services.AccountService;

/**
 * 
 * @author mortada majid
 * @email majid.mortada@gmail.com
 *
 */
@RestController
public class AccountController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	@Autowired
	private AccountService accountService;	

	@RequestMapping(method = RequestMethod.PUT, path = "/kata/accounts/{accountNumero}")
	public Account  handleUpdate(@PathVariable String accountNumero,  @RequestBody Amount value) throws AccountNotFoundException {		
			MDC.put("operation", "deposit");
			MDC.put("accountNumero", accountNumero);
			LOGGER.info("send reponse");
			return accountService.depose(value, accountNumero);		
	}
	
	@ExceptionHandler({ AccountNotFoundException.class })
	@ResponseStatus(code = HttpStatus.NOT_FOUND)	
	public void  handleAccountNotFound(Exception e) {
		LOGGER.warn("Account not found, please check your account numero");
        
	}
}