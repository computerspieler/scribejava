package com.github.scribejava.core.extractors;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.scribejava.core.exceptions.OAuthException;

public abstract class AbstractJsonExtractor {

    protected static String extractRequiredParameterText(JSONObject errorNode, String parameterName, String rawResponse)
            throws OAuthException {
        try {
        	final String value = errorNode.getString(parameterName);
        	return value;
        } catch (JSONException e) {
        	throw new OAuthException("Response body is incorrect. Can't extract a '" + parameterName
                    + "' from this: '" + rawResponse + "'", null);
        }
    }
    
    protected static int extractRequiredParameterInt(JSONObject errorNode, String parameterName, String rawResponse)
            throws OAuthException {
        try {
        	final int value = errorNode.getInt(parameterName);
        	return value;
        } catch (JSONException e) {
        	throw new OAuthException("Response body is incorrect. Can't extract a '" + parameterName
                    + "' from this: '" + rawResponse + "'", null);
        }
    }
}
