/*
 * TextConverter.java
 *
 * Created on 8. Juni 2007, 09:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.exmaralda.partitureditor.jexmaralda.convert;

import org.exmaralda.partitureditor.jexmaralda.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
/**
 *
 * @author tommi
 *
 * Some TSV format for INEL. HEre's an email describing:
 * <blockquote>
 * more than one transcription tier.
 * I'd suggest an extra header line with a list of transcription tier names
 * if more than one. If that line is absent, the first column is taken as the
 * only transcription tier.
 * <pre>
 * ### Transcription tiers ### TXT-A TXT-B
 * TXT-A   Lemma-A  POS-A   TXT-B   Lemma-B  POS-B   Start    End
 * [...]
 * xxxx    yyyyy    zzzz                             1.2907   5.3719
 *                         xxxx    yyyyy    zzzz    7.6353   10.7190
 * xxxx    yyyyy    zzzz    aaaa    bbbb     cccc    12.2907  15.371
 * </pre>
 * lternatively, in the row-per-annotation setup I'd use a separate column
 * for tier type (transcription/annot./desc.) and/or for the speaker.
 * <pre>
 * (Tiername   Tiertype  Speaker    Annotation    Start    End)
 * TXT-A       t         A          xxxx          1.2907   5.3719
 * Lemma-A     a         A          yyyy          1.2907   5.3719
 * POS-A       a         A          zzzz          1.2907   5.3719
 * TXT-B       t         B          xxxx          1.2907   5.3719
 * </pre>
 * </blockquote>
 */
public class InelTsvConverter {

    ArrayList<String> lines = new ArrayList<String>();
    public boolean appendSpaces = true;

    /** Creates a new instance of TextConverter */
    public InelTsvConverter() {
    }

    public void readText(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String nextLine = new String();
        System.out.println("Started reading document");
        while ((nextLine = br.readLine()) != null){
            lines.add(nextLine);
        }
        br.close();
    }

    public void readText(File file, String encoding) throws
        FileNotFoundException, IOException, UnsupportedEncodingException{
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, encoding);
        BufferedReader br = new BufferedReader(isr);
        String nextLine = new String();
        System.out.println("Started reading document " + file.getAbsolutePath());
        int lineCount=0;
        while ((nextLine = br.readLine()) != null){
            // remove komisches Zeichen am Anfang von Unicode-kodierten Dateien
            if (lineCount==0 && encoding.startsWith("UTF") && nextLine.charAt(0)=='\uFEFF'){
                nextLine = nextLine.substring(1);
            }
            lines.add(nextLine);
            lineCount++;
        }
        br.close();
        System.out.println("Document read.");
    }


    public BasicTranscription importText(){
        BasicTranscription bt = new BasicTranscription();
        Speakertable st = bt.getHead().getSpeakertable();
        Set<String> speakers = new HashSet<String>();
        Set<String> tiers = new HashSet<String>();
        for (String line : lines) {
            String[] fields = line.split("\\t");
            tiers.add(fields[0] + "\t" + fields[1] + "\t" + fields[2]);
            speakers.add(fields[1]);
        }
        int n_speakers = 0;
        Map<String, String> speakermap = new HashMap<String, String>();
        for (String s : speakers) {
            String sid = "SPK" + n_speakers;
            Speaker speaker = new Speaker();
            speaker.setID(sid);
            speaker.setAbbreviation(s);
            try {
                st.addSpeaker(speaker);
            } catch (JexmaraldaException ex) {
                ex.printStackTrace();
            }
            speakermap.put(s, sid);
            n_speakers++;
        }
        Timeline timeline = bt.getBody().getCommonTimeline();
        String tli = timeline.addTimelineItem();
        int n_tiers  = 0;
        Map<String, Tier> tiermap = new HashMap<String, String>();
        for (String s : tiers) {
            String tid = "TIE" + n_tiers;
            Tier tier = new Tier();
            String[] fields = s.split("\\t");
            tier.setID("TIE" + tiers);
            tier.setType(fields[1]);
            tier.setDisplayName(fields[0]);
            tier.setSpeaker(speakermap.get(fields[2]));
            try {
                bt.getBody().addTier(tier);
            } catch (JexmaraldaException ex) {
                ex.printStackTrace();
            }
            tiermap.add(fields[0], tier);
            n_tiers++;
        }
        for (String line : lines) {
            String[] fields = line.split("\\t");
            Event e = new Event;
            e.setDescription(fields[3]);
            String tli_start = timeline.addTimelineItem(fields[4]);
            String tli_end = timeline.addTimelineItem(fields[5]);
            e.setStart(tli_start);
            e.setEnd(tli_end);
            Tier tier = tiermap.get(fields[0]);
            tier.addEvent(e);
        }
        return bt;
    }


    public static void main(String[] args){
        try {
            File f = new File("T:\\TP-Z2\\DATEN\\Anne_Siekmeyer\\EXMARALDA\\hlte\\G73\\abschrift_tagged.txt");
            TreeTaggerConverter tc = new TreeTaggerConverter();
            tc.readText(f);
            BasicTranscription bt = tc.importText();
            bt.writeXMLToFile("T:\\TP-Z2\\DATEN\\Anne_Siekmeyer\\EXMARALDA\\ausgewhlte\\G73\\abschrift_tagged.exb", "none");
            System.out.println(tc.importText().toXML());
        } catch (PatternSyntaxException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String exportBasicTranscription(BasicTranscription bt) throws IOException{

        // determine main (token) tier
        Tier mainTier = null;
        for (int pos=0; pos<bt.getBody().getNumberOfTiers(); pos++){
            Tier thisTier = bt.getBody().getTierAt(pos);
            if ((thisTier.getSpeaker()!=null) && (thisTier.getType().equals("t"))){
                mainTier = thisTier;
                break;
            }
        }

        if (mainTier==null){
            throw new IOException("No main tier found for this transcription");
        }

        // determine dependent (lemma, pos, etc.) tiers
        Vector<Tier> dependentTiers = new Vector<Tier>();
        for (int pos=0; pos<bt.getBody().getNumberOfTiers(); pos++){
            Tier thisTier = bt.getBody().getTierAt(pos);
            if ((mainTier.getSpeaker().equals(thisTier.getSpeaker())) && (thisTier.getType().equals("a"))){
                dependentTiers.addElement(thisTier);
            }
        }

        StringBuffer result = new StringBuffer();

        for (int pos=0; pos<bt.getBody().getCommonTimeline().getNumberOfTimelineItems(); pos++){
            TimelineItem tli = bt.getBody().getCommonTimeline().getTimelineItemAt(pos);
            try {
                Event e = mainTier.getEventAtStartPoint(tli.getID());
                result.append(e.getDescription().trim());
                for (Tier tier : dependentTiers){
                    if (tier.containsEventAtStartPoint(tli.getID())){
                        Event a = tier.getEventAtStartPoint(tli.getID());
                        result.append("\t");
                        result.append(a.getDescription());
                    }
                    //if (!(dependentTiers.indexOf(tier)==dependentTiers.size()-1)){}
                }
                result.append(System.getProperty("line.separator"));
            } catch (JexmaraldaException ex) { /*do nothing - we simply ignore this non-existing event */}
        }
        return result.toString();
    }

    public void writeText(BasicTranscription bt, File file) throws IOException {
        String text = exportBasicTranscription(bt);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(text.getBytes("UTF-8"));
        fos.close();

    }


}
