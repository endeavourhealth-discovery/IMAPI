import express, { Request, Response } from 'express';
import axios from 'axios';
import Env from '@/services/env.service';

export default class SearchController {
  public path = '/'
  public router = express.Router();

  constructor() {
    this.initRoutes();
  }

  private initRoutes() {
    this.router.post('/api/entity/public/search', (req, res) => this.advancedSearch(req, res));
  }

  async advancedSearch(req: Request, res: Response) {

    const searchRequest = req.body;
    let result = [];

    if (searchRequest != null && searchRequest.termFilter) {
      if (!searchRequest.index)
        searchRequest.index = 'concept';

      if (searchRequest.termFilter.length < 3) {
        const qry = this.buildCodeKeyQuery(searchRequest);
        result = await this.getEntities(qry);
      } else if (!searchRequest.termFilter.includes(" ")) {
        const qry = this.buildSimpleTermCodeMatch(searchRequest);
        result = await this.getEntities(qry);
      } else {
        let qry = this.buildSimpleTermMatch(searchRequest);
        result = await this.getEntities(qry);
        if (result.length === 0) {
          let qry = this.buildMultiWordMatch(searchRequest);
          result = await this.getEntities(qry);
        }
      }
    }

    res.setHeader('Content-Type', 'application/json').send(result);
    res.end();
  }

  private buildCodeKeyQuery(searchRequest: any): any {
    return this.wrapBoolQuery({
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
    });
  }

  private buildSimpleTermCodeMatch(searchRequest: any): any {
    // Fix prefixes if contains ":"

    return this.wrapBoolQuery({
      filter: this.getFilters(searchRequest),
      should: [
        {
          match_phrase: {
            "termCode.term": {
              query: searchRequest.termFilter,
              boost: 1.5
            }
          }
        },
        {
          match_phrase_prefix: {
            "termCode.term": {
              query: searchRequest.termFilter,
              boost: 0.5
            }
          }
        },
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
            iri: {
              value: searchRequest.termFilter,
              boost: 2
            }
          }
        }
      ],
      minimum_should_match: 1,
    });
  }

  private buildSimpleTermMatch(searchRequest: any): any {
    return this.wrapBoolQuery({
      filter: this.getFilters(searchRequest),
      should: [
        {
          match_phrase: {
            "termCode.term": {
              query: searchRequest.termFilter,
              boost: 1.5
            }
          }
        },
        {
          match_phrase_prefix: {
            "termCode.term": {
              query: searchRequest.termFilter,
              boost: 0.5
            }
          }
        }
      ],
      minimum_should_match: 1,
    });
  }

  private buildMultiWordMatch(searchRequest: any): any {
    const musts: any[] = [];

    const words: string[] = searchRequest.termFilter.split(" ");

    for (let i = 0; i < words.length; i++) {
      musts.push({
        match_phrase: {
          "termCode.term": {
            query: words[i],
            boost: i == 0 ? 4 : 1
          }
        }
      });
    }

    return this.wrapBoolQuery({
      filter: this.getFilters(searchRequest),
      must: musts,
      minimum_should_match: 1,
    });
  }

  private wrapBoolQuery(boolQuery: any): any {
    return {
      size: 100,
      query: {
        function_score: {
          query: {
            bool: boolQuery,
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
          ],
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

  private getFilter(key: string, values: string[]) {
    const result: any = { "terms": {} };
    result.terms[key] = values;
    return result;
  }

  private async getEntities(qry: any) {
    const osRes: any = await axios.post(Env.OPENSEARCH_URL as string, qry, { headers: { Authorization: 'Basic ' + Env.OPENSEARCH_AUTH, 'Content-Type': 'application/json' } });

    return osRes.data.hits.hits
      .map((h: any) => h._source)
      .map((s: any) => ({
        iri: s.iri,
        name: s.name,
        code: s.code,
        scheme: s.scheme,
        status: s.status,
        entityType: s.entityType,
        match: s.name
      }));
  }
}

