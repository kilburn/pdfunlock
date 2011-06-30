/*
 * Software License Agreement (BSD License)
 * 
 * Copyright (c) 2011, Marc Pujol <kilburn at la3 dot org>
 * All rights reserved.
 * 
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * 
 *   Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 * 
 *   Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 * 
 *   Neither the name of la3.org 
 *   nor the names of its contributors may be used to
 *   endorse or promote products derived from this
 *   software without specific prior written permission of
 *   Marc Pujol.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.la3.pdfunlock;

import com.itextpdf.text.pdf.PdfEncryptor;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.io.FileOutputStream;

/**
 * Small utility to unlock pdf files.
 * 
 * @author Marc Pujol <kilburn at la3 dot org>
 */
public class PdfUnlock {
    
    private static String programName = "pdfunlock";
    
    private String lockedFile = "";
    private String unlockedFile = "";
    private String password = null;
    
    public static void showUsage() {
        System.err.println("Usage: " + programName + " <input.pdf> [output.pdf]");
        System.exit(0);
    }
    
    public static void showLongUsage() {
        System.err.println("Usage: " + programName + "[options] <input.pdf> [output.pdf]");
        System.err.println("Unlocks the <input.pdf> file, storing the unlocked file as [output.pdf].");
        System.err.println("Options:");
        System.err.println("  -h, --help");
        System.err.println("    Displays this help message.");
        System.err.println("  -p <password>, --password=<password>");
        System.err.println("    Uses <password> to decrypt the original pdf.");
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        PdfUnlock pdfUnlock = new PdfUnlock();
        pdfUnlock.launch(args);
    }
    
    private void parseOptions(String[] args) {
        // Long options
        LongOpt[] longopts = new LongOpt[] {
            new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
        };
        Getopt g = new Getopt(programName, args, "h", longopts);
        
        int c = 0;
        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 'h':
                    showLongUsage();
                    break;
                case 'p':
                    password = g.getOptarg();
                    break;
            }
        }
        
        c = g.getOptind();
        if (c >= args.length) {
            showUsage();
        }

        if (c < args.length) {
            lockedFile = args[c++];
        }

        if (c < args.length) {
            unlockedFile = args[c++];
        } else {
            unlockedFile = lockedFile.substring(0, lockedFile.lastIndexOf('.'))
                    + "_unlocked"
                    + lockedFile.substring(lockedFile.lastIndexOf('.'));
        }
    }

    private void launch(String[] args) throws Exception {
        parseOptions(args);
        
        System.err.println("Unlocking " + lockedFile + " to " + unlockedFile);
        
        PdfReader reader;
        if (password == null) {
            reader = new PdfReader(lockedFile);
        } else {
            reader = new PdfReader(lockedFile, password.getBytes());
        }
        PdfEncryptor.encrypt(reader, new FileOutputStream(unlockedFile), null,
                null, PdfWriter.ALLOW_ASSEMBLY | PdfWriter.ALLOW_COPY
                | PdfWriter.ALLOW_DEGRADED_PRINTING | PdfWriter.ALLOW_FILL_IN
                | PdfWriter.ALLOW_MODIFY_ANNOTATIONS | PdfWriter.ALLOW_MODIFY_CONTENTS
                | PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_SCREENREADERS, false);
               
        System.err.println("PDF Unlocked successfully!");
        System.exit(0);
    }
    
    
}
