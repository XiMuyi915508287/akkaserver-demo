package com.ximuyi.akkagame.server.game.user;

import com.google.protobuf.MessageLite;
import com.ximuyi.akkagame.server.proto.ProtoTest;
import com.ximuyi.akkagame.server.proto.ProtoUser;
import com.ximuyi.akkaserver.extension.Command;
import com.ximuyi.akkaserver.extension.CommandConst;
import com.ximuyi.akkaserver.extension.ICommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class LoginTest {

    @Test
    public void test0() throws IOException, InterruptedException {

        final int count = 4;
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(new Client(1, i));
            if (i > (count / 2)){
                TimeUnit.SECONDS.sleep(1);
            }
            thread.start();
        }
        System.in.read();
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
                TimeUnit.SECONDS.sleep(60);

                //建立连接后就可以往服务端写数据了
                BufferedOutputStream writer = new BufferedOutputStream(client.getOutputStream());
                BufferedInputStream reader = new BufferedInputStream(client.getInputStream());
                ProtoUser.LoginRequest.Builder builder = ProtoUser.LoginRequest.newBuilder();
                ProtoUser.LoginRequest request = builder.setUserId(userId).setPassword("000000").build();
                writer.write(getBytes(CommandConst.LOGIN, request));
                writer.flush();//写完后要记得flush

                TimeUnit.SECONDS.sleep(5);
                long current = 0;
                while (client.isConnected()){
                    byte[] bytes;
                    if (current++ == 5){
                        ProtoUser.LogoutRequest request1 = ProtoUser.LogoutRequest.newBuilder().build();
                        bytes = getBytes(CommandConst.LOGOUT, request1);
                    }
                    else {
                        ProtoTest.TestRequest request1 = ProtoTest.TestRequest.newBuilder().build();
                        bytes = getBytes(new Command((short) 1, (short) 1), request1);
                    }
                    writer.write(bytes);
                    writer.flush();
                    TimeUnit.SECONDS.sleep(1);
                }
            }
            catch (Throwable t){
                t.printStackTrace();
            }
            System.out.println("线程退出 :" + index);
        }

        private byte[] getBytes(ICommand command, MessageLite obj){
            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
            byteBuf.writeInt(0);
            command.wirte(byteBuf);
            byteBuf.writeBytes(obj.toByteArray());
            byteBuf.setInt(0, byteBuf.readableBytes() - 4);
            final byte[] array = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(array);
            return array;
        }
    }
}
