package fr.majid.kata.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import fr.majid.kata.AccountService;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {
	 private AccountService accountService = Mockito.mock(AccountService.class);
	 
	     private MockMvc mockMvc;
	 
	     @Before
	     public void init() {
	         mockMvc = MockMvcBuilders.standaloneSetup(new AccountController(accountService)).build();
	     }
	 
	     @Test
	     public void should_test_noting() {
	 
	     }
	 }
