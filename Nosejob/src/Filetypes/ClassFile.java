package Filetypes;

import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import sun.rmi.rmic.iiop.ClassPathLoader;
import sun.tools.java.ClassPath;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/* This class represents a single Class file.
 */

public class ClassFile implements Reflectable {
    private String path;
    private File cfile;

    public ClassFile(String path) {
        this.path = path;
    }

    @Override
    // Instantiates the file specified in path.
    public void open() throws IOException {
        this.cfile = new File(this.path);
        if(!cfile.exists()) {
            throw new IOException("No such file or directory");
        }
    }

    @Override
    public String getName() {
        return this.path;
    }

    @Override
    public File openFile() {
    	try {
    		open();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
        return this.cfile;
    }

    @Override
    public Class getJavaClass() throws IOException, ClassNotFoundException {
        // Open class file and get its name
        open();
        URL url;
        int p = path.lastIndexOf('/');
        int p2 = path.substring(0,p-1).lastIndexOf('/');

        File f = new File(path.substring(0,p2));
        url = f.toURI().toURL();
        URL[] urls = new URL[1];
        urls[0] = url;
        ClassLoader loader = new URLClassLoader(urls);

        String cls = path.substring(p2 + 1, path.lastIndexOf('.')).replace('/', '.');

        if (cfile.getParentFile().getParentFile().getName().equals("production")) {
            cls = cls.substring(cls.lastIndexOf('.') + 1);
        }



        return loader.loadClass(cls);
    }
}
