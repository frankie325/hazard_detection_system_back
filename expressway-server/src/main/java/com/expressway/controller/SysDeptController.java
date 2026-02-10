package com.expressway.controller;

import com.expressway.dto.DeptAddDTO;
import com.expressway.dto.DeptQueryParamsDTO;
import com.expressway.dto.DeptUpdateDTO;
import com.expressway.entity.SysDept;
import com.expressway.exception.DeptException;
import com.expressway.result.Result;
import com.expressway.service.SysDeptService;
import com.expressway.vo.DeptTreeVO;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 * 处理部门相关HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    /**
     * 新增部门
     *
     * @param deptAddDTO 新增部门参数（已做参数校验）
     * @return 统一响应结果
     */
    @PostMapping("/add")
    public Result<?> addDept(@Validated @RequestBody DeptAddDTO deptAddDTO) {
        try {
            sysDeptService.addDept(deptAddDTO);
            return Result.success("新增部门成功");
        } catch (DeptException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("新增部门失败：" + e.getMessage());
        }
    }

    /**
     * 编辑部门
     *
     * @param deptUpdateDTO 编辑部门参数（已做参数校验）
     * @return 统一响应结果
     */
    @PutMapping("/update")
    public Result<?> updateDept(@Validated @RequestBody DeptUpdateDTO deptUpdateDTO) {
        try {
            sysDeptService.updateDept(deptUpdateDTO);
            return Result.success("编辑部门成功");
        } catch (DeptException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("编辑部门失败：" + e.getMessage());
        }
    }

    /**
     * 单个删除部门
     *
     * @param id 部门ID（路径参数）
     * @return 统一响应结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteDeptById(@PathVariable("id") Long id) {
        try {
            sysDeptService.deleteDeptById(id);
            return Result.success("删除部门成功");
        } catch (DeptException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("删除部门失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除部门
     *
     * @param ids 部门ID列表（请求体）
     * @return 统一响应结果
     */
    @DeleteMapping("/batchDelete")
    public Result<?> batchDeleteDept(@RequestBody List<Long> ids) {
        try {
            sysDeptService.batchDeleteDept(ids);
            return Result.success("批量删除部门成功");
        } catch (DeptException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("批量删除部门失败：" + e.getMessage());
        }
    }

    /**
     * 查询部门列表（平级）
     *
     * @param queryParams 部门名称、部门编码（可选，模糊查询）
     * @return 部门列表数据
     */
    @PostMapping("/list")
    public Result<List<SysDept>> getAllDeptList(@RequestBody(required = false) DeptQueryParamsDTO queryParams) {
        try {
            // 如果不传参数，创建空对象
            if (queryParams == null) {
                queryParams = new DeptQueryParamsDTO();
            }
            List<SysDept> deptList = sysDeptService.getAllDeptList(queryParams);
            return Result.success(deptList);
        } catch (Exception e) {
            return Result.error("查询部门列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询单个部门（用于编辑回显）
     *
     * @param id 部门ID（路径参数）
     * @return 单个部门详情
     */
    @GetMapping("/{id}")
    public Result<SysDept> getDeptById(@PathVariable("id") Long id) {
        try {
            SysDept dept = sysDeptService.getDeptById(id);
            return Result.success(dept);
        } catch (DeptException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询部门详情失败：" + e.getMessage());
        }
    }

    /**
     * 获取部门树形结构
     *
     * @param queryParams 部门查询参数DTO，包含过滤条件
     * @return 包含部门树形结构的Result对象
     * @throws Exception 当查询失败时抛出异常
     */
    @PostMapping("/tree")
    public Result<List<SysDept>> getDeptTree(@RequestBody(required = false) DeptQueryParamsDTO queryParams) {
        try {
            // 如果不传参数，创建空对象
            if (queryParams == null) {
                queryParams = new DeptQueryParamsDTO();
            }
            List<SysDept> deptTree = sysDeptService.getDeptTree(queryParams);
            return Result.success(deptTree);
        } catch (Exception e) {
            return Result.error("查询部门树形结构失败：" + e.getMessage());
        }
    }
}