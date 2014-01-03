package builtInVMCode;

import Hack.VMEmulator.TerminateVMProgramThrowable;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: wiggins
 * Date: 12/31/13
 * Time: 11:06 AM
 */
public class File extends JackOSClass
{
    private static ArrayList<FileHandle> fileHandles;
    private static class FileHandle {
        public short fh;
        public java.io.File file;
        public short writable;
        public LineNumberReader reader;
        public BufferedWriter writer;
    }

    public static void init() {
        fileHandles = new ArrayList<FileHandle>();
    }

    public static short open(short filename, short writable) throws TerminateVMProgramThrowable, FileNotFoundException, IOException
    {
        int filenameLength = callFunction("String.length", filename);
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < filenameLength; i++) {
            int c = callFunction("String.charAt", filename, i);
            sb.append((char)c);
        }

        FileHandle fh = new FileHandle();
        fh.fh = (short)fileHandles.size();
        fh.file = new java.io.File(sb.toString());
        fh.writable = writable;
        fileHandles.add(fh.fh, fh);

        if (fh.writable == 1) {
            fh.writer = new BufferedWriter(new FileWriter(fh.file));
        }
        else {
            fh.reader = new LineNumberReader(new FileReader(fh.file));
        }

        return fh.fh;
    }

    public static void close(short fd) throws TerminateVMProgramThrowable, IOException
    {
        FileHandle fh;
        try {
            fh = fileHandles.get(fd);
        } catch (IndexOutOfBoundsException e) {
            callFunction("Sys.error", FILE_UNKNOWN_FILEHANDLE);
            return;
        }
        if (fh.reader != null)
            fh.reader.close();
        if (fh.writer != null)
            fh.writer.close();
        fh.file = null;
        fh.reader = null;
        fh.writer = null;
    }

    public static void write(short fd, short str) throws TerminateVMProgramThrowable, IOException
    {
        FileHandle fh;
        try {
            fh = fileHandles.get(fd);
        } catch (IndexOutOfBoundsException e) {
            callFunction("Sys.error", FILE_UNKNOWN_FILEHANDLE);
            return;
        }
        if (fh.writable != 1) {
            callFunction("Sys.error", FILE_NOT_OPEN_FOR_WRITING);
        }

        int strlen = callFunction("String.length", str);
        char buffer[] = new char[strlen];
        for (int i=0; i < strlen; i++) {
            buffer[i] = (char)callFunction("String.charAt", str, i);
        }
        fh.writer.write(buffer, 0, strlen);
    }

    public static short read(short fd, short buffer, short count) throws TerminateVMProgramThrowable, IOException
    {
        FileHandle fh;
        try {
             fh = fileHandles.get(fd);
        } catch (IndexOutOfBoundsException e) {
            return callFunction("Sys.error", FILE_UNKNOWN_FILEHANDLE);
        }
        if (fh.writable == 1) {
            callFunction("Sys.error", FILE_NOT_OPEN_FOR_READING);
        }

        char readbuf[] = new char[count];
        int readCount = fh.reader.read(readbuf, 0, count);

        for (int i=0; i < readCount; i++) {
            writeMemory(buffer+2+i, readbuf[i]);
        }
        writeMemory(buffer+1, readCount);

        return (short)readCount;
    }

    public static short readln(short fd) throws TerminateVMProgramThrowable, IOException
    {
        FileHandle fh;
        try {
            fh = fileHandles.get(fd);
        } catch (IndexOutOfBoundsException e) {
            return callFunction("Sys.error", FILE_UNKNOWN_FILEHANDLE);
        }
        if (fh.writable == 1) {
            callFunction("Sys.error", FILE_NOT_OPEN_FOR_READING);
        }

        java.lang.String line = fh.reader.readLine();
        return javaStringToJackStringUsingVM(line);
    }

    public static short ready(short fd) throws TerminateVMProgramThrowable, IOException
    {
        FileHandle fh;
        try {
            fh = fileHandles.get(fd);
        } catch (IndexOutOfBoundsException e) {
            return callFunction("Sys.error", FILE_UNKNOWN_FILEHANDLE);
        }
        if (fh.writable == 1) {
            return 1;
        }
        return (short)(fh.reader.ready() ? 1 : 0);
    }

}
