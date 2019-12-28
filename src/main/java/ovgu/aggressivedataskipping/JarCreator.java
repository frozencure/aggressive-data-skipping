package ovgu.aggressivedataskipping;

import org.apache.commons.compress.utils.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class JarCreator {

    private String packagePath;
    private String jarPath;


    public JarCreator(String packagePath, String jarPath) {
        this.packagePath = packagePath;
        this.jarPath = jarPath;
    }

    public void createJar() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(jarPath);
            JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream);
            jarOutputStream.putNextEntry(new ZipEntry(packagePath));
//            Enumeration<URL> classes = ClassLoader.getSystemResources(packagePath);
            jarOutputStream.write(IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream(packagePath)));
            jarOutputStream.closeEntry();
            jarOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
