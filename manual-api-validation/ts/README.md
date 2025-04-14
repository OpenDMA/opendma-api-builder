# Manual validation of generated TypeScript API

Validation steps:
1. In `../../src.generated/ts` install dependencies with `npm install` and then compile the library with `tsc`
2. In the same folder, run `npm link` to make this local library available
3. In `./opendma-sample` run `npm link @opendma/api` and then `tsc`
4. In the same folder, run `node ./dist/index.js`