tasks.register("extractEnumsFromAutoGen") {
  group = "other"
  description = "Changes typescript generated const enums to standard enums for functionality"
  dependsOn("typescriptConstEnumToEnum")

  val inputFile = File(rootDir, "../VueLibrary/src/interfaces/AutoGen.ts")
  val outputFile = File(rootDir, "../VueLibrary/src/enums/AutoGen.ts")

  doLast {
    if (!inputFile.exists()) {
      throw GradleException("AutoGen.ts not found: $inputFile")
    }

    val content = inputFile.readText()

    val regex = Regex(
      pattern = """(?ms)^\s*export\s+enum\s+\w+\s*\{.*?^\s*\}""",
    )
    val matches = regex.findAll(content).map { it.value }.toList()

    if (matches.isEmpty()) {
      println("No enums found.")
      return@doLast
    }

    outputFile.writeText(
      "/* Auto-extracted enums */\n\n" +
        matches.joinToString("\n")
    )

    val updated = content.replace(
      Regex("""(?m)^\s*export\s+enum\s+"""),
      { matchResult ->
        matchResult.value.replace("export ", "")
      }
    )

    inputFile.writeText(updated)

    println("Extracted ${matches.size} enums to ${outputFile.name}")
  }
}