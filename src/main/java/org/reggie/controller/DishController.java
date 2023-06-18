package org.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.reggie.common.Res;
import org.reggie.dto.DishDto;
import org.reggie.pojo.Category;
import org.reggie.pojo.Dish;
import org.reggie.service.CategoryService;
import org.reggie.service.DishFlavorService;
import org.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/page")
    public Res<Page<DishDto>> page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<DishDto>();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, dishLambdaQueryWrapper);

        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<Dish> dishList = dishPage.getRecords();
        List<DishDto> dishDtoList = dishList.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);

        return Res.success(dishDtoPage);
    }

    @PostMapping
    public Res<String> insert(@RequestBody DishDto dishDto) {
        dishService.saveDishWithFlavor(dishDto);
        return Res.success("添加成功");
    }

    @GetMapping("/{id}")
    public Res<DishDto> getDishById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Res.success(dishDto);
    }

    @PutMapping
    public Res update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);

        return Res.success("修改成功");
    }

    @GetMapping("/list")
    public Res<List<Dish>> getDishList(Dish dish){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!= null,Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);

        return Res.success(dishList);
    }
}
