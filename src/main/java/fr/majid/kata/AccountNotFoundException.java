package fr.majid.kata;

/**
 * 
 * @author mortada majid
 *
 */
public class AccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;

    public AccountNotFoundException(String message) {
        this.message = message;
    }
}
