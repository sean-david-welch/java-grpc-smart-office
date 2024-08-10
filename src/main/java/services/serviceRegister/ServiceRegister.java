package services.serviceRegister;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;

public class ServiceRegister {
    private static final System.Logger logger = System.getLogger(ServiceRegister.class.getName());
    private JmDNS jmdns;

    public void registerService(String serviceType, String serviceName, int port) {
        try {
            jmdns = JmDNS.create(InetAddress.getLocalHost());
            ServiceInfo serviceInfo = ServiceInfo.create(serviceType, serviceName, port, serviceName);
            jmdns.registerService(serviceInfo);
            logger.log(System.Logger.Level.INFO, "Service registered: {0}", serviceName);
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Failed to register service: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void unregisterService() {
        if (jmdns != null) {
            jmdns.unregisterAllServices();
            logger.log(System.Logger.Level.INFO, "All services unregistered.");
        }
    }
}
