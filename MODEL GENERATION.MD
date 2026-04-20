# Model Generation

In the latest version of the code, the Java -> Typescript process has changed to now go via the new Vue-Library shared
package.

Note: There is an assumption that you have the relevant Git repositories already checked out and all under the same
folder.

* IMAPI
* VueLibrary
* IMDirectory (or other relevant UI project such as IMQueryRunner)

Once you have made changes to your Java classes, to push them through into Typescript, the following steps should be
performed.

1. Build IMAPI (or run the `IMAPI -> Tasks -> other -> extractEnumsFromAutoGen` gradle task)
1. Check that the changes have been correctly generated in the VueLibrary code (`src/interfaces/AutoGen.ts` &
   `src/enums/AutoGen.ts`)
1. Commit & push the VueLibrary changes
1. Download the new VueLibrary package to your UI project automatically from the package.json using `"vue-library": "github:endeavourhealth-discovery/VueLibrary",
` in the dependencies section. This will build the latest code from github on install.
