import App from './app';
import * as dotenv from "dotenv";

import SearchController from './controllers/searchController';

import bodyParser from 'body-parser';
import EntityController from './controllers/entityController';

dotenv.config({ path: __dirname+'/.env' });

const app = new App({
  port: 3000,
  controllers: [
    new SearchController(),
    new EntityController()
  ],
  middleWares: [
    bodyParser.json(),
    bodyParser.urlencoded({ extended: true})
  ],
})

app.listen();
