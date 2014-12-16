package com;
import java.util.Iterator;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush("Welcome!");
		System.out.println("WELCOME");
		channels.add(ctx.channel());
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	System.out.println(msg);
    	for(Channel c : channels) {
    		c.write(msg);
    	}
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	for(Channel c : channels) {
    		c.flush();
    	}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}