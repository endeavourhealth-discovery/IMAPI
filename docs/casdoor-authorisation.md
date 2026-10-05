# Authentication and authorisation with Casdoor

IMAPI no longer uses Endeavour Security. Callers authenticate with a Casdoor access token, and IMAPI talks to Casdoor directly.

## Authentication

Frontends sign users in with Casdoor (OIDC) and send the access token on every call:

```
Authorization: Bearer <Casdoor access token>
```

`SecurityConfig` makes IMAPI an OAuth2 resource server. A token is accepted only if it:

- is signed by Casdoor (keys from `<CASDOOR_URL>/.well-known/jwks`),
- has the expected issuer and has not expired,
- is an access token (refresh tokens are refused), and
- was issued to one of the applications in `CASDOOR_ALLOWED_CLIENT_IDS` (its `aud` claim).

The token only proves *who* is calling. Roles, namespaces and preferences are read from Casdoor (cached for 60 seconds), so changes take effect within a minute whatever the token lifetime.

Tokens issued with the `client_credentials` grant (server-to-server, e.g. the IMQueryRunner worker) are recognised by the `type: application` claim. They identify an application, not a person: the user has no roles or namespaces, so such callers can only use endpoints that need authentication but no permission.

### Environment variables

| Variable | Purpose |
|---|---|
| `CASDOOR_URL` | Base URL of Casdoor. **Must be `https`**: an http to https redirect drops the `Authorization` header and turns POSTs into GETs. |
| `CASDOOR_ISSUER` | Expected `iss` claim. Defaults to `CASDOOR_URL`; set it if Casdoor's configured origin differs. |
| `CASDOOR_ORGANISATION_NAME` | The Casdoor organisation that owns the users. |
| `CASDOOR_CLIENT_ID`, `CASDOOR_CLIENT_SECRET` | The Casdoor application IMAPI uses for management calls (read users, update preferences, enforce). |
| `CASDOOR_ENFORCER_ID` | The casbin enforcer (`owner/name`) consulted for authorisation. |
| `CASDOOR_ALLOWED_CLIENT_IDS` | Comma-separated client ids (token audiences) of the applications whose tokens are accepted, e.g. IMQueryRunner and IMDirectory. |
| `CASDOOR_JWS_ALGORITHM` | Optional. Signing algorithm of access tokens. Default `RS256`. |

Casdoor is only contacted when a request needs it, so the app starts (and serves public endpoints) without these set.

## Authorisation

`SecurityService.requiresPermission(Resource, Action)` posts this to Casdoor:

```
POST {CASDOOR_URL}/api/enforce?enforcerId={CASDOOR_ENFORCER_ID}
Authorization: Basic <application credentials>

[ <user>, "<RESOURCE>", "<ACTION>" ]
```

`<user>` is the IMAPI `User` with its top-level keys capitalised and without the password. Casdoor builds a Go struct from it, which only allows exported (capitalised) fields, so a matcher reads `r.sub.Roles`, `r.sub.Username`, `r.sub.Namespaces` and so on.

Namespace access depends on the data being touched, so it cannot be part of the enforce request. It is checked in IMAPI with `SecurityService.requiresNamespace(namespace, read, write)` against the user's namespaces. Where both apply, both must pass.

### Resources and actions

Each row lists the roles that were required before this migration; these are what the enforcer's policy needs to grant. "Namespace" means the endpoint also requires write access to the namespace being changed.

