package org.fix.repair.controller;

import org.fix.repair.common.R;
import org.fix.repair.entity.categories;
import org.fix.repair.mapper.CategoriesMapper;
import org.fix.repair.service.CategoriesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoriesController {

    private CategoriesService categoriesService;
    private CategoriesMapper categoriesMapper;
    public CategoriesController(CategoriesService categoriesService, CategoriesMapper categoriesMapper){
        this.categoriesService = categoriesService;
        this.categoriesMapper = categoriesMapper;
    }

    @RequestMapping("/add")
    public R<String> addCategory(categories categories){
        categories.setCreatedat(new java.util.Date());
        boolean save = categoriesService.save(categories);
        if (save){
            return R.ok("添加成功");
        }
        return R.error("添加失败");
    }

    @RequestMapping("/list")
    public R<List<categories>> listCategory(){
        List<categories> list = categoriesService.list();
        return R.ok(list);
    }

    @RequestMapping("/delete")
    public R<String> deleteCategory(Long id){
        boolean remove = categoriesService.removeById(id);
        if (remove){
            return R.ok("删除成功");
        }
        return R.error("删除失败");
    }

}
