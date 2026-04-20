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
1. Bump patch version of VueLibrary in package.lock (e.g. `v0.0.0 -> v0.0.1`)
1. Repackage VueLibrary (`.tgz`) with the changes (run `pnpm pack`)
1. Commit & push the VueLibrary changes
1. Download the new VueLibrary package to your UI project by upgrading the dependency version (e.g. `v0.0.0 -> v0.0.1`)

1. Alternatively for local development skip stage 3 and perform commands
   `pnpm add     "vue-library": "https://raw.githubusercontent.com/endeavourhealth-discovery/VueLibrary/main/vue-library-0.0.0.tgz",`
   in your UI project.