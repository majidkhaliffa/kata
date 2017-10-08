package fr.majid.kata.exception;

public class SoldeInsuffisantException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String message;

	public SoldeInsuffisantException(String message) {
		this.message = message;
	}
}