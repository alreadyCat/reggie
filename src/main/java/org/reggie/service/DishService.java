package org.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.reggie.dto.DishDto;
import org.reggie.pojo.Dish;

public interface DishService extends IService<Dish> {

     void saveDishWithFlavor(DishDto dishDto);
      DishDto getByIdWithFlavor(Long id);

     void updateWithFlavor(DishDto dishDto);
}
