package fish.focus.uvms.spatial.client;

import java.io.File;
import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

@ArquillianSuiteDeployment
public abstract class BuildSpatialRestClientDeployment {

    @Deployment(name = "spatial")
    public static Archive<?> createDeployment() {
        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "spatial.war");

        File[] files = Maven.configureResolver().loadPomFromFile("pom.xml")
                .resolve("fish.focus.uvms.spatial:spatial-module:jar:classes:?")
                .withTransitivity().asFile();

        testWar.addAsLibraries(files);

        testWar.addPackages(true, "fish.focus.uvms.spatial.client");
        testWar.addAsResource("beans.xml", "META-INF/beans.xml");

        return testWar;
    }
}
