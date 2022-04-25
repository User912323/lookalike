package com.dailycodebuffer.filemngt.service;

import Luxand.FSDK;
import com.dailycodebuffer.filemngt.Download;
import com.dailycodebuffer.filemngt.controller.AttachmentController;
import com.dailycodebuffer.filemngt.entity.Attachment;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;


public class Match {

        public final java.util.List<tes.TFaceRecord> FaceList = new ArrayList<tes.TFaceRecord>();
        public int FaceDetectionThreshold = 2;
        public int FARValue = 100;
        public static final int width = 640;
        public static final int height = 480;
        private final DefaultListModel listModel = new DefaultListModel();
        private final java.util.List<ImageIcon> listImages = new ArrayList<ImageIcon>();
        public final java.util.List<String> listStrings = new ArrayList<String>();
        private boolean cleaning = false;




        class TFaceRecord {
            public FSDK.FSDK_FaceTemplate.ByReference FaceTemplate;
            public FSDK.TFacePosition.ByReference FacePosition;
            public FSDK.FSDK_Features.ByReference FacialFeatures;
            public String ImageFileName;
            public FSDK.HImage image;
            public FSDK.HImage faceImage;
        }


        class Sortable {
            public float similarity;
            public int index;
        }


        public void init() {
        try {
            int r = FSDK.ActivateLibrary("dTpn0XbVZQhGU1AWvlsSFnBGoDaIqwOQe7EqiEYwwXdeuSv5RRXXtsYxAaIvvJh95WFo/F7pTpxAnGSCrE37XRPS9F4cYEjfaTDhnf3pZwt83NRAuABSpEXp+WBejj+I6I6oM88tOLDyV3MJXs6/Gee5R+RAiaFjQCnLbzxiyKc=");
            if (r != FSDK.FSDKE_OK) {
                System.out.println("error1");
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("error2");
        }

        System.out.println("sukses");

        FSDK.Initialize();


    }

     AttachmentService attachmentService;
    String fileid;
        public void menuEnrollFace(List<Attachment> imageList) {
        FSDK.SetFaceDetectionParameters(false, true, 384);
        FSDK.SetFaceDetectionThreshold(FaceDetectionThreshold);

        for (Attachment imageBase64 : imageList) {

//            System.out.println(imageBase64);
//            AttachmentController attachmentController = new AttachmentController(attachmentService);
//            attachmentController.getFileid();
            String link = "http://localhost:8080/download/11";
            File out = new File("C:\\Me'\\Intellij\\tes\\coba\\coba\\tes.jpg");
//            new Thread (new Download(link,out)).start();
//            Thread.;

            File sample = new File("C:\\Me'\\Intellij\\tes\\coba\\coba\\tes.jpg");

            tes.TFaceRecord fr = new tes.TFaceRecord();
            fr.ImageFileName = String.valueOf(sample.getAbsoluteFile());
            fr.image = new FSDK.HImage();


            int res = FSDK.LoadImageFromFile(fr.image, String.valueOf(sample));
            if (res != FSDK.FSDKE_OK) {
                System.out.println("Cannot load " + sample + " with error " + res);
                return;
            }
            System.out.println("Enrolling '" + sample);
            fr.FacePosition = new FSDK.TFacePosition.ByReference();
            res = FSDK.DetectFace(fr.image, fr.FacePosition);
            if (res != FSDK.FSDKE_OK) {
                System.out.println("No faces found. Try to lower the Minimal Face Quality parameter in the Options dialog box.");
                FSDK.FreeImage(fr.image);
                return;
            }

            fr.faceImage = new FSDK.HImage();
            FSDK.CreateEmptyImage(fr.faceImage);
            FSDK.CopyRect(fr.image,
                    (int) (fr.FacePosition.xc - fr.FacePosition.w * 0.5),
                    (int) (fr.FacePosition.yc - fr.FacePosition.w * 0.5),
                    (int) (fr.FacePosition.xc + fr.FacePosition.w * 0.5),
                    (int) (fr.FacePosition.yc + fr.FacePosition.w * 0.5), fr.faceImage);

            fr.FacialFeatures = new FSDK.FSDK_Features.ByReference();
            res = FSDK.DetectEyesInRegion(fr.image, fr.FacePosition, fr.FacialFeatures);
            if (res != FSDK.FSDKE_OK) {
                System.out.println("Error detecting facial features.");
                FSDK.FreeImage(fr.image);
                FSDK.FreeImage(fr.faceImage);
                return;
            }

            fr.FaceTemplate = new FSDK.FSDK_FaceTemplate.ByReference();
            res = FSDK.GetFaceTemplateInRegion(fr.image, fr.FacePosition, fr.FaceTemplate);
            if (res != FSDK.FSDKE_OK) {
                System.out.println("Error retrieving face template.");

                FSDK.FreeImage(fr.image);
                FSDK.FreeImage(fr.faceImage);
                return;
            }

            FaceList.add(fr);

            // Adding item to jList1 via its Model
            FSDK.HImage tempImage = new FSDK.HImage();
            FSDK.CreateEmptyImage(tempImage);
            int widthByReference[] = new int[1];
            FSDK.GetImageWidth(fr.faceImage, widthByReference);
            double ratio = 100.0 / widthByReference[0];
            FSDK.ResizeImage(fr.faceImage, ratio, tempImage);
            Image thumbnail[] = new Image[1];
            FSDK.SaveImageToAWTImage(tempImage, thumbnail, FSDK.FSDK_IMAGEMODE.FSDK_IMAGE_COLOR_24BIT);
            FSDK.FreeImage(tempImage);
            listImages.add(new ImageIcon(thumbnail[0]));
            listStrings.add(fr.ImageFileName);
            listModel.addElement(FaceList.size() - 1);

            System.out.println("File '" + sample + "' enrolled\n");
        }

        menuMatchFace();
    }

        //matching
        public void menuMatchFace() {
        if (FaceList.isEmpty()) {
            System.out.println("Please enroll faces first");
            return;
        }

        File sample2 = new File("C:\\image\\will2.jpg");

        TFaceRecord fr = new TFaceRecord();
        fr.ImageFileName = String.valueOf(sample2.getAbsoluteFile());
        fr.image = new FSDK.HImage();

        int res = FSDK.LoadImageFromFile(fr.image, String.valueOf(sample2));
        if (res != FSDK.FSDKE_OK) {
            System.out.println("Cannot load ");
            return;
        }

        fr.FacePosition = new FSDK.TFacePosition.ByReference();
        res = FSDK.DetectFace(fr.image, fr.FacePosition);
        if (res != FSDK.FSDKE_OK) {
            System.out.println("No faces found. Try to lower the Minimal Face Quality parameter in the Options dialog box.");
            FSDK.FreeImage(fr.image);
            return;
        }

        fr.faceImage = new FSDK.HImage();
        FSDK.CreateEmptyImage(fr.faceImage);
        FSDK.CopyRect(fr.image,
                (int) (fr.FacePosition.xc - fr.FacePosition.w * 0.5),
                (int) (fr.FacePosition.yc - fr.FacePosition.w * 0.5),
                (int) (fr.FacePosition.xc + fr.FacePosition.w * 0.5),
                (int) (fr.FacePosition.yc + fr.FacePosition.w * 0.5), fr.faceImage);

        fr.FaceTemplate = new FSDK.FSDK_FaceTemplate.ByReference();
        res = FSDK.GetFaceTemplateInRegion(fr.image, fr.FacePosition, fr.FaceTemplate);

        if (res != FSDK.FSDKE_OK) {
            System.out.println("Error retrieving face template.");
            FSDK.FreeImage(fr.image);
            FSDK.FreeImage(fr.faceImage);
            return;
        }

        // Matching
        float ThresholdByReference[] = new float[1];
//			float ThresholdByReference = 50;
        FSDK.GetMatchingThresholdAtFAR((float) 0.70, ThresholdByReference);
        float Threshold = ThresholdByReference[0];
        int MatchedCount = 0;
        float SimilarityByReference[] = new float[1];

        java.util.List<Sortable> sim_ind = new ArrayList<Sortable>();
        for (int i = 0; i < FaceList.size(); ++i) {
            System.out.println("test");
            FSDK.MatchFaces(fr.FaceTemplate, FaceList.get(i).FaceTemplate, SimilarityByReference);
            float Similarity = SimilarityByReference[0];
            if (Similarity >= Threshold) {
                Sortable s = new Sortable();
                s.index = i;
                s.similarity = Similarity;
                sim_ind.add(s);
                ++MatchedCount;
            }
        }

        if (MatchedCount == 0) {
            System.out.printf("No matches found. You can try to increase the FAR parameter in the Options dialog box.");
            FSDK.FreeImage(fr.image);
            FSDK.FreeImage(fr.faceImage);
            return;
        }

        Collections.sort(sim_ind, new Comparator<Sortable>() {
            @Override
            public int compare(Sortable obj1, Sortable obj2) {
                return ((Float) obj2.similarity).compareTo(obj1.similarity);
            }
        });

        // Adding JList elements to resultsDialog via its Model
        for (int i = 0; i < sim_ind.size(); ++i) {
            tes.TFaceRecord cur_fr = FaceList.get(sim_ind.get(i).index);
            FSDK.HImage tempImage = new FSDK.HImage();
            FSDK.CreateEmptyImage(tempImage);
            int widthByReference[] = new int[1];
            FSDK.GetImageWidth(cur_fr.faceImage, widthByReference);
            double ratio = 100.0 / widthByReference[0];
            FSDK.ResizeImage(cur_fr.faceImage, ratio, tempImage);
            java.awt.Image thumbnail[] = new java.awt.Image[1];
            FSDK.SaveImageToAWTImage(tempImage, thumbnail, FSDK.FSDK_IMAGEMODE.FSDK_IMAGE_COLOR_24BIT);
            FSDK.FreeImage(tempImage);
            listImages.add(new ImageIcon(thumbnail[0]));
            listStrings.add("; similarity " + sim_ind.get(i).similarity * 100.0f + "%");
            listModel.addElement(i);
            System.out.println(listStrings);

        }
    }
    }


