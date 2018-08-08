package com.ximuyi.akkagame.server.coder;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.ximuyi.akkagame.server.proto.ProtoCommon;
import com.ximuyi.akkaserver.coder.MessageCoder;
import com.ximuyi.akkaserver.extension.ICommand;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class MyMessageCoder extends MessageCoder<MessageLite> {

    private Map<Integer, MessageLite> messages;


    public MyMessageCoder() {
        super(MyMessageCoder.class.getName());
        this.messages = new HashMap<>();
    }

    @Override
    public byte[] _encode(ICommand command, MessageLite obj) {
        return obj.toByteArray();
    }

    @Override
    public MessageLite _decode(ICommand command, ByteBuf buffer) throws InvalidProtocolBufferException {
        MessageLite message = messages.get(command.getUniqueId());
        if (message == null){
            return null;
        }
        final byte[] array = new byte[buffer.readableBytes()];
        buffer.readBytes(array);
        return message.getParserForType().parseFrom(array);
    }

    @Override
    public MessageLite empty() {
        return ProtoCommon.ProEmpty.newBuilder().build();
    }

    @Override
    public MessageLite wrap(String string) {
        return ProtoCommon.ProString.newBuilder().setValue(string).build();
    }

    @Override
    public MessageLite wrap(int value) {
        return ProtoCommon.ProInteger.newBuilder().setValue(value).build();
    }

    public void register(ICommand extCmd, MessageLite messageLite){
        messages.put(extCmd.getUniqueId(), messageLite);
    }
}
