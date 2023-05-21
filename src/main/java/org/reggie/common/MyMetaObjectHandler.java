package org.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentUserId = BaseContext.getCurrentUserId();
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", currentUserId);
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("createUser", currentUserId);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentUserId = BaseContext.getCurrentUserId();
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", currentUserId);
    }
}
