package week03.gateway.filter.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import week03.gateway.filter.HttpRequestFilter;

public class HttpRequestFilterImpl implements HttpRequestFilter {
    private String name;

    public HttpRequestFilterImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().add("nio", name);
    }
}
