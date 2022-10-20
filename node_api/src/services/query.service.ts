import Env from "@/services/env.service";
import { QueryRequest } from "im-library/dist/types/interfaces/Interfaces";
import { Vocabulary } from "im-library/dist/api";
const { IM, RDFS } = Vocabulary;

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
    } as any;
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
    } as any;
    let suggestions = [] as any[];
    try {
      const allowableRanges = await this.queryIM(allowableRangesQuery);
      if (allowableRanges.entities) {
        for (const entity of allowableRanges.entities) {
          const from = {
            includeSubtypes: true,
            "@id": entity["@id"]
          };
          subtypesQuery.query.from.push(from);
        }
        const response = await this.queryIM(subtypesQuery);
        suggestions = searchTerm ? this.filterSuggestions(searchTerm, response.entities) : response.entities;
        suggestions = suggestions.map(suggestion => {
          return { "@id": suggestion["@id"], name: suggestion[RDFS.LABEL] };
        });
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
    } as any;
    let suggestions = [] as any[];
    try {
      const response = await this.queryIM(query);
      suggestions = searchTerm ? this.filterSuggestions(searchTerm, response.entities) : response.entities;
      return suggestions.map(suggestion => {
        return { "@id": suggestion["@id"], name: suggestion[RDFS.LABEL] };
      });
    } catch (error) {
      return suggestions;
    }
  }

  filterSuggestions(query: string, suggestions: any[]) {
    const filteredSuggestions = [];

    for (let i = 0; i < suggestions.length; i++) {
      let item = suggestions[i];
      const codeStartsWith = item[IM.CODE] && item[IM.CODE].indexOf(query) === 0;
      const nameStartsWith = item[RDFS.LABEL].toLowerCase().indexOf(query.toLowerCase()) === 0;
      if (nameStartsWith || codeStartsWith) {
        filteredSuggestions.push(item);
      }
    }

    return filteredSuggestions;
  }
}
