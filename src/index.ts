import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ReactNativeSslPublickey.web.ts
// and on native platforms to ReactNativeSslPublickey.ts
import ReactNativeSslPublickeyModule from './ReactNativeSslPublickeyModule';
import ReactNativeSslPublickeyView from './ReactNativeSslPublickeyView';
import { ChangeEventPayload, ReactNativeSslPublickeyViewProps } from './ReactNativeSslPublickey.types';

// Get the native constant value.
export const PI = ReactNativeSslPublickeyModule.PI;

export function hello(): string {
  return ReactNativeSslPublickeyModule.hello();
}

export async function setValueAsync(value: string) {
  return await ReactNativeSslPublickeyModule.setValueAsync(value);
}

const emitter = new EventEmitter(ReactNativeSslPublickeyModule ?? NativeModulesProxy.ReactNativeSslPublickey);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ReactNativeSslPublickeyView, ReactNativeSslPublickeyViewProps, ChangeEventPayload };
