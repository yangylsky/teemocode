package tk.teemocode.web.util;

import javax.servlet.http.Cookie;

public class LoginVerification {
	private Integer loginMode;

	private Cookie verificationCookie;

	private String verifiedData;

	private String identity;

	public Integer getLoginMode() {
		return loginMode;
	}

	public void setLoginMode(Integer loginMode) {
		this.loginMode = loginMode;
	}

	public Cookie getVerificationCookie() {
		return verificationCookie;
	}

	public void setVerificationCookie(Cookie verificationCookie) {
		this.verificationCookie = verificationCookie;
	}

	public String getVerifiedData() {
		return verifiedData;
	}

	public void setVerifiedData(String verifiedData) {
		this.verifiedData = verifiedData;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
}
