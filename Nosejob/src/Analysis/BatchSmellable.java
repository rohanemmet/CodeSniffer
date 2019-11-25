package Analysis;

import Filetypes.Openable;

import java.util.Vector;

/* This is a generic interface to allow the user to insert a vector of any
   Openable objects to their respective tests. To enable this, declare a class as

   Class class implements BatchSmellable<TextReadable>

   or

   Class class implements BatchSmellable<Reflectable>
 */

public interface BatchSmellable<T extends Openable> extends Smellable {
    public void addFiles(Vector<T> files);
    public void addFile(T file);
}
