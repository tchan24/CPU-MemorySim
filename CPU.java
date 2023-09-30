import java.util.*;
import java.lang.*;
import java.io.*;

public class CPU 
{
    static int  PC = 0,
                SP = 1000,
                IR = 0,
                AC = 0,
                X = 0,
                Y = 0;
    static int  userTimer,
                instr = 0,
                userStack = 1000,
                sysStack = 2000;
    static boolean kernel = true;
    static boolean interrupt = false;

    static Scanner scMem;
    static OutputStream osMem;
    static InputStream isMem;
    static PrintWriter pwMem;

    public static void main(String []args) 
    {

    }
}
