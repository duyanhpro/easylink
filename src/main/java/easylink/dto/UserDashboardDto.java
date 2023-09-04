package easylink.dto;

public class UserDashboardDto  {
	int id;
	Integer userId;
	String username;
	Integer dashboardId;
	String dashboardName;
	int status;
	String url;
	String	param;	// json array of the params
	
	public UserDashboardDto(int id, Integer userId, String username, Integer dashboardId, String dashboardName,
			int status, String url, String param) {
		super();
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.dashboardId = dashboardId;
		this.dashboardName = dashboardName;
		this.status = status;
		this.url = url;
		this.param = param;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getDashboardId() {
		return dashboardId;
	}
	public void setDashboardId(Integer dashboardId) {
		this.dashboardId = dashboardId;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDashboardName() {
		return dashboardName;
	}
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
