import App from './app';
import * as dotenv from "dotenv";

import SearchController from './controllers/searchController';

const bodyParser = require('body-parser');

import EntityController from './controllers/entityController';
import QueryController from './controllers/queryController';
import ValidationController from './controllers/validationController';

dotenv.config({ path: __dirname+'/.env' });
const dns = require('dns');
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
