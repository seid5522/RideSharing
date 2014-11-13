/*
 * Copyright (C) 2014 Wensheng Yan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ridesharing.crypto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

/**
 * @file Hash.java
 * @author Wensheng Yan
 * @date Mar 26, 2014
 */
public class Hash {
    private static final String ENCODING = "UTF-8";
    private String algorithm;
    
    public Hash(String algorithm){
        this.algorithm = algorithm;
    }
        
    public byte[] Encrypt(byte[] data){
        MessageDigest md;
        byte[] array = null;
        try {
            md = MessageDigest.getInstance(algorithm);
            array = md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return array;
    }
        
    public byte[] Encrypt(String str){
        byte[] data = null;
        try{
            data = str.getBytes(ENCODING);
            data = Encrypt(data);
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return data;
    }
        
    public String EncryptToString(String str){
        byte[] array = Encrypt(str);
        if(array == null){
            return "";
        }
        return new String(Hex.encodeHex(array));
    }
        
    public String EncryptToString(byte[] data){
        byte[] array = Encrypt(data);
        if(array == null){
            return "";
        }
        return new String(Hex.encodeHex(array));
    }
}
