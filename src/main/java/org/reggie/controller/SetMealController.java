package org.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.reggie.common.HasOtherAssociateRecordsException;
import org.reggie.common.Res;
import org.reggie.dto.SetmealDto;
import org.reggie.pojo.Setmeal;
import org.reggie.pojo.SetmealDish;
import org.reggie.service.CategoryService;
import org.reggie.service.SetMealDishService;
import org.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j

public class SetMealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetMealDishService setMealDishService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Res<Page<SetmealDto>> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, queryWrapper);

        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");

        List<SetmealDto> setmealDtoList = setmealPage.getRecords().stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            setmealDto.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName());
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtoList);

        return Res.success(setmealDtoPage);
    }

    @PostMapping
    public Res insert(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDishes(setmealDto);
        return Res.success("添加成功");
    }

    @GetMapping("/{id}")
    public Res<SetmealDto> getById(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDishes(id);

        return Res.success(setmealDto);
    }

    @PutMapping
    public Res update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDishes(setmealDto);
        return Res.success("更新成功");
    }

    @DeleteMapping
    @Transactional
    public Res delete(@RequestParam List<String> ids){
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);

        if(setmealService.count(setmealLambdaQueryWrapper) > 0){
            throw new HasOtherAssociateRecordsException("存在部分套餐正在售卖中，不可删除");
        }

        setmealService.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setMealDishService.remove(setmealDishLambdaQueryWrapper);


        return Res.success("删除成功");
    }
}
