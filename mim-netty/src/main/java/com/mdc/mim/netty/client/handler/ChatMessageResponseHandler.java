package com.mdc.mim.netty.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ChatMessageResponseHandler extends ChannelOutboundHandlerAdapter {

}
