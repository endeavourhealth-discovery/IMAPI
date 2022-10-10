import express, { Application, Request, Response } from "express";
import cors from "cors";
import * as https from "https";
import * as fs from "fs";
// import swaggerAutogen from 'swagger-autogen';
// import swaggerUi from 'swagger-ui-express';
// import swaggerFile from './swagger_output.json';

class App {
  public app: Application;
  public port: number;
  public router = express.Router();

  constructor(appInit: {
    port: number;
    controllers: any[];
    middleWares: any[];
  }) {
    this.app = express();
    this.port = appInit.port;

    this.app.use(
      cors({
        origin: "*" || (process.env.ALLOWED_HOSTS as string),
        optionsSuccessStatus: 200,
      })
    );

    this.app.options("*", cors());

    // this.app.use('/swagger', swaggerUi.serve, swaggerUi.setup(swaggerFile))

    appInit.middleWares.forEach((m) => this.app.use(m));

    appInit.controllers.forEach((c) => this.app.use(c.path, c.router));
  }

  public listen() {
    const prod: boolean = process.env.NODE_ENV === "production";

    this.app.listen(prod ? 8000 : this.port, () => {
      console.log(`App started on port ${this.port}`);
    });

    if (prod) {
      const options = {
        key: fs.readFileSync("/srv/www/keys/my-site-key.pem"),
        cert: fs.readFileSync("/srv/www/keys/chain.pem"),
      };

      https.createServer(options, this.app).listen(this.port);
    }
  }
}

export default App;
