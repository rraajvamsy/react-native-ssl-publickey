import { StyleSheet, Text, View } from 'react-native';

import * as ReactNativeSslPublickey from 'react-native-ssl-publickey';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ReactNativeSslPublickey.hello()}</Text>
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
