package com.github.scribejava.core.pkce;

import com.github.scribejava.core.base64.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum PKCECodeChallengeMethod {
    S256 {
        //@Override
        public String transform2CodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
            return Base64.encodeUrlWithoutPadding(
                    MessageDigest.getInstance("SHA-256").digest(Charset.forName("US-ASCII").encode(codeVerifier).array()));
        }

        //@Override
		public String getName()
		{
			return "S256";
		}
    },
    PLAIN {
        //@Override
        public String transform2CodeChallenge(String codeVerifier) {
            return codeVerifier;
        }

        //@Override
		public String getName()
		{
			return "plain";
		}
    };

    public abstract String transform2CodeChallenge(String codeVerifier) throws NoSuchAlgorithmException;
	public abstract String getName();
}
