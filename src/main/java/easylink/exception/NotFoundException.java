package easylink.exception;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
	String message;
	Exception exception;
	public NotFoundException(String msg, Exception e) {
		super();
		this.message = msg;
		this.exception = e;
	}
	
	public NotFoundException(String msg) {
		super();
		this.message = msg;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	
}
