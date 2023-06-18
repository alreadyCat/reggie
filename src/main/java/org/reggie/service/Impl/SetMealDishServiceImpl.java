package org.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.reggie.mapper.SetMealDishMapper;
import org.reggie.pojo.SetmealDish;
import org.reggie.service.SetMealDishService;
import org.reggie.service.SetmealService;
import org.springframework.stereotype.Service;


@Service
@Slf4j

public class SetMealDishServiceImpl extends ServiceImpl<SetMealDishMapper, SetmealDish> implements SetMealDishService {

}
