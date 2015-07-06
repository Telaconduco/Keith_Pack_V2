import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class InstallMods extends JPanel implements Runnable{
    public final JProgressBar progressBar1;
    public final JProgressBar progressBar2;
    public final JProgressBar progressBar3;
    public final JLabel[] labels = new JLabel[6];
    public float iforge;
    public float ipercentage;
    public float idone;
    private final Thread thread;
    public InstallMods(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        progressBar1 = new JProgressBar();
        progressBar2 = new JProgressBar();
        progressBar3 = new JProgressBar();
        labels[0] = new JLabel("Step 1: Install Forge");
        labels[1] = new JLabel("Step 2: Install Mods");
        labels[2] = new JLabel("Step 3: Setup Minecraft");
        labels[3] = new JLabel("0%");
        labels[4] = new JLabel("0%");
        labels[5] = new JLabel("0%");
        for(int i = 0; i < labels.length; i++){
            labels[i].setFont(new Font("Constantina",Font.PLAIN,30));
        }
        this.add(Box.createVerticalGlue());
        this.add(labels[0]);
        this.add(progressBar1);
        this.add(labels[3]);
        this.add(Box.createVerticalGlue());
        this.add(labels[1]);
        this.add(progressBar2);
        this.add(labels[4]);
        this.add(Box.createVerticalGlue());
        this.add(labels[2]);
        this.add(progressBar3);
        this.add(labels[5]);
        this.add(Box.createVerticalGlue());
        new progressbarupdate(this);
        thread = new Thread(this);
        thread.start();

    }
    public void run(){
        //22 Minute Installation: Single Port
        System.out.println("Initializing...");
        String OS = System.getProperties().getProperty("os.name").toLowerCase();
        String home = System.getProperties().getProperty("user.home");
        File modziplocation = new File("");
        if(OS.contains("mac")){
            modziplocation = new File(home + "/Library/Application Support/minecraft");
        }else if(OS.contains("windows")){
            modziplocation = new File(home + "/AppData/Roaming/.minecraft");
        }else{
            JOptionPane.showConfirmDialog(this,"Error Cannot Install Mods(Operating System not compatible");
        }
        System.out.println("Completed...");
        File delzip = new File(modziplocation + "/mods");
        File forge = new File(modziplocation + "/forge-1.7.10-10.13.4.1448-1.7.10-installer.jar");
        File flan = new File(modziplocation + "/Flan");
        File coremods = new File(modziplocation + "/coremods");
        try {
            Socket socket = new Socket();
            System.out.println("Connecting...");
            socket.connect(new InetSocketAddress("telaconduco.ddns.net", 35592), 10000);
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
            if(socket.isConnected()) {
                System.out.println("Connected...");
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                File modfolder = new File(modziplocation.getPath() + "/mods");
                if (!modfolder.exists()) {
                    if (!forge.exists()) {
                        System.out.println("Forge not installed");
                        pw.println("getforge");
                        pw.println("");
                        System.out.println("Getting Forge...");
                        String bytesrequired = br.readLine();
                        long brequired = Long.parseLong(bytesrequired);
                        InputStream is = socket.getInputStream();
                        downloadFile(forge,is,pw,brequired);
                    }
                        Desktop.getDesktop().open(forge);
                        JOptionPane.showMessageDialog(this, "Please now RUN Minecraft Forge and keep the Installer open. Press ok when complete.");
                        boolean ran = false;
                        while (!modfolder.exists()) {
                            if (!ran) {
                                JOptionPane.showMessageDialog(this, "Forge Installation Error. Please Try Again.");
                                ran = true;
                                Desktop.getDesktop().open(forge);
                            }
                        }
                        forge.delete();
                }
                iforge = 100;
                    System.out.println("Get Mods");
                    pw.println("getmods");
                    pw.println("");
                    String bytesrequired = br.readLine();
                    System.out.println("Bytes Req:" + bytesrequired);
                    long brequired = Long.parseLong(bytesrequired);
                    long mtotalbytes = 0;
                    boolean readfiles = true;
                    InputStream is = socket.getInputStream();
                    deleteFiles(delzip);
                    delzip.delete();
                    deleteFiles(flan);
                    flan.delete();
                    coremods.delete();
                    downloadFiles(is, br, pw, readfiles, modziplocation, mtotalbytes, brequired);
                    br.close();
                    pw.close();
                    is.close();
                    socket.close();
                    ipercentage = 100;
                    if (OS.contains("mac") || OS.contains("windows")) {
                        File launchersaves = new File(modziplocation + "/launcher_profiles.json");
                        FileReader fr = new FileReader(launchersaves);
                        BufferedReader br2 = new BufferedReader(fr);
                        int linecount = 0;
                        int plinestart = 0;
                        int slpf = 0;
                        String line;
                        idone = 5;
                        while((line = br2.readLine()) != null){
                            linecount += 1;
                            if(line.contains("profiles")){
                                plinestart = linecount;
                            }
                            if(line.contains("selectedProfile")){
                                slpf = linecount;
                            }
                        }
                        idone = 30;
                        br2.close();
                        fr.close();
                        idone = 35;
                        String[] alllines = new String[linecount];
                        FileReader fr2 = new FileReader(launchersaves);
                        BufferedReader br3 = new BufferedReader(fr2);
                        for(int i = 0; i < linecount; i++){
                            alllines[i] = br3.readLine();
                        }
                        idone = 50;
                        FileWriter fw = new FileWriter(launchersaves);
                        BufferedWriter bw = new BufferedWriter(fw);
                        for(int i = 0; i < plinestart; i++){
                            bw.write(alllines[i]);
                            bw.newLine();
                        }
                        bw.write("\"KPV2\": {");
                        bw.newLine();
                        bw.write("\"name\": \"KPV2\",");
                        bw.newLine();
                        bw.write("\"lastVersionId\": \"1.7.10-Forge10.13.4.1448-1.7.10\",");
                        bw.newLine();
                        if(OS.contains("mac")) {
                            bw.write("\"javaDir\": \"/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java\"");
                        }else{
                            bw.write("\"javaDir\": \"C:\\\\Program Files (x86)\\\\Java\\\\jdk" + System.getProperties().getProperty("java.version") + "\\\\bin\\\\java.exe\"");
                        }
                        bw.newLine();
                        bw.write("},");
                        bw.newLine();
                        for(int i = 0; i < linecount - plinestart; i++){
                            if(i + plinestart == slpf - 1){
                                bw.write("\"selectedProfile\": \"KPV2\",");
                            }else {
                                bw.write(alllines[i + plinestart]);
                            }
                            bw.newLine();
                        }
                        bw.close();
                        fw.close();
                        idone = 100;
                        JOptionPane.showMessageDialog(this,"Thank You For Installing Keith Pack V2");
                        System.exit(0);
                    } else {
                        JOptionPane.showMessageDialog(this, "This Operating System is Unsupported For Final Unzipping Installation Please Manually Install");
                        System.exit(0);
                    }
                }else {
                JOptionPane.showMessageDialog(this,"Could Not Connect to AwesomeKnightServer");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void deleteFiles(File file){
        File[] files = file.listFiles();
        if(files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteFiles(files[i]);
                    files[i].delete();
                } else {
                    files[i].delete();
                }
            }
        }
        }
    private void downloadFile(File file,InputStream is, PrintWriter pw,long totalbytes){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[32768];
            int count;
            boolean shouldRead = true;
            long currentbytes = 0;
            while(shouldRead){
                pw.println("g");
                count = is.read(buffer);
                bos.write(buffer,0,count);
                currentbytes += count;
                iforge = (currentbytes * 100 / totalbytes * 100) / 100;
                if(totalbytes == currentbytes){
                    shouldRead = false;
                    bos.flush();
                    fos.flush();
                    bos.close();
                    fos.close();
                }
                pw.println("q");
            }
        }catch (Exception d){
            d.printStackTrace();
        }
    }
    private void downloadFiles(InputStream is, BufferedReader reader, PrintWriter pw, boolean readfiles, File file, long mtotalbytes, long brequired) {
        try {
            while (readfiles) {
                String type = reader.readLine();
                if (type.equals("f")) {
                    System.out.println("File");
                    long fbrequired = Long.parseLong(reader.readLine());
                    System.out.println(fbrequired);
                    String name = reader.readLine();
                    File nfile = new File(file.getPath() + "/" + name);
                    System.out.println(nfile.getPath());
                    FileOutputStream fos = new FileOutputStream(nfile);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    byte[] buffer = new byte[32768];
                    long totalbytes = 0;
                    int count;
                    boolean shouldRead;
                    System.out.println("Checking for Zero Byte...");
                    String zerobytefile = reader.readLine();
                    if (zerobytefile.equals("g")) {
                        shouldRead = true;
                    } else {
                        System.out.println("0 Bytes");
                        shouldRead = false;
                        bos.close();
                        fos.close();
                        nfile.createNewFile();
                        pw.println("y");
                    }
                    pw.println("r");
                    while (shouldRead) {
                        count = is.read(buffer);
                        bos.write(buffer, 0, count);
                        System.out.println(count);
                        totalbytes += count;
                        mtotalbytes += count;
                        ipercentage = (mtotalbytes * 100 / brequired * 100) / 100;
                        if (totalbytes == fbrequired) {
                            shouldRead = false;
                            bos.close();
                            fos.close();
                            pw.println("y");
                        }
                    }
                    if (mtotalbytes == brequired) {
                        readfiles = false;
                    }
                } else {
                    String name = reader.readLine();
                    System.out.println(name);
                    File ndir = new File(file + "/" + name);
                    System.out.println("Creating new Directory:" + ndir.getPath());
                    Files.createDirectory(ndir.toPath());
                }
            }
        } catch (Exception t) {
            t.printStackTrace();
            JOptionPane.showMessageDialog(this,"There was an error please restart the installer");
        }
    }
}