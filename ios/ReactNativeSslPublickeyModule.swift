import ExpoModulesCore

import CryptoKit
import Foundation
import React
import Security
import CommonCrypto

// Function to convert PEM string to DER format
func pemToDer(pemString: String) -> Data? {
    // Remove PEM header and footer
    let pemContent = pemString.components(separatedBy: "\n").filter { !$0.hasPrefix("-----") }.joined()
    return Data(base64Encoded: pemContent)
}

// Function to compute SHA256 hash of data
func sha256(data: Data) -> Data {
    return Data(SHA256.hash(data: data))
}

// Function to convert Data to base64 string
func dataToBase64(data: Data) -> String {
    return data.base64EncodedString()
}

var base64 = ""

class SslPinningDelegate: NSObject, URLSessionDelegate {
    func urlSession(_ session: URLSession, didReceive challenge: URLAuthenticationChallenge, completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Void) {
        guard let serverTrust = challenge.protectionSpace.serverTrust else {
            completionHandler(.cancelAuthenticationChallenge, nil)
            return
        }

        let certificate = SecTrustGetCertificateAtIndex(serverTrust, 0)
        var error: Unmanaged<CFError>?
        let data = SecKeyCopyExternalRepresentation(SecCertificateCopyKey(certificate!)!, &error)! as Data
        let base64PublicKey = data.base64EncodedString()
        var pemString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A"+base64PublicKey

        var pemStringWithNewlines = ""
        var index = pemString.startIndex
        while index < pemString.endIndex {
            let endIndex = pemString.index(index, offsetBy: 64, limitedBy: pemString.endIndex) ?? pemString.endIndex
            let substring = pemString[index..<endIndex]
            pemStringWithNewlines += String(substring) + "\n"
            index = endIndex
        }
        pemString = "-----BEGIN PUBLIC KEY-----\n" + pemStringWithNewlines + "-----END PUBLIC KEY-----\n"
        
    
        base64 = ""
        if let derData = pemToDer(pemString: pemString) {
            let sha256Hash = sha256(data: derData)
            base64 = dataToBase64(data: sha256Hash)
        }
        completionHandler(.useCredential, URLCredential(trust: serverTrust))
    }
}

public class ReactNativeSslPublickeyModule: Module {
    public func definition() -> ModuleDefinition {
        Name("ReactNativeSslPublickey")

        AsyncFunction("getPublicHashKey") { (domain: String) async -> String? in
            
            // Check if domain is empty or nil
            guard !domain.isEmpty else {
                NSLog("Domain parameter is empty or nil")
                return nil
            }
            
            do {
                let session = URLSession(configuration: .default, delegate: SslPinningDelegate(), delegateQueue: nil)
                let (data, response) = try await session.data(from: URL(string: "https://\(domain)")!)
                
                guard let response = response as? HTTPURLResponse else {
                    NSLog("Invalid HTTP response")
                    return nil
                }
                
                return base64
            } catch {
                NSLog("Error: %@", error.localizedDescription)
                return nil
            }
        }
    }
}
