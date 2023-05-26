package org.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import org.reggie.common.HasOtherAssociateRecordsException;
import org.reggie.mapper.CategoryMapper;
import org.reggie.pojo.Category;
import org.reggie.pojo.Dish;
import org.reggie.pojo.Setmeal;
import org.reggie.service.CategoryService;
import org.reggie.service.DishService;
import org.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {
  @Autowired private DishService dishService;
  @Autowired private SetmealService setmealService;

  @Override
  public boolean removeById(Serializable id) {
    // 查询是否有关联菜品
    LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
    dishQueryWrapper.eq(Dish::getCategoryId, id);
    int dishCount = dishService.count(dishQueryWrapper);
    if (dishCount > 0) {
      throw new HasOtherAssociateRecordsException("请先删除相关联的菜品");
    }

    // 查询是否有关联他套餐
    LambdaQueryWrapper<Setmeal> setMealQueryWrapper = new LambdaQueryWrapper<>();
    setMealQueryWrapper.eq(Setmeal::getCategoryId, id);
    int setmealCount = setmealService.count(setMealQueryWrapper);
    if (setmealCount > 0) {
      throw new HasOtherAssociateRecordsException("请先删除相关联的套餐");
    }

    return super.removeById(id);
  }
}
