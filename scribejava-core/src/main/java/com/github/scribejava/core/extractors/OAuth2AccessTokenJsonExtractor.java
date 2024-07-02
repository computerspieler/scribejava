package com.github.scribejava.core.extractors;

import java.io.IOException;
import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth2.OAuth2Error;
import com.github.scribejava.core.utils.Preconditions;

/**
 * JSON (default) implementation of {@link TokenExtractor} for OAuth 2.0
 */
public class OAuth2AccessTokenJsonExtractor extends AbstractJsonExtractor implements TokenExtractor<OAuth2AccessToken> {

    protected OAuth2AccessTokenJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final OAuth2AccessTokenJsonExtractor INSTANCE = new OAuth2AccessTokenJsonExtractor();
    }

    public static OAuth2AccessTokenJsonExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    //@Override
    public OAuth2AccessToken extract(Response response) throws IOException, JSONException {
        final String body = response.getBody();
        Preconditions.checkEmptyString(body, "Response body is incorrect. Can't extract a token from an empty string");

        if (response.getCode() != 200) {
            generateError(response);
        }
        return createToken(body);
    }

    /**
     * Related documentation: https://tools.ietf.org/html/rfc6749#section-5.2
     *
     * @param response response
     * @throws java.io.IOException IOException
     *
     */
    public void generateError(Response response) throws IOException, JSONException {
        final String responseBody = response.getBody();

    	final JSONTokener tokenizer = new JSONTokener(responseBody);
    	final JSONObject responseBodyJson = new JSONObject(tokenizer);
        
    	URI errorUri;
    	try {
    		try {
    			final String errorUriInString = responseBodyJson.getString("error_uri");
    	        errorUri = URI.create(errorUriInString);
        	} catch(JSONException e) {
        		errorUri = null;   		
        	}
        } catch (IllegalArgumentException iae) {
            errorUri = null;
        }

        OAuth2Error errorCode;
        try {
            errorCode = OAuth2Error
                    .parseFrom(extractRequiredParameterText(responseBodyJson, "error", responseBody));
        } catch (IllegalArgumentException iaE) {
            //non oauth standard error code
            errorCode = null;
        }

        String errorDescription = null;
        try {
        	errorDescription = responseBodyJson.getString("error_description");
		} catch(JSONException e) {}

        throw new OAuth2AccessTokenErrorResponse(errorCode, errorDescription == null ? null : errorDescription,
                errorUri, response);
    }

    private OAuth2AccessToken createToken(String rawResponse) throws IOException, JSONException {

    	final JSONTokener tokenizer = new JSONTokener(rawResponse);
    	final JSONObject response = new JSONObject(tokenizer);

    	Integer expiresInNode = null;
    	String refreshToken = null;
    	String scope = null;
    	String tokenType = null;
    	
        try {
        	expiresInNode = response.getInt("expires_in");
		} catch(JSONException e) {}
        try {
        	refreshToken = response.getString(OAuthConstants.REFRESH_TOKEN);
		} catch(JSONException e) {}
        try {
        	scope = response.getString(OAuthConstants.SCOPE);
		} catch(JSONException e) {}
        try {
        	tokenType = response.getString("token_type");
		} catch(JSONException e) {}

        return createToken(extractRequiredParameterText(response, OAuthConstants.ACCESS_TOKEN, rawResponse),
                tokenType, expiresInNode,
                refreshToken, scope, response,
                rawResponse);
    }

    protected OAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, JSONObject response, String rawResponse) {
        return new OAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
    }
}
