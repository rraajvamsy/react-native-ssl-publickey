import { useEffect, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { getPublicHashKey } from 'react-native-ssl-publickey';

export default function App() {
  const [hash, setHash] = useState("");
  useEffect(() => {
    console.log("hash", hash, getPublicHashKey)
    getPublicHashKey && getPublicHashKey('randomuser.me').then((_hash: string) => {
      console.log(_hash)
      setHash(_hash)
    })
  })
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
