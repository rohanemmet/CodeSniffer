package Filetypes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

// This class encapsulates the list of files to be analysed, each
// stored as a vector of their respective types
public class FileList {
    private static FileList instanceCache;
    private Directory dir;
    private Vector<TextReadable> javaFiles;
    private Vector<Reflectable> classFiles;

    public static synchronized FileList instantiate() {
        if(instanceCache == null) {
            instanceCache = new FileList();
        }

        return instanceCache;
    }

    private FileList() {
        javaFiles = new Vector<>();
        classFiles = new Vector<>();
    }

    // Adds a directory to the file list for batch analysis.
    // The directory is checked first to see if it exists, then to see
    // if the given path corresponds to a directory.
    public void addDirectory (String dirpath, boolean showHidden) throws FileNotFoundException, IncorrectFileTypeException {
        File dir = new File(dirpath);
        if(!dir.exists()) {
            throw new FileNotFoundException("File: " + dirpath + " does not exist\n");
        } else if(!dir.isDirectory()) {
            throw new IncorrectFileTypeException("File: + " + dirpath + " is not a directory");
        } else {
            this.dir = new Directory(showHidden, dirpath);
        }
    }

    public void removeDirectory() {
        if(this.dir != null)
            this.dir = null;
    }

    // Constructs the file lists from the given directory
    // by issuing a command to the Directory object to
    // begin its search for all .java & .class files
    public void constructFromDir() throws IOException{
        if(dir == null) {
            System.out.println("Specify directory to analyse!");
            return;
        }

        dir.open();

        for(String s : this.dir.getJavaFileNames()) {
            javaFiles.add(new JavaFile(s));
        }

        for(String s : this.dir.getClassFileNames()) {
            classFiles.add(new ClassFile(s));
        }
    }

    // Adds a single file to either the java or class file lists.
    // First the given path is checked to see if that file exists.
    // Then it is checked to see if it its already in the file list
    public void addFile (String path) throws FileNotFoundException, IncorrectFileTypeException {
        File f = new File(path);
        if(!f.exists()) {
            throw new FileNotFoundException("File: " + path + " does not exist\n");
        } else if(!path.endsWith(".java") && !path.endsWith(".class")) {
            throw new IncorrectFileTypeException("File: " + path + " is neither a .java or .class file.");
        }

        if(path.endsWith(".java")) {
            for (TextReadable javaFile : javaFiles) {
                if (path.equals(javaFile.getName())) {
                    System.out.println("File: " + path + " is already in file list.");
                    return;
                }
            }
            javaFiles.add(new JavaFile(path));
        }

        else if(path.endsWith(".class")) {
            for (Reflectable classFile : classFiles) {
                if (path.equals(classFile.getName())) {
                    System.out.println("File: " + path + " is already in file list.");
                    return;
                }
            }
            classFiles.add(new ClassFile(path));
        }
    }

    public void removeFile(String path) {
        if(path.endsWith(".java")) {
            Vector<TextReadable> found = new Vector<>();
            for(TextReadable t : javaFiles) {
                if(t.getName().equals(path)) {
                    found.add(t);
                }
            }
            javaFiles.removeAll(found);
        }

        if(path.endsWith(".class")) {
            Vector<Reflectable> found = new Vector<>();
            for(Reflectable r : classFiles) {
                if(r.getName().equals(path)) {
                    found.add(r);
                }
            }
            classFiles.removeAll(found);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Java Files:\n");

        for(TextReadable t : javaFiles) {
            sb.append("\t").append(t.getName()).append("\n");
        }

        sb.append("Class Files:\n");

        for(Reflectable r : classFiles) {
            sb.append("\t").append(r.getName()).append("\n");
        }

        return sb.toString();
    }

    public Vector<TextReadable> getJavaFiles() {
        return this.javaFiles;
    }

    public Vector<Reflectable> getClassFiles() {
        return this.classFiles;
    }
}
