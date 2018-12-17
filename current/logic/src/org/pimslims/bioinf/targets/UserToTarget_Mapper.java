package org.pimslims.bioinf.targets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserToTarget_Mapper {

    /**
     * @author Petr Troshin aka pvt43
     * 
     * Class to map output from "grep -B 3 'user_name>' *.xml" in the Targets directory to a HashMap TargetID =>
     * user_name check ID and modify user names to fit YSBL user names e.g. If ID contains 'David' user name =
     * David Waterman If ID = 'EdPLATE..' user name = Ed Taylor If ID = 'YANDREA' user name = Andrea Rawlings
     * If ID = ITQB -delete If ID = OX.. delete unless it contains 'David'
     * 
     * @param bufferedReader created from file to parse
     * @throws IOException
     */

    public static Map convertToMap(final BufferedReader bufferedReader) throws IOException {
        final Map convertedToMap = new java.util.HashMap();// hash for ID to User
        // map
        String strLine;

        String id = "";
        String userName = "";
        int numMatched = 0;

        while ((strLine = bufferedReader.readLine()) != null) {
            // System.out.println(strLine);

            // need to process the lines.
            // Need preferredID = key, user_name = value
            final Pattern regex1 = Pattern.compile("<preferredID>");
            final Matcher match1 = regex1.matcher(strLine);
            final Pattern regex2 = Pattern.compile("<user_name>");
            final Matcher match2 = regex2.matcher(strLine);
            if (match1.find()) {
                numMatched++;
                id = strLine.replaceAll("TARGET-.*.xml-    <preferredID>", "");
                // System.out.println("Matched " +id);
                id = id.replaceAll("</preferredID>", "").trim();
                // System.out.println("id is " +id);
                if (id.matches("OX.*David.*")) {
                    // System.out.println("Found David");
                    userName = "Mr David Waterman";
                }
                if (id.matches("Ed.*")) {
                    // System.out.println("Found Ed");
                    userName = "Dr Ed Taylor";
                }
                if (id.matches("YANDREA.*")) {
                    // System.out.println("Found Andrea");
                    userName = "Miss Andrea Rawlings";
                }
                if (id.matches("OX[A-Z]\\d")) {
                    // System.out.println("Found an Oxford construct");
                    id = "";
                    userName = "";
                }
                if ((id.matches("ITQB\\d+")) | (id.matches("YTFRAN"))) {
                    // System.out.println("Found a Portuguese construct");
                    id = "";
                    userName = "";
                }
                if (id.matches("Olga")) {
                    // System.out.println("Found a Portuguese construct");
                    userName = "Dr Olga Moroz";
                }
            }
            if (match2.find()) {
                userName = strLine.replaceAll("TARGET-.*.xml:    <user_name>", "");
                // System.out.println("Matched " +userName);
                userName = userName.replaceAll("</user_name>", "").trim();
                if ((userName.matches("Keith Wilson")) || (userName.matches("Mark Fogg"))) {
                    // System.out.println("Found Keith!");
                    userName = "Dr Mark Fogg";
                }
                if (userName.matches("Tony Wilkinson")) {
                    // System.out.println("Found Keith!");
                    userName = "Professor Tony Wilkinson";
                    // System.out.println(" User name is " +userName);
                }
                if (userName.matches("Gillian Wade")) {
                    userName = "Miss Gillian Wade";
                    // System.out.println(" User name is " +userName);
                }
                if (userName.matches("Rosa Grenha")) {
                    userName = "Miss Rosa Grenha";
                    // System.out.println(" User name is " +userName);
                }
                if (userName.matches("Axel Muller")) {
                    userName = "Mr Axel Muller";
                    // System.out.println(" User name is " +userName);
                }
                if (userName.matches("Elena Blagova")) {
                    userName = "Dr Elena Blagova";
                    // System.out.println(" User name is " +userName);
                }
                if (userName.matches("Jim Brannigan")) {
                    userName = "Dr Jim Brannigan";
                    // System.out.println(" User name is " +userName);
                }

                // convertedToMap.put(id, userName);
            }
            convertedToMap.put(id, userName);
        }
        System.out.println("Total matches in file is " + numMatched);
        return convertedToMap;

    }

    public static void main(final String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: ");
            System.out.println("UserToTarget_Map inputFile");
            System.exit(0);
        }
        try {
            final String inputFile = args[0];
            final FileInputStream fstream = new FileInputStream(inputFile);
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // String strLine;
            Map idToUser = new java.util.HashMap(); // hash for ID to User map
            idToUser = UserToTarget_Mapper.convertToMap(br);

            final int numTargets = idToUser.size();
            System.out.println("number of targets is " + numTargets);
            // Need to check we have 265 different key-value pairs
            final Iterator keys = idToUser.keySet().iterator();
            while (keys.hasNext()) {
                final Object currentKey = keys.next();
                System.out.println("ID is " + currentKey.toString() + " USER is "
                    + idToUser.get(currentKey).toString());
            }
            in.close();
        }

        catch (final FileNotFoundException e) {
            System.err.println("Could not find input file");
            System.exit(1);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
