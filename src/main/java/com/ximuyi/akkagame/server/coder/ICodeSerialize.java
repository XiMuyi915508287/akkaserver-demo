package com.ximuyi.akkagame.server.coder;

import com.google.protobuf.MessageLite;

public interface ICodeSerialize<T extends MessageLite> {

    T serialize();
}