| Resource | Action | Roles | Namespace | Endpoint(s) |
|---|---|---|---|---|
| ENTITY | CREATE | CREATOR | write | `POST entity/private/create` |
| ENTITY | UPDATE | EDITOR | write | `POST entity/private/update`, `POST file/entity` |
| (none) | (none) | any signed-in user | read | `GET entity/protected/entityExists` |
| DOCUMENT | WRITE | EDITOR | write | `POST file/document` |
| DOCUMENT | READ | EDITOR | | `GET file/document/{taskId}` |
| FOLDER | CREATE | CREATOR | write | `POST folder/create` |
| FOLDER | UPDATE | EDITOR | write | `POST folder/move`, `POST folder/add` |
| DELTA | READ | ADMIN | | `GET deltas/download` |
| GITHUB | UPDATE | ADMIN | | `POST updateGithubConfig` |
| QUERY | EXECUTE | EXECUTOR | | `findRequestMissingArguments`, `argumentType` |
| SET | PUBLISH | PUBLISHER | | `GET set/private/publish` |
| SET | UPDATE | EDITOR | write | `POST set/private/updateSubsetsFromSuper` |
| USER | READ | TASK_MANAGER, DEVELOPER | | `getUsersInGroup`, `getGroups` |
| USER | UPDATE | ADMIN | | `POST user/private/namespaces` |
| BUG_REPORT | UPDATE | DEVELOPER | | `updateBugReport` |
| TASK | READ | DEVELOPER, TASK_MANAGER | | `getTasksByCreatedBy`, `getTasksByAssignedTo`, `getUnassignedTasks`, `getTask` |
| TASK | DELETE | DEVELOPER, TASK_MANAGER | | `deleteTask` |
| TASK | UPDATE | TASK_MANAGER | | `updateTask` |
| ROLE_REQUEST | READ / UPDATE / REJECT | TASK_MANAGER | | `roleRequest`, `updateRoleRequest`, `rejectRoleRequest` |
| ROLE_REQUEST | APPROVE | APPROVER | | `approveRoleRequest` |
| NAMESPACE_REQUEST | READ / UPDATE | TASK_MANAGER | | `namespaceRequest`, `updateNamespaceRequest` |
| NAMESPACE_REQUEST | APPROVE / REJECT | APPROVER | | `approveNamespaceRequest`, `rejectNamespaceRequest` |
| ENTITY_APPROVAL | CREATE | EDITOR, CREATOR | | `createEntityApproval` |
| ENTITY_APPROVAL | READ / UPDATE | TASK_MANAGER | | `entityApproval`, `updateEntityApproval` |
| ENTITY_APPROVAL | APPROVE / REJECT | APPROVER | | `approveEntityApproval`, `rejectEntityApproval` |
| UPRN | EXECUTE | UPRN | | the four UPRN endpoints |

### Policy that reproduces the previous behaviour

As `role, resource, action` rows (the policy shape in `src/main/resources/model.conf`). Whether `ADMIN` should also be granted everything is a decision for the enforcer, because Endeavour Security's model lived in Casdoor's database and is not in this repository.

```
CREATOR, ENTITY, CREATE
EDITOR, ENTITY, UPDATE
EDITOR, DOCUMENT, WRITE
EDITOR, DOCUMENT, READ
CREATOR, FOLDER, CREATE
EDITOR, FOLDER, UPDATE
ADMIN, DELTA, READ
ADMIN, GITHUB, UPDATE
EXECUTOR, QUERY, EXECUTE
PUBLISHER, SET, PUBLISH
EDITOR, SET, UPDATE
TASK_MANAGER, USER, READ
DEVELOPER, USER, READ
ADMIN, USER, UPDATE
DEVELOPER, BUG_REPORT, UPDATE
DEVELOPER, TASK, READ
TASK_MANAGER, TASK, READ
DEVELOPER, TASK, DELETE
TASK_MANAGER, TASK, DELETE
TASK_MANAGER, TASK, UPDATE
TASK_MANAGER, ROLE_REQUEST, READ
TASK_MANAGER, ROLE_REQUEST, UPDATE
TASK_MANAGER, ROLE_REQUEST, REJECT
APPROVER, ROLE_REQUEST, APPROVE
TASK_MANAGER, NAMESPACE_REQUEST, READ
TASK_MANAGER, NAMESPACE_REQUEST, UPDATE
APPROVER, NAMESPACE_REQUEST, APPROVE
APPROVER, NAMESPACE_REQUEST, REJECT
EDITOR, ENTITY_APPROVAL, CREATE
CREATOR, ENTITY_APPROVAL, CREATE
TASK_MANAGER, ENTITY_APPROVAL, READ
TASK_MANAGER, ENTITY_APPROVAL, UPDATE
APPROVER, ENTITY_APPROVAL, APPROVE
APPROVER, ENTITY_APPROVAL, REJECT
UPRN, UPRN, EXECUTE
```

## User preferences

`POST api/user/private/*` saves theme, colours, dark mode, font size, favourites and recent activity into the Casdoor user's `properties` (as strings, the same encoding Endeavour Security used). Roles and namespaces are never writable through these endpoints. An administrator changes another user's namespaces through `POST api/user/private/namespaces`, and approving a namespace request grants the requested access to the user who made the request.
