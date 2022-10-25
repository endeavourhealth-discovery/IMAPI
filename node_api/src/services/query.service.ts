import { TTIriRef } from "@/helpers/text/Clause";
import Env from "@/services/env.service";
import { IM, RDFS } from "@/vocabulary";
import { QueryRequest } from "im-library/dist/types/interfaces/Interfaces";
import { Query, TTAlias } from "im-library/dist/types/models/modules/AutoGen";

export default class QueryService {
  axios: any;

  constructor(axios: any) {
    this.axios = axios;
  }

  public async queryIM(query: QueryRequest, controller?: AbortController) {
    try {
      const response = await this.axios.post(Env.API + "api/query/public/queryIM", query);
      return response.data;
    } catch (error) {
      return {} as any;
    }
  }

  public async getAllowableRangeSuggestions(iri: string, searchTerm?: string) {
    const allowableRangesQuery = {
      argument: {
        this: iri
      },
      query: {
        name: "Allowable Ranges for a property",
        description: "'using property domains get the allowable properties from the supertypes of this concept",
        from: [
          {
            "@id": "http://endhealth.info/im#Concept",
            isType: true
          }
        ],
        where: {
          property: {
            inverse: true,
            "@id": "http://www.w3.org/2000/01/rdf-schema#range"
          },
          is: {
            variable: "$this",
            includeSupertypes: true,
            includeSubtypes: true
          }
        },
        select: [
          {
            property: {
              "@id": "http://endhealth.info/im#code"
            }
          },
          {
            property: {
              "@id": "http://www.w3.org/2000/01/rdf-schema#label"
            }
          }
        ],
        activeOnly: true
      }
    } as QueryRequest;
    const subtypesQuery = {
      query: {
        name: "All subtypes of an entity, active only",
        from: [] as any[],
        select: [
          {
            property: {
              "@id": "http://endhealth.info/im#code"
            }
          },
          {
            property: {
              "@id": "http://www.w3.org/2000/01/rdf-schema#label"
            }
          }
        ],
        activeOnly: true
      }
    } as QueryRequest;
    let suggestions = [] as any[];
    try {
      const allowableRanges = await this.queryIM(allowableRangesQuery);
      if (allowableRanges.entities) {
        for (const entity of allowableRanges.entities) {
          const from = {
            includeSubtypes: true,
            "@id": entity["@id"]
          };
          subtypesQuery.query.from.push(from as TTAlias);
        }
        if (searchTerm) {
          subtypesQuery.textSearch = searchTerm;
        }
        suggestions = (await this.queryIM(subtypesQuery)).entities;
        suggestions = this.convertTTEntitiesToTTIriRefs(suggestions);
      }
      return suggestions;
    } catch (error) {
      return suggestions;
    }
  }

  public async getAllowablePropertySuggestions(iri: string, searchTerm?: string) {
    const query = {
      argument: {
        this: iri
      },
      query: {
        name: "Allowable Properties for a concept",
        description: "'using property domains get the allowable properties from the supertypes of this concept",
        from: [
          {
            "@id": "http://endhealth.info/im#Concept",
            isType: true
          }
        ],
        where: {
          property: {
            "@id": "http://www.w3.org/2000/01/rdf-schema#domain"
          },
          is: {
            variable: "$this",
            includeSupertypes: true
          }
        },
        select: [
          {
            property: {
              "@id": "http://www.w3.org/2000/01/rdf-schema#label"
            }
          },
          {
            property: {
              "@id": "http://endhealth.info/im#code"
            }
          }
        ],
        activeOnly: true
      }
    } as QueryRequest;
    let suggestions = [] as any[];
    try {
      if (searchTerm) {
        query.textSearch = searchTerm;
      }
      suggestions = (await this.queryIM(query)).entities;
      return this.convertTTEntitiesToTTIriRefs(suggestions);
    } catch (error) {
      return suggestions;
    }
  }

  convertTTEntitiesToTTIriRefs(ttEntities: any[]) {
    return ttEntities.map(ttEntity => this.convertTTEntityToTTIriRef(ttEntity));
  }

  private convertTTEntityToTTIriRef(ttEntity: any) {
    return { "@id": ttEntity["@id"], name: ttEntity[RDFS.LABEL] };
  }
}
