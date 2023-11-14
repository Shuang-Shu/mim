package com.mdc.mim.netty.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ChatMessageRedirectHandler extends ChannelInboundHandlerAdapter {

}
