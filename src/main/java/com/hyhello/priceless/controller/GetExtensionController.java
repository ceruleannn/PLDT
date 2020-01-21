package com.hyhello.priceless.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.hyhello.priceless.service.AccessLogService;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/getex")
public class GetExtensionController {

    /**
     * only for startup using [java -jar] command
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<byte[]> getex(HttpServletResponse response) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(bos);

        ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        Resource[] resources = loader.getResources("classpath*:extension/**");

        String basename = "";
        for (Resource resource : resources) {
            String url = resource.getURL().toString();
            if (url.endsWith("/BOOT-INF/classes!/extension/")){
                basename = url;
            }
            if (resource.contentLength() > 0){
                String name = resource.getURL().toString().substring(basename.length());
                addFileToZip(resource.getInputStream(), zip, name);
            }
        }
        zip.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "extension.zip");
        return new ResponseEntity<byte[]>(bos.toByteArray(), headers, HttpStatus.CREATED);

    }

    private void addFileToZip(@NotNull InputStream in, @NotNull ZipOutputStream zip, String name) throws IOException {
        ZipEntry zipEntry = new ZipEntry(name);
        zip.putNextEntry(zipEntry);
        IOUtils.copy(in, zip);
        zip.closeEntry();
    }
}
