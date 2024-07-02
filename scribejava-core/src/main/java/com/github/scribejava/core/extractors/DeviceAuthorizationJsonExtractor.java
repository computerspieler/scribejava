package com.github.scribejava.core.extractors;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.github.scribejava.core.model.DeviceAuthorization;
import com.github.scribejava.core.model.Response;

public class DeviceAuthorizationJsonExtractor extends AbstractJsonExtractor {

    protected DeviceAuthorizationJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final DeviceAuthorizationJsonExtractor INSTANCE = new DeviceAuthorizationJsonExtractor();
    }

    public static DeviceAuthorizationJsonExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    public DeviceAuthorization extract(Response response) throws IOException, JSONException {
        if (response.getCode() != 200) {
            generateError(response);
        }
        return createDeviceAuthorization(response.getBody());
    }

    public void generateError(Response response) throws IOException, JSONException {
        OAuth2AccessTokenJsonExtractor.instance().generateError(response);
    }

    private DeviceAuthorization createDeviceAuthorization(String rawResponse) throws IOException, JSONException {

    	final JSONTokener tokenizer = new JSONTokener(rawResponse);
    	final JSONObject response = new JSONObject(tokenizer);

        final DeviceAuthorization deviceAuthorization = new DeviceAuthorization(
        		extractRequiredParameterText(response, "device_code", rawResponse),
        		extractRequiredParameterText(response, "user_code", rawResponse),
        		extractRequiredParameterText(response, getVerificationUriParamName(), rawResponse),
        		extractRequiredParameterInt(response, "expires_in", rawResponse));

    
        try {
        	deviceAuthorization.setIntervalSeconds(response.getInt("interval"));        	
        } catch (JSONException e) {}

        try {
        	deviceAuthorization.setVerificationUriComplete(response.getString("verification_uri_complete"));        	
        } catch (JSONException e) {}
 
        return deviceAuthorization;
    }

    protected String getVerificationUriParamName() {
        return "verification_uri";
    }
}
