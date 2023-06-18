package org.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.reggie.dto.SetmealDto;
import org.reggie.mapper.SetMealMapper;
import org.reggie.pojo.Setmeal;
import org.reggie.pojo.SetmealDish;
import org.reggie.service.CategoryService;
import org.reggie.service.SetMealDishService;
import org.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SelMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;

    @Transactional
    public void saveWithDishes(SetmealDto setmealDto) {
        this.save(setmealDto);


        setmealDto.setCategoryName(categoryService.getById(setmealDto.getCategoryId()).getName());
        List<SetmealDish> dishList = setmealDto.getSetmealDishes().stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());

        setMealDishService.saveBatch(dishList);
    }

    public SetmealDto getByIdWithDishes(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmealService.getById(id), setmealDto);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> dishes = setMealDishService.list(setmealDishLambdaQueryWrapper);

        setmealDto.setSetmealDishes(dishes);

        return setmealDto;
    }



    @Transactional

    public void updateWithDishes(SetmealDto setmealDto) {
        setmealService.updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setMealDishService.remove(setmealDishLambdaQueryWrapper);

        List<SetmealDish> dishList = setmealDto.getSetmealDishes().stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());

        setMealDishService.saveBatch(dishList);
    }
}
