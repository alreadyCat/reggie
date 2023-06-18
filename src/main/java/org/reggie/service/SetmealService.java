package org.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.reggie.dto.SetmealDto;
import org.reggie.pojo.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDishes(SetmealDto setmealDto);

    SetmealDto getByIdWithDishes(Long id);

    void updateWithDishes(SetmealDto setmealDto);
}
