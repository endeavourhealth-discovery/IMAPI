import App from './app';
import * as dotenv from "dotenv";

import SearchController from './controllers/searchController';

const bodyParser = require('body-parser');

import EntityController from './controllers/entityController';
import QueryController from './controllers/queryController';

dotenv.config({ path: __dirname+'/.env' });

const app = new App({
  port: 3000,
  controllers: [
    new SearchController(),
    new EntityController(),
    new QueryController()
  ],
  middleWares: [
    bodyParser.json(),
    bodyParser.urlencoded({ extended: true})
  ],
})

app.listen();
