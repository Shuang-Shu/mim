package com.mdc.mim.netty.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ChatMessageResponseHandler extends ChannelOutboundHandlerAdapter {

}
