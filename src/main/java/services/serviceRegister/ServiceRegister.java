package services.serviceRegister;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;

public class ServiceRegister {
    private JmDNS jmdns;

    public void registerService(String serviceType, String serviceName, int port) {
        try {
            jmdns = JmDNS.create(InetAddress.getLocalHost());
            ServiceInfo serviceInfo = ServiceInfo.create(serviceType, serviceName, port, "path=index.html");
            jmdns.registerService(serviceInfo);
            System.out.println("Service registered: " + serviceName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unregisterService() {
        if (jmdns != null) {
            jmdns.unregisterAllServices();
        }
    }
}
