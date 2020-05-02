package rmiserver;

public class TokenRequest {
	public final String grant_type;
	public final String client_id;
	public final String client_secret;
	public final String username;
	public final String password;
	
	public TokenRequest(String grant_type, String client_id, String client_secret, String username, String password) {
		super();
		this.grant_type = grant_type;
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.username = username;
		this.password = password;
	}
	
	
}
