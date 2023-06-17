package org.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.reggie.dto.DishDto;
import org.reggie.mapper.DishMapper;
import org.reggie.pojo.Dish;
import org.reggie.pojo.DishFlavor;
import org.reggie.service.DishFlavorService;
import org.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;



    // 新增菜品，同时新增口味
    @Transactional
    public void saveDishWithFlavor(DishDto dishDto){
        this.save(dishDto);

        Long dishDtoId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors().stream().map(item -> {
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());


        dishFlavorService.saveBatch(flavors);
    }

    //
    public DishDto getByIdWithFlavor(Long id){

        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> dishDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishDtoLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishDtoLambdaQueryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors().stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }
}
