package com.pd.pdp.server.base;

import com.pd.pdp.server.dto.PageDTO;

import java.util.List;

/**
 * @Author pdp
 * @Description Service 层 基础接口，其他Service 接口 请继承该接口
 **/

public interface Service<T, E> {
    /**
     * 持久化
     * @param model
     */
    void save(T model);

    /**
     * 批量持久化
     * @param models
     */
    void save(List<T> models);

    /**
     * 通过主鍵刪除
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 批量刪除
     * @param ids
     */
    void deleteByIds(List<String> ids);

    /**
     * 更新
     * @param model
     */
    void update(T model);

    /**
     * 通过ID查找
     * @param id
     * @return
     */
    T findById(Integer id);

    /**
     * 通过多个ID查找//eg：ids -> “1,2,3,4”
     * @param ids
     * @return
     */
    List<T> findByIds(String ids);

    /**
     * 根据条件查找
     * @param pageVO
     * @return
     */
    PageDTO<T> findByCondition(E pageVO);

    /**
     * 获取所有
     * @return
     */
    List<T> findAll();
}
