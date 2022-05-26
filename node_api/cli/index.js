// const { TextGenerator } = require("../model/text");
const { TextGenerator } = require("../src/model/text");


function summarise(clause) {
  // console.log("Input Clause \n", clause);
  // let summary = TextGenerator.summarise(JSON.parse(clause))
  console.log("test name");
  // console.log(summary);
}

const printHelp = () => {
  const txt = `
  Text Generator CLI supported commands
  
  - summarise: returns a string for a Match/Filter clause JSON string
  
  -------------------------------------------------------------------------
  Invoke as:
  
  $ node cli summarise "json_object_here"
  -------------------------------------------------------------------------

  You should escape quotes in your JSON object using a backtick and backslack i.e. " becomes \`\\"
    `;

  console.log(txt);
  return;
};

async function startCli() {
  const args = process.argv;

  if (args.length > 2 && args[2] === "summarise") {
    await summarise(args[3]);
  } else {
    printHelp();
    process.exit(1);
  }

  process.exit(0);
}

startCli();
