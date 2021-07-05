package buckpal;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DependencyRuleTests {

    @Test
    void domainLayerDoesNotDependOnApplicationLayer() {
        noClasses()
                .that()
                .resideInAPackage("buckpal.account.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("buckpal.account.application..")
                .check(new ClassFileImporter()
                        .importPackages("buckpal.account.."));
    }

}
