import { defineConfig } from 'vite';
import { VitePluginNode } from 'vite-plugin-node';
import path from 'path';

export default defineConfig({
  server: {
    port: 3000
  },
  plugins: [
    ...VitePluginNode({
      adapter: 'express',
      appPath: './src/server.ts',
      exportName: 'viteNodeApp',
      tsCompiler: 'esbuild',
      swcOptions: {}
    })
  ],
  resolve: {
    alias: {"@": path.resolve(__dirname, "./src"), "./runtimeConfig": "./runtimeConfig.browser"}
  },
    ssr: {
      noExternal: ["im-library"]
    }
});
