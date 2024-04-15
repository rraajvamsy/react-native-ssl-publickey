package expo.modules.sslpublickey

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLPeerUnverifiedException
import javax.security.cert.X509Certificate
import java.net.URL
import java.io.IOException

import android.util.Log

class ReactNativeSslPublickeyModule : Module() {

  override fun definition() = ModuleDefinition {
    Name("ReactNativeSslPublickey")

    Function("getPublicHashKey") { domain: String ->
      try {
        val hash = getSSLCertificateHash(domain)
        hash
      } catch (e: Exception) {
        Log.e("ReactNativeSslPublickey", "Error fetching pin for domain: $domain", e)
        null
      }
    }
  }

  private fun getSSLCertificateHash(domain: String): String? {
    val connection = (URL("https://$domain").openConnection() as HttpsURLConnection).apply {
        connect()
        Log.d("ReactNativeSslPublickey", "Connection established for domain: $domain")
    }

    return try {
        val certs = connection.serverCertificates
        
        val hashList = mutableListOf<String>()

        certs.forEachIndexed { index, cert ->
            try {
                val publicKey = cert.publicKey.encoded
                val digest = MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(publicKey)
                val hashString = Base64.getEncoder().encodeToString(hash)
                hashList.add(hashString)
            } catch (e: Exception) {
                Log.e("ReactNativeSslPublickey", "Error processing certificate[$index]: $e")
            }
        }
        if (hashList.isEmpty()) {
            throw SSLPeerUnverifiedException("No SSL certificates found for the domain.")
        }
        // Return the concatenated SHA-256 hashes of all the public keys
        hashList[0]
    } catch (e: Exception) {
        Log.e("ReactNativeSslPublickey", "Error fetching certificates: $e")
        null
    } finally {
        connection.disconnect()
    }
}


}
