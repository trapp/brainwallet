package net.zaeda.coinaddress;

import com.google.bitcoin.core.ECKey;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AdressCreator {
    public ECKey createAddress(String secret) {
        byte[] hash = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(secret.getBytes("UTF-8"));
            hash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ECKey key = new ECKey(hash, (byte[])null);
        return key;
    }
}
