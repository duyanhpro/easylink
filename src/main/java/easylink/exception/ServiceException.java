package easylink.exception;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {
	String message;
	Exception exception;
	public ServiceException(String msg, Exception e) {
		super();
		this.message = msg;
		this.exception = e;
	}
	
	public ServiceException(String msg) {
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
