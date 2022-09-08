import App from './app';
import * as dotenv from "dotenv";

import SearchController from './controllers/searchController';

import EntityController from './controllers/entityController';
import QueryController from './controllers/queryController';
import ValidationController from './controllers/validationController';
import bodyParser from 'body-parser';
import * as dns from 'dns';

dotenv.config({ path: './src/.env' });

dns.setDefaultResultOrder('ipv4first');

const app = new App({
  port: 3000,
  controllers: [
    new SearchController(),
    new EntityController(),
    new QueryController(),
    new ValidationController()
  ],
  middleWares: [
    bodyParser.json(),
    bodyParser.urlencoded({ extended: true})
  ],
})

app.listen();

export const viteNodeApp = app.app;
