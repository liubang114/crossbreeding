package com.liubang.crossbreeding.util;

/**
 * 简单模式的客户端缓存。
 *
 * <p>设计目的：避免 common 类直接引用 {@code event.ClientEvents}
 * （后者 import 了客户端专用类 ClientPlayerNetworkEvent），
 * 从而防止专用服务端因类加载而崩溃。
 */
public final class SimpleModeCache {

    private SimpleModeCache() {
    }

    private static boolean clientSimpleMode = false;

    public static boolean isClientSimpleMode() {
        return clientSimpleMode;
    }

    public static void setClientSimpleMode(boolean value) {
        clientSimpleMode = value;
    }
}