package org.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.reggie.common.Res;
import org.reggie.pojo.Category;
import org.reggie.pojo.Dish;
import org.reggie.service.CategoryService;
import org.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
  @Autowired private DishService dishService;

  @GetMapping("/page")
  public Res<Page> page(int page, int pageSize, String name) {
    Page<Dish> dishPage = new Page<>(page, pageSize);
    LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
    dishLambdaQueryWrapper.like(!StringUtils.isEmpty(name), Dish::getName, name);
    dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
    dishService.page(dishPage, dishLambdaQueryWrapper);
    return Res.success(dishPage);
  }

  @PostMapping
  public Res<String> insert(@RequestBody Dish dish) {
    dishService.save(dish);
    return Res.success("添加成功");
  }
  @GetMapping("/{id}")
  public Res<Dish> getDishById(@PathVariable Long id ){
    Dish dish = dishService.getById(id);
    return  Res.success(dish);
  }
}
