package buckpal;

import buckpal.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DependencyRuleTests {

    @Test
    void validateRegistrationContextArchitectures() {
        HexagonalArchitecture.boundedContext("buckpal.account")

                .withDomainLayer("domain")

                .withAdaptersLayer("adapter")
                .incoming("in.web")
                .outgoing("out.persistence")
                .and()

                .withApplicationLayer("application")
                .services("service")
                .incomingPorts("port.in")
                .outgoingPorts("port.out")
                .and()

//                .withConfiguration("configuration")
                .check(new ClassFileImporter()
                        .importPackages("buckpal.."));
    }

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
