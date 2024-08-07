package services.serviceRegister;

import javax.jmdns.JmDNS;
import java.io.IOException;

public class ServiceRegister {
    private JmDNS jmdns;

    public void registerService(String serviceName) {
        try {
            jmdns = JmDNS.create(java.net.InetAddress.getLocalHost());

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
