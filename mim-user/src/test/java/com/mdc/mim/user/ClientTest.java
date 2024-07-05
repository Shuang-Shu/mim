package com.mdc.mim.user;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mdc.mim.thrift.CommonRpcService;

@SpringBootTest
public class ClientTest {
    public static void main(String[] args) {

        try (var transport = new TSocket("localhost", CommonRpcService.DEFAULT_PORT)) {
            transport.open();
            var client = new UnifyService.Client(new TBinaryProtocol(transport));
            var user = client.FindUidByUserName(new FindUidReq().setUserName("双竖"));
            System.out.println(user);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    UnifyService.Client client;

    @BeforeEach
    public void init() {
        try (var transport = new TSocket("localhost", CommonRpcService.DEFAULT_PORT)) {
            transport.open();
            client = new UnifyService.Client(new TBinaryProtocol(transport));
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHello() {
        System.out.println(client);
    }

    @Test
    public void testGetUser() throws TException {
        // TODO: 单测会发生报错，main函数正常，原因待排查
        System.out.println("try calling rpc");
        var user = client.FindUidByUserName(new FindUidReq().setUserName("双竖"));
        System.out.println(user);
    }
}
