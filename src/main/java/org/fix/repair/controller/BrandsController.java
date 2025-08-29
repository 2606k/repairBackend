package org.fix.repair.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.fix.repair.common.R;
import org.fix.repair.entity.brands;
import org.fix.repair.mapper.BrandsMapper;
import org.fix.repair.service.BrandsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandsController {

    private final BrandsService brandsService;
    private final BrandsMapper brandsMapper;

    public BrandsController(BrandsService brandsService, BrandsMapper brandsMapper) {
        this.brandsService = brandsService;
        this.brandsMapper = brandsMapper;
    }

    @RequestMapping("/add")
    public R<String> addBrand(brands brands) {
            return brandsService.save(brands) ? R.ok("添加成功") : R.error("添加失败");
    }

    @RequestMapping("/list")
    public R<List<brands>> listBrand(@RequestParam("pageNum") Integer pageNum,
                                     @RequestParam("pageSize") Integer pageSize,Long categoryId) {
        // 创建分页对象
        Page<brands> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<brands> queryWrapper = new LambdaQueryWrapper<>();

        if (categoryId != null) {
            queryWrapper.eq(brands::getCategoryId, categoryId);
        }

        // 添加排序（可选）
        queryWrapper.orderByDesc(brands::getCreatedat);
        List<brands> list = brandsMapper.selectPage(page, queryWrapper).getRecords();
        return R.ok(list);
    }

    @RequestMapping("/delete")
    public R<String> deleteBrand(Long id) {
        return brandsService.removeById(id) ? R.ok("删除成功") : R.error("删除失败");
    }
}
