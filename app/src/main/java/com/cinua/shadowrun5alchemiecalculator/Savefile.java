package com.cinua.shadowrun5alchemiecalculator;
import android.content.Context;
import android.net.Uri;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Savefile{
    public static final String FILE_NAME = "Stats.txt";
    public static final String FILE_EXTENSION = ".txt";

    private static final String ATTRIBUTE_FORMAT = "%s:%s\n";
    private static final String MAGIC_ATTRIBUTE_NAME = "Magie";
    private static final String ALCHEMY_ATTRIBUTE_NAME = "Skill";
    private static final String DRAIN_POOL_ATTRIBUTE_NAME = "Drain-Pool";
    private static final String STATE_MONITOR_ATTRIBUTE_NAME = "Zustandsmonitor";

    private String characterName;
    private String magicRating;
    private String alchemyRating;
    private String drainPool;
    private String stateMonitor;

    public Savefile(String characterName, String magicRating, String alchemyRating, String drainPool, String stateMonitor){
        this.characterName = characterName;
        this.magicRating = magicRating;
        this.alchemyRating = alchemyRating;
        this.drainPool = drainPool;
        this.stateMonitor = stateMonitor;
    }

    private static String loadFromStream(InputStream is) throws IOException{
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;
        while((line = br.readLine()) != null){
            sb.append(line);
            sb.append("\n");
        }
        br.close();

        return sb.toString();
    }

    private static void saveRawDataToStream(OutputStream os, String content) throws IOException{
        os.write(content.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    private static String loadRawDataFromInternalFile(Context c, String path) throws IOException{
        FileInputStream is = c.openFileInput(path);
        return loadFromStream(is);
    }

    private static void saveRawDataToInternalFile(Context c, String filename, String content) throws IOException{
        FileOutputStream os = c.openFileOutput(filename,Context.MODE_PRIVATE);
        saveRawDataToStream(os, content);
    }

    private static String loadRawDataFromUri(Context c, Uri r) throws IOException{
        InputStream is = c.getContentResolver().openInputStream(r);
        return loadFromStream(is);
    }

    private static void saveRawDataToUri(Context c, Uri r, String content) throws IOException{
        OutputStream os = c.getContentResolver().openOutputStream(r);
        saveRawDataToStream(os, content);
    }

    private static Savefile makeFromRawData(String rawData) throws IndexOutOfBoundsException, NumberFormatException{
        String[] fields = rawData.split("\n");
        fields = Arrays.stream(fields).map(field -> field.contains(":") ? field.split(":")[1] : field).toArray(String[]::new);
        return new Savefile(fields[0], fields[1], fields[2], fields[3], fields[4]);
    }

    public static Savefile loadFromInternalFile(Context c, String path) throws IOException{
        return makeFromRawData(loadRawDataFromInternalFile(c, path));
    }

    public static Savefile loadFromUri(Context c, Uri uri) throws IOException{
        return Savefile.makeFromRawData(loadRawDataFromUri(c, uri));
    }

    private String toRawData(){
        return
                characterName + "\n" +
                String.format(ATTRIBUTE_FORMAT, MAGIC_ATTRIBUTE_NAME, magicRating) +
                String.format(ATTRIBUTE_FORMAT, ALCHEMY_ATTRIBUTE_NAME, alchemyRating) +
                String.format(ATTRIBUTE_FORMAT, DRAIN_POOL_ATTRIBUTE_NAME, drainPool) +
                String.format(ATTRIBUTE_FORMAT, STATE_MONITOR_ATTRIBUTE_NAME, stateMonitor);
    }

    public void saveToInternalFile(Context context) throws IOException{
        saveRawDataToInternalFile(context, FILE_NAME, toRawData());
    }

    public void saveToExternalFile(Context c, Uri uri) throws IOException{
        saveRawDataToUri(c, uri, toRawData());
    }

    public String getCharacterName(){
        return characterName;
    }

    public String getMagicRating(){
        return magicRating;
    }

    public String getAlchemyRating(){
        return alchemyRating;
    }

    public String getDrainPool(){
        return drainPool;
    }

    public String getStateMonitor(){
        return stateMonitor;
    }
}
