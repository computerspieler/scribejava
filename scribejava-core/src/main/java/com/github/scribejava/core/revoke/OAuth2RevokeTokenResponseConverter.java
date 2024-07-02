package com.github.scribejava.core.revoke;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.Response;
import java.io.IOException;
import org.json.JSONException;

public class OAuth2RevokeTokenResponseConverter {

    public Void convert(Response response) throws IOException, JSONException {
        if (response.getCode() != 200) {
            OAuth2AccessTokenJsonExtractor.instance().generateError(response);
        }
        return null;
    }
}
