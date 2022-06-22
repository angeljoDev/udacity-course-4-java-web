package com.example.demo.logging;

import com.splunk.*;

public class SplunkLogger {
    private static final String SPLUNK_HOST = "localhost";
    private static final int SPLUNK_PORT = 8089;
    private static final String SPLUNK_TOKEN = "a4830feb-af0e-4732-aa55-007ad55af472";
    private static final String HEC_URL = "http://localhost:8088/services/collector/event";
    private static final String USER = "omega";
    private static final String PASSWORD = "0megaprime";

    private final Service splunkService;

    public SplunkLogger() {
        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        ServiceArgs loginArgs = new ServiceArgs();
        loginArgs.setUsername(USER);
        loginArgs.setPassword(PASSWORD);
        loginArgs.setHost(SPLUNK_HOST);
        loginArgs.setPort(SPLUNK_PORT);
        loginArgs.setToken(SPLUNK_TOKEN);
        splunkService = Service.connect(loginArgs);
    }

    public void log(String message) {
        try {
            Receiver receiver = splunkService.getReceiver();
            Args logArgs = new Args();
            logArgs.put("sourcetype", "log4j");
            receiver.log("spring_boot", logArgs, message);
        } catch (HttpException e) {
            System.err.println("Error sending log to Splunk: " + e.getMessage() + e.getCause() + e.getDetail());
        }
    }
}