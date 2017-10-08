package fr.majid.kata.controller;

import static fr.majid.kata.builder.GenericBuilder.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import fr.majid.kata.KataApplication;
import fr.majid.kata.builder.GenericBuilder;
import fr.majid.kata.exception.AccountNotFoundException;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.model.Customer;
import fr.majid.kata.services.AccountService;

/**
 *
 * @author mortada majid
 * @email majid.mortada@gmail.com
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = KataApplication.class, 
                webEnvironment = WebEnvironment.RANDOM_PORT,                
                properties = {"server.context-path=/kata-it", "server.port=0"})
public class AccountControllerIT {

    private static TestRestTemplate restTemplate;
    @Autowired
    private WebApplicationContext webApplicationContext;    
    @MockBean
    private AccountService accountService;
   
    
    @Value("${local.server.port}")
    private int httpPort;

    private static final String BASE_URL = "http://localhost:";
	private String ACOUNT_ENDPOINT = "/kata/accounts";	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	private Account account;
	private Account accountupdated;
     
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {        
        restTemplate = new TestRestTemplate();
    }

	@Before
	public void setUp() {
		Customer customer = of(Customer::new)
				.with(Customer::setNom, "guest")
				.with(Customer::setPrenom, "guest")
				.build();
         account = GenericBuilder.of(Account::new)
                .with(Account::setNumero,"1000236")
                .with(Account::setSolde,50l)
                .with(Account::setCustomer,customer)
                .build();
         accountupdated = GenericBuilder.of(Account::new)
                 .with(Account::setNumero,"1000236")
                 .with(Account::setSolde,1050L)
                 .with(Account::setCustomer,customer)
                 .build();     

	}
    
    @Test
    public void that_deposit_by_account_numero_should_Return_Success() throws Exception {    	
    	doReturn(accountupdated).when(accountService).depose(new Amount(1000L), "1000236"); 
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);        
        HttpEntity<String> entity = new HttpEntity<String>(this.json(new Amount(1000L)),httpHeaders);

        ResponseEntity<Account> response = restTemplate.exchange(BASE_URL + httpPort + "/kata-it"+ACOUNT_ENDPOINT+"/1000236/operations/deposit/",HttpMethod.PUT, entity,
        		Account.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).as("Status code").isEqualTo(HttpStatus.OK);        
        assertThat(response.getBody().getSolde()).as("solde").isEqualTo(1050L);        
    }
    
    @Test
    public void that_deposit_by_account_numero_should_Return_faild_with_404() throws Exception {    	
    	doThrow(new AccountNotFoundException("acount not found")).when(accountService).depose(Mockito.anyObject(), Mockito.any()); 
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);        
        HttpEntity<String> entity = new HttpEntity<String>(this.json(new Amount(1000L)),httpHeaders);

        ResponseEntity<Account> response = restTemplate.exchange(BASE_URL + httpPort + "/kata-it" + ACOUNT_ENDPOINT + "/1000236/operations/deposit/",
						HttpMethod.PUT, entity, Account.class);

						assertThat(response).isNotNull();
				        assertThat(response.getStatusCode()).as("Status code").isEqualTo(HttpStatus.NOT_FOUND);          
    }

    @Test
	public void should_make_withdraw_by_account_numero() throws Exception {

	}

    protected String json(Object o) throws Exception {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage(); 
			this.mappingJackson2HttpMessageConverter.write(
			        o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);		
        return mockHttpOutputMessage.getBodyAsString();
    }
       
}
