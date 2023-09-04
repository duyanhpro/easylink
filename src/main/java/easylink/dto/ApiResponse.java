package easylink.dto;

public class ApiResponse {
	Integer resultCode;
	String message;
	
	public ApiResponse(Integer resultCode, String message) {
		super();
		this.resultCode = resultCode;
		this.message = message;
	}
	public static final int RESULT_OK = 0;
	public static final int RESULT_NOK = 1;

	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
