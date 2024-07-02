package com.github.scribejava.core.extractors;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1Token;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.utils.Preconditions;


public abstract class AbstractOAuth1JSONTokenExtractor<T extends OAuth1Token> implements TokenExtractor<T> {

    //@Override
    public T extract(Response response) throws IOException {
        final String rawBody = response.getBody();
        Preconditions.checkEmptyString(rawBody,
                "Response body is incorrect. Can't extract a token from an empty string");

    	final JSONTokener tokenizer = new JSONTokener(rawBody);
		try {
    		final JSONObject body = new JSONObject(tokenizer);

	        final String token = body.getString(OAuthConstants.TOKEN);
	        final String secret = body.getString(OAuthConstants.TOKEN_SECRET);
	
	        return createToken(token, secret, rawBody);
    	} catch(JSONException e) {
    		throw new OAuthException("Response body is incorrect. Can't extract token and secret from this: '"
    				+ rawBody + '\'', null);    		
    	}
    }

    protected abstract T createToken(String token, String secret, String response);
}
