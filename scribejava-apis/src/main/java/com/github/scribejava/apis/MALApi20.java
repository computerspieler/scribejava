package com.github.scribejava.apis;

import java.util.Random;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;
import com.github.scribejava.core.pkce.PKCE;
import com.github.scribejava.core.pkce.PKCECodeChallengeMethod;

public class MALApi20 extends DefaultApi20 {

	protected MALApi20() {}
    
	private static class InstanceHolder {

        private static final MALApi20 INSTANCE = new MALApi20();
    }

    public static MALApi20 instance() {
        return InstanceHolder.INSTANCE;
    }
    
    //@Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }
    
	//@Override
	public String getAccessTokenEndpoint() {
		return "https://myanimelist.net/v1/oauth2/token";
	}

	//@Override
	protected String getAuthorizationBaseUrl() {
		return "https://myanimelist.net/v1/oauth2/authorize";
	}
	
    //@Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }
    
    public static PKCE generatePKCE()
    {
    	final String chars = "azertyuiopqsdfghjklmwxcvnAZERTYUIOPQSDFGHJKLMWXCVBN1234567890-._~";
    	char code[] = new char[128];
    	PKCE output = new PKCE();
    	Random r = new Random();
    	
    	for(int i = 0; i < code.length; i ++)
    		code[i] = chars.charAt(r.nextInt(chars.length()));
    	
    	output.setCodeChallengeMethod(PKCECodeChallengeMethod.PLAIN);
    	output.setCodeVerifier(new String(code));
    	output.setCodeChallenge(new String(code));
    	
    	return output;
    }
}
