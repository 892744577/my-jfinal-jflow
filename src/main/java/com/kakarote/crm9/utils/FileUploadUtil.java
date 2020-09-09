package com.kakarote.crm9.utils;

import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 文件上传工具类，上传文件将默认存放到jfinal的默认路径（详看jfinal-cos），然后使用奔雷将文件转移到真正的业务路径下
 */
@Slf4j
public class FileUploadUtil {

    public static String FILE_SEPERATOR = "/";
    public static String COMMA = ".";
    public static String RELATIVE_PATH = "relativePath";//图片保存的相对路径
    public static String ACCESS_PATH = "accessPath";//图片访问路径，用于前台图片回显

    /**
     * 获取文件扩展名
     *
     * @param file 文件
     * @return
     */
    public static String getFileExtention(UploadFile file) {
        String extension = null;
        if (file != null) {
            String fileName = file.getOriginalFileName();
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return extension;
    }

    /**
     * 上传单个文件,重载
     * */
    public static Map<String, String> upload(UploadFile file, String path, String prefix) {
        String fileName = DateUtil.changeDateTOStr2(new Date())+System.currentTimeMillis() +
                ThreadLocalRandom.current().nextInt(100, 1000) + "";
        return upload(file,path,fileName,prefix,true);
    }
    public static Map<String, String> upload(UploadFile file, String path, String prefix,boolean isCover) {
        String fileName = DateUtil.changeDateTOStr2(new Date())+System.currentTimeMillis() +
                ThreadLocalRandom.current().nextInt(100, 1000) + "";
        return upload(file,path,fileName,prefix,isCover);
    }
    /**
     * 上传单个文件,重载
     * @param file 上传的文件
     * @param path 文件输出目录
     * @param fileName 文件名,不传则默认（时间1+时间2+3位随机数)
     * @param prefix 文件名前缀
     * @param isCover 是否覆盖,不传则默认true
     * @return
     */
    public static Map<String, String> upload(UploadFile file, String path, String fileName,String prefix,boolean isCover) {
        try {
            // 文件扩展名
            String suffix = getFileExtention(file);
            String newFileName = prefix + fileName + COMMA + suffix;
            String urlPath = path + newFileName;
            String fileDirectory = path;
            String filePath = urlPath;
            File dir = new File(fileDirectory);
            File destFile = new File(filePath);
            //若目录不存在，则创建目录
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
            }
            //若文件已存在，则覆盖
            if (isCover && destFile.exists() && destFile.isFile()) {
                destFile.delete();
            }
            //写入文件到真实路径文件
            transferTo(file.getFile(),new File(filePath));
            //返回图片保存的相对路径和图片回显路径
            Map<String, String> data = new HashMap<>();
            data.put(RELATIVE_PATH, urlPath);
            data.put(ACCESS_PATH, newFileName);
            return data;
        } catch (Exception e) {
            log.error("upload", e);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     *
     * @param sourceFile 上传的文件对象
     * @param destFile   写入的文件对象，对应服务器中真实路径/xx/xx的文件
     */
    public static void transferTo(File sourceFile,File destFile){
        try {
            // 构造临时对象
            InputStream is = new FileInputStream(sourceFile);;
            int buffer = 1024; // 定义缓冲区的大小
            int length = 0;
            byte[] b = new byte[buffer];
            FileOutputStream fos = new FileOutputStream(destFile);
            while ((length = is.read(b)) != -1) {
                // 计算上传文件的百分比
                fos.write(b, 0, length); // 向文件输出流写读取的数据
            }
            fos.close();
        } catch (Exception ex) {
            throw new RuntimeException("@文件存储失败,有可能是路径的表达式出问题,导致是非法的路径名称:" + ex.getMessage());
        }
    }

    /**
     * 多文件上传
     * @param fileList
     * @param path
     * @param prefix
     * @return
     */
    public static List<Map<String, String>> upload(List<UploadFile> fileList, String path, String prefix) {
        if (fileList == null || fileList.isEmpty()) {
            throw new RuntimeException("请上传文件");
        }
        List<Map<String, String>> list = new ArrayList<>();
        try {
            for (UploadFile file : fileList) {
                Map<String, String> data = upload(file, path, prefix);
                list.add(data);
            }
            return list;
        } catch (Exception e) {
            log.error("upload", e);
            throw new RuntimeException("上传文件失败");
        }
    }
}
