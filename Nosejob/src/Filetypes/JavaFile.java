package Filetypes;

import java.io.*;

// This class represents a single Java file

public class JavaFile implements TextReadable {
    private String path;
    private File jfile;

    public JavaFile(String path) {
        this.path = path;
    }

    @Override
    // Instantiates the file specified in path.
    public void open() throws IOException {
        File f = new File(this.path);
        this.jfile = new File(f.getCanonicalPath());
        if(!jfile.exists())
            throw new IOException("No such file or directory");
    }

    @Override
    public String getName() {
        return this.path;
    }

    @Override
    public BufferedReader readFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(jfile));
    }

    @Override
    public File openFile() {
        return this.jfile;
    }
}
