package com.dailycodebuffer.filemngt;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download implements Runnable{
    String link;
    File out;
    public Thread t;

    public Download(String link, File out)
    {
        this.link = link;
        this.out = out;

//        t = new Thread(this);
//        System.out.println("New thread: " + t);
//        t.start(); // Starting the threa
    }

//    private boolean exit;
//    public void setExit(boolean exit)
//    {
//        this.exit = exit;
//    }
//
//    // for stopping the thread
//    public void stop()
//    {
//        exit = true;
//    }

    @Override
    public void run()
    {
        try {
//            while (!Thread.interrupted()) {
////                System.out.println("Thread is running");
//                t.interrupt();
//            }
//            System.out.println("Thread has stopped.");

            URL url =  new URL(link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            double filesize = (double) http.getContentLengthLong();
            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(this.out);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] buffer = new byte[1024];
            double downloaded = 0.00;
            int read = 0;
            double percentDownloaded = 0.00;
            while ((read = in.read(buffer, 0, 1024)) >= 0)
            {
                bout.write(buffer, 0, read);
                downloaded += read;
                percentDownloaded = (downloaded*100)/filesize;
                String percent = String.format("%.4f", percentDownloaded);

            }
            bout.close();
            in.close();
            System.out.println("download complete");
//            t.stop();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

}
