package Filetypes;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Vector;

// This class represents the object that will search the specified
// directory tree for all .java and .class files for analysis.
// Objects of this class should be used when multiple files are to
// be tested for code smells.

// This class relies on the use of Java's fileVisitor interface for
// traversing directory trees.

public class Directory implements Openable, FileVisitor<Path> {
    private boolean ignoreHidden;
    private Path dir;
    private Vector<String> javaFileNames = new Vector<>();
    private Vector<String> classFileNames = new Vector<>();

    public Directory(boolean ignoreHidden, String path) {
        this.ignoreHidden = ignoreHidden;
        this.dir = Paths.get(path);
    }

    @Override
    public void open() throws IOException {
        Files.walkFileTree(this.dir, this);
    }

    @Override
    public String getName() {
        return this.dir.toString();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        File f = dir.toFile();

        if(!f.exists())
            throw new IOException(f.getAbsolutePath() + ": No such file or directory");

        if(f.isHidden() && ignoreHidden)
            return FileVisitResult.SKIP_SUBTREE;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String filename = file.toAbsolutePath().toString();

        if(filename.endsWith(".java"))
            javaFileNames.add(filename);
        else if(filename.endsWith(".class"))
            classFileNames.add(filename);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        throw new IOException("Failed to visit file: " + file.toAbsolutePath().toString());
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        boolean searchFinished = Files.isSameFile(dir, this.dir);
        if(searchFinished)
            return FileVisitResult.TERMINATE;
        return FileVisitResult.CONTINUE;
    }

    public Vector<String> getJavaFileNames() {
        return this.javaFileNames;
    }

    public Vector<String> getClassFileNames() {
        return this.classFileNames;
    }
}

