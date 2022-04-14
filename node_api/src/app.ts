import express, { Application } from 'express';
import * as https from 'https';
import * as fs from 'fs';

class App {
  public app: Application
  public port: number

  constructor(appInit: {port: number; controllers: any[]; middleWares: any[]}) {
    this.app = express();
    this.port = appInit.port;

    appInit.middleWares.forEach(m => this.app.use(m));

    appInit.controllers.forEach(c => this.app.use(c.path, c.router));
  }

  public listen() {
    const prod: boolean = (process.env.NODE_ENV === "production");

    this.app.listen(prod ? 8000 : this.port, () => {
      console.log(`App started on port ${this.port}`);
    });

    if (prod) {
      const options = {
        key: fs.readFileSync("/srv/www/keys/my-site-key.pem"),
        cert: fs.readFileSync("/srv/www/keys/chain.pem")
      };

      https.createServer(options, this.app).listen(this.port);
    }
  }
}

export default App;
