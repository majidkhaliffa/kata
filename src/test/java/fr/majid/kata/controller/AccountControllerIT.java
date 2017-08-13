package fr.majid.kata.controller;

import static fr.majid.kata.builder.GenericBuilder.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;

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

import fr.majid.kata.AccountService;
import fr.majid.kata.KataApplication;
import fr.majid.kata.builder.GenericBuilder;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.model.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = KataApplication.class, 
                webEnvironment = WebEnvironment.RANDOM_PORT,                
                properties = {"server.context-path=/kata-it", "server.port=0"})

public class AccountControllerIT {

    private static final String BASE_URL = "http://localhost:";
   
    private static TestRestTemplate restTemplate;
    
    @Value("${local.server.port}")
    private int httpPort;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @MockBean
    private AccountService accountService;
    
	private String ACOUNT_ENDPOINT = "/kata/accounts";	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	private Account account;
     
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

	}
    
    @Test
    public void that_deposit_by_account_numero_should_Return_Success() throws Exception {
    	doReturn(account).when(accountService).depose(Mockito.anyObject(), Mockito.any()); 
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
		HttpEntity<String> entity = new HttpEntity<String>(this.json(new Amount(1000L)),httpHeaders);
		
        ResponseEntity<Account> response = restTemplate.exchange(BASE_URL + httpPort + "/kata-it"+ACOUNT_ENDPOINT+"/1000236",HttpMethod.PUT, entity,
        		Account.class);       
        Account body = response.getBody();
        
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).as("Status code").isEqualTo(HttpStatus.OK);

        
        assertThat(body.getSolde()).as("solde").isNotEqualTo(530L);
        
    }
    
    protected String json(Object o) throws Exception {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage(); 
			this.mappingJackson2HttpMessageConverter.write(
			        o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);		
        return mockHttpOutputMessage.getBodyAsString();
    }
       
}
