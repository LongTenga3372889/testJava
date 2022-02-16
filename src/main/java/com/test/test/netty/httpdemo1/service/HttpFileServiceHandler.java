package com.test.test.netty.httpdemo1.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class HttpFileServiceHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    public HttpFileServiceHandler(String uri) {

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.getDecoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        if (request.getMethod() != HttpMethod.GET) {
            sendError(ctx,HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        final String uri = request.getUri();
        final String path = sanitizeUri(uri);
        if (path ==null) {
            sendError(ctx,HttpResponseStatus.FORBIDDEN);
            return;
        }
        File file = new File(path);
        if (file.isHidden() && !file.exists()) {
            sendError(ctx,HttpResponseStatus.NOT_FOUND);
            return ;
        }
        if (!file.isFile()) {
            sendError(ctx,HttpResponseStatus.NOT_FOUND);
            return ;
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (Exception e) {
            sendError(ctx,HttpResponseStatus.NOT_FOUND);
            return;
        }
        long fileLength = randomAccessFile.length();
        HttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        httpResponse.headers().set("Content-Length",fileLength);
    }

    private void sendError(ChannelHandlerContext ctx, Object status) {
        ctx.writeAndFlush(status);
    }

    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri,"ISO-8859-1");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new Error();
            }
        }
        return uri;
    }
}
