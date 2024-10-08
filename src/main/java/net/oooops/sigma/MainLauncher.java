package net.oooops.sigma;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import io.vertx.core.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import net.oooops.sigma.core.Banner;

public class MainLauncher extends Launcher {

  public static final Logger LOG = LoggerFactory.getLogger(MainLauncher.class);

  public static void main(String[] args) {
    // String currentDir = System.getProperty("user.dir");
    // String classPath = MainLauncher.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    // System.out.println(classPath);
    // System.out.println(currentDir);
    // try {
    //   ClassLoader cl = Thread.currentThread().getContextClassLoader();
    //   URL resource = cl.getResource("server.yml");
    //   if (resource == null) {
    //     return;
    //   }
    //   LoaderOptions loadingConfig = new LoaderOptions();
    //   SafeConstructor constructor = new Constructor(loadingConfig);
    //   PropertyUtils propertyUtils = new PropertyUtils();
    //   propertyUtils.setSkipMissingProperties(true); // Skip properties that are missing during deserialization of YAML to a Java object. The default is false.
    //   constructor.setPropertyUtils(propertyUtils);
    //   Yaml yaml = new Yaml(constructor);
    //   sigmaOptions = yaml.loadAs(resource.openStream(), SigmaOptions.class);
    // } catch (IOException e) {
    //   throw new RuntimeException(e);
    // }
    // MDC.put("pid", STR."\{ProcessHandle.current().pid()}");
    Banner.create().printBanner();
    new MainLauncher().dispatch(args);
  }

  @Override
  public void beforeStartingVertx(VertxOptions options) {
    LOG.debug(STR."VertxOptions is : \{options.toJson().toString()}");
  }

  @Override
  public void afterStartingVertx(Vertx vertx) {
    if (vertx.isNativeTransportEnabled()) {
      LOG.debug("Vert.x is run with native transports on BSD (OSX) and Linux.");
    } else {
      LOG.debug("Vert.x Can't run with native transports.", vertx.unavailableNativeTransportCause());
    }
  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    deploymentOptions.setThreadingModel(ThreadingModel.VIRTUAL_THREAD);
    LOG.debug(STR."DeploymentOptions : \{deploymentOptions.toJson().toString()}");
  }
}
