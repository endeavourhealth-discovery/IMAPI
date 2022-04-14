import express, {Request, Response} from 'express';
import axios from 'axios';

export default class SearchController {
  public path = '/'
  public router = express.Router();
  private service;

  constructor() {
    this.initRoutes();
  }

  private initRoutes() {
    this.router.post('/api/entity/public/search', (req, res) => this.advancedSearch(req, res));
  }

  async advancedSearch(req: Request, res: Response) {

    const searchRequest = req.body;

    if (searchRequest != null && searchRequest.termFilter) {
      if (!searchRequest.index)
        searchRequest.index = 'concept';

      if (searchRequest.termFilter.length < 3) {
        const qry = this.buildCodeKeyQuery(searchRequest);
        await this.getEntities(qry, res);
      }
    }

    res.end();
  }


  private buildCodeKeyQuery(searchRequest: any): any {
    return {
      size: 100,
      query: {
        function_score: {
          query: {
            bool: {
              filter: this.getFilters(searchRequest),
              should: [
                {
                  term: {
                    code: {
                      value: searchRequest.termFilter,
                      boost: 2
                    }
                  }
                },
                {
                  term: {
                    key: {
                      value: searchRequest.termFilter.toLowerCase(),
                      boost: 1
                    },
                  }
                }
              ],
              minimum_should_match: 1,
            },
          },
          functions: [
            {
              filter: {
                match_all: {
                  boost: 1
                }
              },
              field_value_factor: {
                field: "weighting",
                factor: 0.5,
                missing: 1
              }
            }
          ]
        }
      }
    };
  }

  private getFilters(searchRequest: any): any {
    const filter: any = [];
    if (searchRequest?.schemeFilter?.length > 0)
      filter.push(this.getFilter("scheme.@id", searchRequest.schemeFilter));

    if (searchRequest?.statusFilter?.length > 0)
      filter.push(this.getFilter("status.@id", searchRequest.statusFilter));

    if (searchRequest?.typeFilter?.length > 0)
      filter.push(this.getFilter("entityType.@id", searchRequest.typeFilter));

    return filter;
  }

  private getFilter(key: string, values: string[]){
    const result = { "terms" : {} };
    result.terms[key] = values;
    return result;
  }

  private async getEntities(qry: any, res: Response) {
    try {
      const osRes = await axios.post(process.env.OPENSEARCH_URL as string, qry, { headers: { Authorization: 'Basic ' + process.env.OPENSEARCH_AUTH, 'Content-Type': 'application/json' } });

      if (osRes.status >= 300) {
        console.error('Error calling opensearch : ' + osRes.status);
        res.setHeader('Content-Type', 'text/plain').status(500).send('Error calling opensearch : ' + osRes.status);
      } else {

        const r = osRes.data.hits.hits
          .map(h => h._source)
          .map(s => ({
            iri: s.iri,
            name: s.name,
            code: s.code,
            scheme: s.scheme,
            status: s.status,
            entityType: s.entityType,
            match: s.name
          }));

        res.setHeader('Content-Type', 'application/json').send(r);
      }
    } catch (e) {
      console.error('Exception calling opensearch : ');
      console.error(e);
      res.setHeader('Content-Type', 'text/plain').status(500).send('Exception calling opensearch');
    }
  }
}

