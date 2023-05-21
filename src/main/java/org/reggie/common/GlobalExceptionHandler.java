package org.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, ControllerAdvice.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Res<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {

        if (ex.getMessage().contains("Duplicate entry")) {
            String[] arr = ex.getMessage().split(" ");
            String msg = arr[2] + "已存在，请重新添加";
            return Res.error(msg);
        }
        return Res.error("未知的请求异常");
    }
}
