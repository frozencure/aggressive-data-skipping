package ovgu.aggressivedataskipping.livy;

import org.apache.commons.compress.utils.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class JarCreator {

    private String classPath;
    private String jarPath;


    public String getJarPath() {
        return jarPath;
    }

    public JarCreator(String classPath, String jarPath) {
        this.classPath = classPath;
        this.jarPath = jarPath;
    }

    public void createJar() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(jarPath);
            JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream);
            jarOutputStream.putNextEntry(new ZipEntry(classPath));
            jarOutputStream.write(IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream(classPath)));
            jarOutputStream.closeEntry();
            jarOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
