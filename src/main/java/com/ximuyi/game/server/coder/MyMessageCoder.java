package com.ximuyi.game.server.coder;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.ximuyi.core.coder.MessageCoder;
import com.ximuyi.core.command.ICommand;
import com.ximuyi.game.server.proto.ProtoCommon;
import io.netty.buffer.ByteBuf;

public class MyMessageCoder extends MessageCoder<MessageLite> {

    private Map<Integer, MessageLite> messages;


    public MyMessageCoder() {
        super(MyMessageCoder.class.getName());
        this.messages = new HashMap<>();
    }

    @Override
    public byte[] encode(ICommand command, MessageLite message) {
        return message.toByteArray();
    }

    @Override
    public MessageLite decode(ICommand command, ByteBuf buffer) throws InvalidProtocolBufferException {
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
    public MessageLite encodeString(String string) {
        return ProtoCommon.ProString.newBuilder().setValue(string).build();
    }

    @Override
    public MessageLite encodeInt(int value) {
        return ProtoCommon.ProInteger.newBuilder().setValue(value).build();
    }

    @Override
    public MessageLite encodeLong(long value) {
        return ProtoCommon.ProLong.newBuilder().setValue(value).build();
    }

    @Override
    public String byteString(ByteBuf buffer) throws InvalidProtocolBufferException {
        final byte[] array = new byte[buffer.readableBytes()];
        buffer.readBytes(array);
        return ProtoCommon.ProString.parseFrom(array).getValue();
    }

    @Override
    public int byteInt(ByteBuf buffer) throws InvalidProtocolBufferException {
        final byte[] array = new byte[buffer.readableBytes()];
        buffer.readBytes(array);
        return ProtoCommon.ProInteger.parseFrom(array).getValue();
    }

    @Override
    public long byteLong(ByteBuf buffer) throws InvalidProtocolBufferException {
        final byte[] array = new byte[buffer.readableBytes()];
        buffer.readBytes(array);
        return ProtoCommon.ProLong.parseFrom(array).getValue();
    }

    public void register(ICommand command, MessageLite messageLite){
        messages.put(command.getUniqueId(), messageLite);
    }
}
