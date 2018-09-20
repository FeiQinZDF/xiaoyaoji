package cn.xiaoyaoji.api.extension.file;

import cn.xiaoyaoji.service.common.FileType;
import cn.xiaoyaoji.service.integration.file.FileManager;
import cn.xiaoyaoji.service.util.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件上传
 *
 * @author: zhoujingjie
 * @Date: 16/5/2
 */
public class FileUtils {
    private Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static MetaData upload(MultipartFile file) throws IOException {
        MetaData md = new MetaData();
        if (file.getContentType() != null && file.getContentType().startsWith("image")) {
            md.setType(FileType.IMG);
        } else {
            md.setType(FileType.FILE);
        }
        String path = path(getExtension(file.getOriginalFilename()));
        md.setPath(FileManager.getFileProvider().upload(path, IOUtils.toByteArray(file.getInputStream())));
        return md;
    }

    private static String getExtension(String filename) {
        String ext = FilenameUtils.getExtension(filename);
        if (ext == null) {
            ext = "";
        } else {
            ext = "." + ext;
        }
        return ext;
    }

    public static String path(String ext) {
        if (ext == null) {
            ext = "";
        }
        return new SimpleDateFormat("yyyyMM/dd/").format(new Date()).concat(StringUtils.uuid() + ext);
    }


    /**
     * 删除文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static void delete(String path) throws IOException {
        if (path == null) {
            return;
        }
        FileManager.getFileProvider().delete(path);
    }
}
