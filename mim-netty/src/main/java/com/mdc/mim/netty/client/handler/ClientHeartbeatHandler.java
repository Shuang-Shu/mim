package com.mdc.mim.netty.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ClientHeartbeatHandler extends ChannelInboundHandlerAdapter {

}
