package utils

import java.math.BigInteger
import java.security.MessageDigest;

object Hash{
  
  def apply( str: String ): String = md5(str)

  def md5( str : String ) : String = {
    try{
        val digest = MessageDigest.getInstance("MD5");
        digest.update(str.getBytes("UTF-8"),0,str.length());
        return new BigInteger(1, digest.digest()).toString(16);
      } catch {
        case e: java.security.NoSuchAlgorithmException => throw new RuntimeException("Invalid hash function", e)
      }
    }
}