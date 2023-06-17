package org.reggie.dto;


import lombok.Data;
import org.reggie.pojo.Dish;
import org.reggie.pojo.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
