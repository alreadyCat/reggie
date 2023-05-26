package org.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.reggie.mapper.CategoryMapper;
import org.reggie.mapper.SetMealMapper;
import org.reggie.pojo.Category;
import org.reggie.pojo.Setmeal;
import org.reggie.service.CategoryService;
import org.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SelMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetmealService {
}
