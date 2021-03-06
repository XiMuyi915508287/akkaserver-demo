package com.ximuyi.game.core.scene.notify;

import com.google.protobuf.MessageLite;

/**
 * MessageLite 最坑爹：无多态、Inmutable
 * @param <T>
 */
abstract class NotifyCache<T extends MessageLite> {

    protected abstract T serialize(int index);
}
