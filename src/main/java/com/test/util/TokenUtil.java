package com.test.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;

public class TokenUtil {

    /**
     * 过期时间15分钟
     */
    private static final long EXPIRE_TIME = 15 * 60 * 1000;

    /**
     * token 私钥
     */
    private static final String TOKEN_SECRET = "f26e57c28064d0e855e72c06a0e618";

    public static final String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("account").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 校验token是否正确
     *
     * @param token
     * @return
     */

    public static boolean vertify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWT.require(algorithm)
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成token
     * @param account
     * @return
     */
    public static final String generalToken(String account) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            HashMap<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            return JWT.create()
                    .withHeader(header)
                    .withClaim("account", account)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }



    public static void main(String[] args) throws UnsupportedEncodingException {
        String token = generalToken("18606291072");
        System.out.println(token + " token");
        long time = new Date().getTime();
        System.out.println(time);
        String account = getAccount("6D4616E806CE5006EE09414E101CA2B42B074B02AC824DC94E66546661601A7F");
        System.out.println(account + " account");
    }

}
