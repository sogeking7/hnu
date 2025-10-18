import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'io.ionic.starter',
  appName: 'hnu-app',
  webDir: 'www/browser',
  server: {
    allowNavigation: [
      '10.131.2.162:9000'
    ],
    errorPath: 'assets/upgrade-webview.html'
  },
};

export default config;
