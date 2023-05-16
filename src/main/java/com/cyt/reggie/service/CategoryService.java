package com.cyt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyt.reggie.entity.Category;

/**
 * @author cyt
 * @version 1.0
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
