package com.mdc.mim.thrift;

import javax.annotation.PostConstruct;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import lombok.Data;

@Data
public abstract class CommonRpcService {

    public static int DEFAULT_PORT = 28080;

    private TServer server;
    private int port;

    public CommonRpcService() {
        this(DEFAULT_PORT);
    }

    public CommonRpcService(int port) {
        this.port = port;
    }

    protected abstract TProcessor getProcessor();

    @PostConstruct
    public void start() {
        var thread = new Thread(() -> {
            try {
                var serverTransport = new TServerSocket(port);
                TProcessor processor = getProcessor();
                var serverArgs = new Args(serverTransport).processor(processor);
                server = new TSimpleServer(serverArgs);
                System.out.println("[Thrift][Server]Starting the server on port " + port);
                server.serve();
            } catch (TTransportException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void close() {
        if (server != null && server.isServing()) {
            System.out.println("Stopping the server...");
            server.stop();
        }
    }

    public boolean isServing() {
        return server != null && server.isServing();
    }
}
