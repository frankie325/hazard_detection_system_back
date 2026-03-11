package com.expressway.controller;

import com.expressway.result.Result;
import com.expressway.service.SysFileService;
import com.expressway.vo.SysFileVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件管理控制器
 * 处理文件上传、下载、删除等操作
 */
@RestController
@RequestMapping("/sys/file")
public class SysFileController {

    @Resource
    private SysFileService sysFileService;

    /**
     * 上传文件
     * 
     * @param file 上传的文件
     * @return 文件信息
     */
    @PostMapping("/upload")
    public Result<SysFileVO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            SysFileVO fileVO = sysFileService.uploadFile(file);
            return Result.success(fileVO);
        } catch (Exception e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件详情
     * 
     * @param id 文件ID
     * @return 文件信息
     */
    @GetMapping("/{id}")
    public Result<SysFileVO> getFileById(@PathVariable("id") Long id) {
        try {
            SysFileVO fileVO = sysFileService.getFileById(id);
            if (fileVO == null) {
                return Result.error("文件不存在");
            }
            return Result.success(fileVO);
        } catch (Exception e) {
            return Result.error("获取文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户上传的文件列表
     * 
     * @return 文件列表
     */
    @GetMapping("/myFiles")
    public Result<List<SysFileVO>> getMyFiles() {
        try {
            List<SysFileVO> files = sysFileService.getMyFiles();
            return Result.success(files);
        } catch (Exception e) {
            return Result.error("获取文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     * 
     * @param id 文件ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteFile(@PathVariable("id") Long id) {
        try {
            sysFileService.deleteFile(id);
            return Result.success("文件删除成功");
        } catch (Exception e) {
            return Result.error("文件删除失败: " + e.getMessage());
        }
    }
}
