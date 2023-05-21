package org.reggie.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId){
        threadLocal.set(userId);
    }
    public static Long getCurrentUserId(){
        return threadLocal.get();
    }
}
