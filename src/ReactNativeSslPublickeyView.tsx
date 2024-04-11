import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ReactNativeSslPublickeyViewProps } from './ReactNativeSslPublickey.types';

const NativeView: React.ComponentType<ReactNativeSslPublickeyViewProps> =
  requireNativeViewManager('ReactNativeSslPublickey');

export default function ReactNativeSslPublickeyView(props: ReactNativeSslPublickeyViewProps) {
  return <NativeView {...props} />;
}
