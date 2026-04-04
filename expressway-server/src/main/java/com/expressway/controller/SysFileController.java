package com.expressway.controller;

import com.expressway.entity.SysFile;
import com.expressway.exception.FileException;
import com.expressway.result.Result;
import com.expressway.service.SysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件管理控制器
 */
@RestController
@RequestMapping("/sys/file")
@Api(tags = "文件管理接口")
public class SysFileController {

    @Resource
    private SysFileService sysFileService;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件信息
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<SysFile> upload(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            SysFile sysFile = sysFileService.upload(file);
            return Result.success(sysFile);
        } catch (FileException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 批量上传文件
     *
     * @param files        文件数组
     * @return 文件信息列表
     */
    @PostMapping("/uploadBatch")
    @ApiOperation("批量上传文件")
    public Result<List<SysFile>> uploadBatch(
            @RequestParam("files") MultipartFile[] files
           ) {
        try {
            List<SysFile> sysFiles = sysFileService.uploadBatch(files);
            return Result.success(sysFiles);
        } catch (FileException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("批量上传文件失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询文件信息
     *
     * @param id 文件ID
     * @return 文件信息
     */
    @GetMapping("/detail/{id}")
    @ApiOperation("查询文件信息")
    public Result<SysFile> getById(@PathVariable Long id) {
        try {
            SysFile sysFile = sysFileService.getById(id);
            return Result.success(sysFile);
        } catch (FileException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询文件失败：" + e.getMessage());
        }
    }

    /**
     * 查询当前用户上传的文件
     *
     * @return 文件列表
     */
    @GetMapping("/all")
    @ApiOperation("查询我上传的文件")
    public Result<List<SysFile>> getFiles() {
        try {
            List<SysFile> files = sysFileService.getFiles();
            return Result.success(files);
        } catch (FileException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询文件失败：" + e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除文件")
    public Result<?> delete(@PathVariable Long id) {
        try {
            sysFileService.deleteById(id);
            return Result.success("删除成功");
        } catch (FileException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("删除文件失败：" + e.getMessage());
        }
    }
}
