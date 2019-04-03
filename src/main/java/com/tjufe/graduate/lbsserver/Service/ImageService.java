package com.tjufe.graduate.lbsserver.Service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Slf4j
@Service
public class ImageService {

    @Value("${absoluteImgPath}")
    String absoluteImgPath;

    @Value("${sonImgPath}")
    String sonImgPath;

    @Autowired
    RedissonClient redissonClient;

    public String getImage(String path) {
        return redissonClient.getBucket(path).get().toString();
    }

    public String saveImage(String base64, String path) {
        BASE64Decoder decoder = new BASE64Decoder();
        int comaIdx = base64.indexOf(",");
        int lineIdx = base64.indexOf("/");
        int endIdx = base64.indexOf(";");
        String fileSuffix = base64.substring(lineIdx + 1, endIdx);
        base64 = base64.substring(comaIdx + 1);
        path = path + "." + fileSuffix;
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(base64);
            File dest = new File(absoluteImgPath + path);
            OutputStream out = new FileOutputStream(dest);
            for(int i = 0; i < b.length; ++ i) {
                if (b[i] < 0) {
                    //调整异常数据
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception ex) {
            log.error("error occur when save image of {}, error: {}", path, ex.getMessage());
        }
        return path;
    }
}
