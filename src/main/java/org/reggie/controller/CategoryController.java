package org.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.reggie.common.BaseContext;
import org.reggie.common.Res;
import org.reggie.pojo.Category;
import org.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Res<String> insert(HttpServletRequest request, @RequestBody Category category) {
        log.info("添加分类{}", category);

        Long userId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentUserId(userId);
        categoryService.save(category);
        return Res.success("添加成功！");
    }

    @GetMapping("/page")
    public Res<Page> page(int page, int pageSize) {
        Page<Category> pageCon = new Page<>(page, pageSize);

        LambdaQueryWrapper<Category> categoryQueryWrapper = new LambdaQueryWrapper<>();
        categoryQueryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageCon, categoryQueryWrapper);

        return Res.success(pageCon);
    }

    @DeleteMapping
    public Res<String> deleteById(Long ids){
        categoryService.removeById(ids);
        return Res.success("删除成功");
    }
}
