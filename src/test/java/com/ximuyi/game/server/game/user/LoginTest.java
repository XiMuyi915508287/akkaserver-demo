package com.ximuyi.game.server.game.user;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.MessageLite;
import com.ximuyi.core.api.login.ConnectWay;
import com.ximuyi.core.command.CommandUtil;
import com.ximuyi.core.command.ICommand;
import com.ximuyi.game.common.util.RandomUtil;
import com.ximuyi.game.server.proto.ProtoCommon;
import com.ximuyi.game.server.proto.ProtoUser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

public class LoginTest {

    @Test
    public void test0() throws IOException, InterruptedException {

        final int count = 20;
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(new Client(i + 1, i));
            TimeUnit.SECONDS.sleep(RandomUtil.nextInt(3) + 1);
            thread.start();
        }
        TimeUnit.HOURS.sleep(1);
    }

    private static class Client implements Runnable{
        static final String host = "127.0.0.1";  //要连接的服务端IP地址
        static final int port = 5000;   //要连接的服务端对应的监听端口

        private final int userId;
        private final int index;

        public Client(int userId, int index) {
            this.userId = userId;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                Socket client = new Socket(host, port);
                TimeUnit.SECONDS.sleep(2);

                //建立连接后就可以往服务端写数据了
                BufferedOutputStream writer = new BufferedOutputStream(client.getOutputStream());
                BufferedInputStream reader = new BufferedInputStream(client.getInputStream());

                long current = 0;
                while (client.isConnected()){
                    MessageLite request = null;
                    ICommand command;
                    if (current == 0) {
                        ProtoUser.LoginRequest.Builder builder = ProtoUser.LoginRequest.newBuilder();
                        int connectWay = RandomUtil.nextInt(ConnectWay.values().length);
                        request = builder.setUserId(userId).setPassword("000000").setConnectWay(connectWay).build();
                        command = CommandUtil.LOGIN;
                    }
                    else if (current < 5){
                        request = ProtoCommon.ProEmpty.newBuilder().build();
                        command = CommandUtil.HEART_BEAT;
                    }
                    else if (current < 10){
                        request = ProtoCommon.ProLong.newBuilder().setValue(0L).build();
                        command = CommandUtil.CACHE_RESPONSE;
                    }
                    else {
                        request = ProtoCommon.ProEmpty.newBuilder().build();
                        command = CommandUtil.LOGOUT;
                    }
                    writer.write(getBytes(command, request));
                    writer.flush();//写完后要记得flush
                    TimeUnit.SECONDS.sleep(RandomUtil.nextInt(5) + 5);
                    current++;
                }
            }
            catch (Throwable t){
                t.printStackTrace();
            }
            System.out.println("线程退出 :" + userId);
        }

        private byte[] getBytes(ICommand command, MessageLite obj){
            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
            byteBuf.writeInt(0);
            command.write(byteBuf);
            byteBuf.writeBytes(obj.toByteArray());
            byteBuf.setInt(0, byteBuf.readableBytes() - 4);
            final byte[] array = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(array);
            return array;
        }
    }
}
