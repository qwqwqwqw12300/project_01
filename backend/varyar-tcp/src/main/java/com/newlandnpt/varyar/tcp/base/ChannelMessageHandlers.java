package com.newlandnpt.varyar.tcp.base;

import com.newlandnpt.varyar.tcp.gateway.AbstractChannelMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lin.ju
 * @date 2023/2/22
 */
public class ChannelMessageHandlers {


    private static Map<String, AbstractChannelMessageHandler> handlers = new HashMap<>();

    public static Req constructFromMessage(String message){
        String[] arr = message.split(",");
        Req req = null;
        if(handlers.containsKey(arr[3])){
            AbstractChannelMessageHandler handler = handlers.get(arr[3]);
            req = handler.newInstance();
            req.setHead(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
            req.handleMessage(arr[7]);

            return req;
        }
        return null;
    }

    public static Response handleRequest(ChannelHandlerContext ctx, Req req){
       for(AbstractChannelMessageHandler handler:handlers.values()){
           if(req.getClass().equals(handler.newInstance().getClass())){
               return handler.handle(ctx, req);
           }
       }
       return null;
    }

    @Component
    public static class ChannelMessageHandlerRegister implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

            if(bean instanceof AbstractChannelMessageHandler){
                handlers.put(((AbstractChannelMessageHandler) bean).messageIdentify(),(AbstractChannelMessageHandler)bean);
            }

            return bean;
        }
    }

}
