import { requireNativeModule } from 'expo-modules-core';

export async function getPublicHashKey(domain: string): Promise<string> {
  try {
    console.log("Index", domain);
    const ReactNativeSslPublickeyModule = await requireNativeModule('ReactNativeSslPublickey');
    console.log('====================================');
    console.log("index", ReactNativeSslPublickeyModule);
    console.log('====================================');
    return ReactNativeSslPublickeyModule && await ReactNativeSslPublickeyModule.getPublicHashKey(domain);
  } catch (error) {
    // console.error('Error while getting public hash key:', error);
    return ''; // or handle the error in a way that makes sense for your application
  }
}
