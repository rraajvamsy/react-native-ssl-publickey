### react-native-ssl-publickey

---

#### Overview
`react-native-ssl-publickey` is a React Native library that provides functionality to fetch and compute the SHA-256 hash of the public key from SSL certificates of a given domain. This library is particularly useful for scenarios where you need to verify the SSL certificate of a remote server programmatically within your React Native application.

---

#### Installation
To install `react-native-ssl-publickey`, you can use npm or yarn:

```bash
npm install react-native-ssl-publickey
```
or
```bash
yarn add react-native-ssl-publickey
```

---

#### Usage
After installing the package, you can use the `getPublicHashKey` function to fetch the SHA-256 hash of the public key for a given domain. Here's an example of how you can use it in your React Native application:

```javascript
import { useEffect, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { getPublicHashKey } from 'react-native-ssl-publickey';

export default function App() {
  const [hash, setHash] = useState("");

  useEffect(() => {
    getPublicHashKey && getPublicHashKey('randomuser.me').then((_hash: string) => {
      setHash(_hash)
    })
  }, []);

  return (
    <View style={styles.container}>
      <Text>PinSHA256{hash}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
```

---

#### Under the Hood
The library utilizes native code to establish a connection with the specified domain and extract the SSL certificates. It then computes the SHA-256 hash of the public key from the certificates and returns it to the calling React Native component. 

---

#### Contributing
Contributions to `react-native-ssl-publickey` are welcome! If you find any bugs or want to suggest improvements, feel free to open an issue or submit a pull request on the [GitHub repository](https://github.com/rraajvamsy/react-native-ssl-publickey).

---

#### License
`react-native-ssl-publickey` is licensed under the [MIT License](https://opensource.org/licenses/MIT). You are free to use, modify, and distribute this software as per the terms of the license.

--- 
