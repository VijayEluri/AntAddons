package org.datech.antaddons.diff;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;


/**
 * Diff task
 * @author Teodor Ivanov
 * @since 2010-12-27
 */
public class DiffTask extends Task {

    private String lineSparator;
    private String fromFile;
    private String toFile;
    private String outputFile;
    private boolean verbose = false;


    /** The fromFile. As attribute. */    
    public void setFrom(String fromFile) {
        this.fromFile = fromFile;
    }
	
    /** The toFile. As attribute. */    
    public void setTo(String toFile) {
        this.toFile = toFile;
    }
	
    /** The outputFile. As attribute. */    
    public void setOutput(String outputFile) {
        this.outputFile = outputFile;
    }

    /** The verbose. As attribute. */
    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }

    /** Do the work. */
    public void execute() {

        lineSparator = System.getProperty("line.separator");

        if (fromFile == null)
            throw new BuildException("You must supply a FROM file to read from.");

        if (!new File(fromFile).exists())
            throw new BuildException("You must supply existing FROM file.");

        if (toFile == null)
            throw new BuildException("You must supply a TO file to read from.");

        if (!new File(toFile).exists())
            throw new BuildException("You must supply existing TO file.");

        if (outputFile == null)
            throw new BuildException("You must supply an output file to write to.");

        log( "Diff files: " + fromFile + " and " + toFile );

        /* Delete output file */
        new File(outputFile).delete();

	FileDiff(fromFile, toFile);
		
    }
	

    public void FileDiff(String fromFile, String toFile)
    {
        String[]         aLines = read(fromFile);
        String[]         bLines = read(toFile);
        List<Difference> diffs  = (new Diff<String>(aLines, bLines)).diff();
        
        for (Difference diff : diffs) {
            int        delStart = diff.getDeletedStart();
            int        delEnd   = diff.getDeletedEnd();
            int        addStart = diff.getAddedStart();
            int        addEnd   = diff.getAddedEnd();
            String     from     = toString(delStart, delEnd);
            String     to       = toString(addStart, addEnd);
            String     type     = delEnd != Difference.NONE && addEnd != Difference.NONE ? "c" : (delEnd == Difference.NONE ? "a" : "d");

            write(outputFile, from + type + to + lineSparator);

            if (delEnd != Difference.NONE) {
                printLines(delStart, delEnd, "<", aLines);
                if (addEnd != Difference.NONE) {
                    write(outputFile, "---" + lineSparator);
                }
            }
            if (addEnd != Difference.NONE) {
                printLines(addStart, addEnd, ">", bLines);
            }
        }
    }

    protected void printLines(int start, int end, String ind, String[] lines)
    {
        String  linesBuff = "";
	
        for (int lnum = start; lnum <= end; ++lnum) {
            linesBuff = linesBuff + ind + " " + lines[lnum] + lineSparator;
        }

        write(outputFile, linesBuff);
    }

    protected String toString(int start, int end)
    {
        // adjusted, because file lines are one-indexed, not zero.
        StringBuffer buf = new StringBuffer();

        // match the line numbering from diff(1):
        buf.append(end == Difference.NONE ? start : (1 + start));
        
        if (end != Difference.NONE && start != end) {
            buf.append(",").append(1 + end);
        }
        return buf.toString();
    }

    protected String[] read(String fileName)
    {
        try {
            BufferedReader br       = new BufferedReader(new FileReader(fileName));
            List<String>   contents = new ArrayList<String>();
            
            String in;
            while ((in = br.readLine()) != null) {
                contents.add(in);
            }

            br.close();
            return (String[])contents.toArray(new String[] {});
        }
        catch (Exception e) {
            throw new BuildException("Error reading " + fileName + ": " + e);
        }        
    }

    protected void write(String fileName, String content)
    {
        if ( verbose ){
            log(content);
        }
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));            
            bw.write(content);
            bw.close();
        }
        catch (Exception e) {
            throw new BuildException("Error writing " + fileName + ": " + e);
        }        
    }	

}