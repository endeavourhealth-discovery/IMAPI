tasks.register("extractEnumsFromAutoGen") {
  group = "other"
  description = "Changes TypeScript generated const enums to standard enums and adds imports"
  dependsOn("typescriptConstEnumToEnum")

  val inputFile = File(rootDir, "../VueLibrary/src/interfaces/AutoGen.ts")
  val outputFile = File(rootDir, "../VueLibrary/src/enums/AutoGen.ts")

  doLast {
    if (!inputFile.exists()) {
      throw GradleException("AutoGen.ts not found: $inputFile")
    }

    val content = inputFile.readText()

    // Regex to match enums
    val enumRegex = Regex(
      pattern = """(?ms)^\s*export\s+enum\s+(\w+)\s*\{.*?^\s*\}"""
    )

    val matches = enumRegex.findAll(content).toList()

    if (matches.isEmpty()) {
      println("No enums found.")
      return@doLast
    }

    // Extract enum names
    val enumNames = matches.map { it.groups[1]!!.value }

    // Write extracted enums to output file
    outputFile.writeText(
      "/* Auto-extracted enums */\n\n" +
        matches.joinToString("\n") { it.value }
    )

    // Remove exported enums from original content
    var updatedContent = content.replace(enumRegex) { "" }

    // Add import statements for extracted enums at the top
    val importLine = "import { ${enumNames.joinToString(", ")} } from '../enums/AutoGen';\n\n"
    updatedContent = importLine + updatedContent.trimStart()

    // Write back updated original file
    inputFile.writeText(updatedContent)

    println("Extracted ${matches.size} enums to ${outputFile.name} and added imports in original file")
  }
}