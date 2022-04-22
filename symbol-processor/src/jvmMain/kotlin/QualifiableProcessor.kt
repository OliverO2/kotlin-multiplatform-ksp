import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import my.annotations.Qualifiable
import java.io.OutputStreamWriter


class QualifiableProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger
    private var isInitialInvocation = true

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val fileNames = resolver.getAllFiles().map { it.fileName }.toList()
        logger.warn("${javaClass.simpleName}: isInitialInvocation=$isInitialInvocation, processing $fileNames")

        if (!isInitialInvocation) {
            // A subsequent invocation is for processing generated files. We do not need to process these.
            return emptyList()
        }

        isInitialInvocation = false

        codeGenerator.createNewFile(
            // TODO: How to support incremental compilation with dedicated dependencies if an annotation can be
            //       added to any source file, not just the ones previously processed?
            dependencies = Dependencies.ALL_FILES,
            packageName = "my.generated",
            fileName = "Qualifiables",
            extensionName = "kt"
        ).use { output ->
            // TODO: This hack to discover the output source set should be replaced with a better solution.
            val outputSourceSet = codeGenerator.generatedFile.first().toString().sourceSetBelow("ksp")
            logger.warn("processing output source set '$outputSourceSet'")

            OutputStreamWriter(output).use { writer ->
                writer.write(
                    listOfNotNull(
                        "// Code generated for source set '$outputSourceSet'",
                        if (outputSourceSet == "jvmMain") """@file:JvmName("QualifiablesJvmKt")""" else null,
                        """@file:Suppress("unused")""",
                        "package my.generated",
                    ).joinToString("\n", postfix = "\n")
                )

                resolver
                    .getSymbolsWithAnnotation(Qualifiable::class.qualifiedName!!)
                    .filterIsInstance<KSClassDeclaration>()
                    .forEach { classDeclaration ->
                        // TODO: This hack to discover the input source set should be replaced with a better solution.
                        val inputSourceSet = classDeclaration.containingFile?.filePath?.sourceSetBelow("src") ?: {
                            logger.error(
                                "Could not determine the source file for class '${classDeclaration.qualifiedName()}'"
                            )
                            "unknown"
                        }

                        logger.warn("at '${classDeclaration.qualifiedName()}' in input source set '$inputSourceSet'")

                        if (inputSourceSet == outputSourceSet) {
                            val qualifiedClassName = classDeclaration.qualifiedName()
                            writer.write("val $qualifiedClassName.qualifiedClassName get() = \"$qualifiedClassName\"\n")
                        }
                    }
            }
        }

        return emptyList()  // no deferred symbols to resolve in the next round
    }

    private fun String.sourceSetBelow(startDirectoryName: String): String =
        substringAfter("/$startDirectoryName/").substringBefore("/kotlin/").substringAfterLast('/')

    private fun KSClassDeclaration.qualifiedName() = (qualifiedName ?: simpleName).asString()
}


class QualifiableProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = QualifiableProcessor(environment)
}
