/**
 * Created by James on 6/25/2015.
 */
public class progressbarupdate implements Runnable {
    InstallMods IM;
    boolean running = true;
    Thread thread2;
    public progressbarupdate(InstallMods IM){
        this.IM = IM;
        thread2 = new Thread(this);
        thread2.start();
    }
    public void run(){
        while(running){
            int fforge = (int)IM.iforge;
            int fperc = (int)IM.ipercentage;
            int fdone = (int)IM.idone;
            IM.progressBar1.setValue(fforge);
            IM.labels[3].setText(fforge + "%");
            IM.progressBar2.setValue(fperc);
            if(fperc >= 80 && fperc <= 84){
                IM.labels[4].setText(fperc + "% Loading Large Mods... Please Wait");
            }else {
                IM.labels[4].setText(fperc + "%");
            }
            IM.progressBar3.setValue(fdone);
            IM.labels[5].setText(fdone + "%");
        }
        try{
            thread2.sleep(500);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
