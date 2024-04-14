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
    Log.d("ReactNativeSslPublickey", "Fetching public key hash for domain: $domain")

    val connection = (URL("https://$domain").openConnection() as HttpsURLConnection).apply {
        connect()
        Log.d("ReactNativeSslPublickey", "Connection established for domain: $domain")
    }

    return try {
        val certs = connection.serverCertificates
        Log.d("ReactNativeSslPublickey", "Certificates found: ${certs.size}")
        
        val hashList = mutableListOf<String>()

        certs.forEachIndexed { index, cert ->
            Log.d("ReactNativeSslPublickey", "Certificate[$index]: $cert")
            try {
                val publicKey = cert.publicKey.encoded
                Log.d("ReactNativeSslPublickey", "Extracted public key from certificate")

                val digest = MessageDigest.getInstance("SHA-256")
                Log.d("ReactNativeSslPublickey", "Created SHA-256 digest instance")

                val hash = digest.digest(publicKey)
                Log.d("ReactNativeSslPublickey", "Computed SHA-256 hash of public key")

                val hashString = Base64.getEncoder().encodeToString(hash)
                Log.d("ReactNativeSslPublickey", "Hash of public key: $hashString")

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
