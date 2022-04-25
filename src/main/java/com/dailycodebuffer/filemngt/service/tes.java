package com.dailycodebuffer.filemngt.service;

import Luxand.FSDK;
import Luxand.FSDK.*;
import com.dailycodebuffer.filemngt.entity.Attachment;
import org.springframework.context.annotation.Bean;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class tes {

        void contextLoads() throws IOException {
        try {
            int r = FSDK.ActivateLibrary("Ib4uDsoRC6McYoq+PUaTBmmOx4kqrdtF0du8klpKBieX47HBwyY5uNPi1MSxefmpxfDHr++D5yTaTs09u+bNi1LBri9/SNcadmAXBgiMRBfQW0x3xSIgLO6uNVxV/ia41e/BnlfVkrNN0BnI26UkdhpshzRk9TC/M1HmY98H7Ic=");
            if (r != FSDK.FSDKE_OK) {
                System.out.println("error1");
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("error2");
        }

        FSDK.Initialize();
		/*imagefile();
		menuEnrollFace();
		menuMatchFace();*/

        FSDK.Finalize();
    }

        public final java.util.List<TFaceRecord> FaceList = new ArrayList<TFaceRecord>();
        public int FaceDetectionThreshold = 2;
        public int FARValue = 100;
        public static final int width = 640;
        public static final int height = 480;

        static class TFaceRecord {
            public FSDK_FaceTemplate.ByReference FaceTemplate;
            public TFacePosition.ByReference FacePosition;
            public FSDK_Features.ByReference FacialFeatures;
            public String ImageFileName;
            public HImage image;
            public HImage faceImage;
        }

        private final DefaultListModel listModel = new DefaultListModel();
        private final java.util.List<ImageIcon> listImages = new ArrayList<ImageIcon>();
        public final java.util.List<String> listStrings = new ArrayList<String>();
        //	private final MyJListCellRenderer listRenderer = new MyJListCellRenderer();
        private boolean cleaning = false;

        class Sortable {
            public float similarity;
            public int index;
        }


        @Bean
        public void menuEnrollFace(List<Attachment> imageList) {
        //Assuming that faces are vertical (HandleArbitraryRotations = false) to speed up face detection
        FSDK.SetFaceDetectionParameters(false, true, 384);
        FSDK.SetFaceDetectionThreshold(FaceDetectionThreshold);

//		FileDialog dlg = new FileDialog( FileDialog.LOAD);
//		dlg.setFile("*.jpg");
//		dlg.setVisible(true);
//		String fileName = dlg.getDirectory() + dlg.getFile();

//		FileDialog tes = new FileDialog();

        for (Attachment imageBase64 : imageList) {

            File sample = new File(String.valueOf(decode(String.valueOf(imageBase64.getData()))));

            TFaceRecord fr = new TFaceRecord();
            fr.ImageFileName = String.valueOf(sample.getAbsoluteFile());
            fr.image = new HImage();


            int res = FSDK.LoadImageFromFile(fr.image, String.valueOf(sample));
            if (res != FSDK.FSDKE_OK) {
                System.out.println("Cannot load " + sample + " with error " + res);
                return;
            }
            System.out.println("Enrolling '" + sample);
            fr.FacePosition = new TFacePosition.ByReference();
            res = FSDK.DetectFace(fr.image, fr.FacePosition);
            if (res != FSDK.FSDKE_OK) {
                System.out.println("No faces found. Try to lower the Minimal Face Quality parameter in the Options dialog box.");
                FSDK.FreeImage(fr.image);
                return;
            }

            fr.faceImage = new HImage();
            FSDK.CreateEmptyImage(fr.faceImage);
            FSDK.CopyRect(fr.image,
                    (int) (fr.FacePosition.xc - fr.FacePosition.w * 0.5),
                    (int) (fr.FacePosition.yc - fr.FacePosition.w * 0.5),
                    (int) (fr.FacePosition.xc + fr.FacePosition.w * 0.5),
                    (int) (fr.FacePosition.yc + fr.FacePosition.w * 0.5), fr.faceImage);

            fr.FacialFeatures = new FSDK_Features.ByReference();
            res = FSDK.DetectEyesInRegion(fr.image, fr.FacePosition, fr.FacialFeatures);
            if (res != FSDK.FSDKE_OK) {
                System.out.println("Error detecting facial features.");
                FSDK.FreeImage(fr.image);
                FSDK.FreeImage(fr.faceImage);
                return;
            }

            fr.FaceTemplate = new FSDK_FaceTemplate.ByReference();
            res = FSDK.GetFaceTemplateInRegion(fr.image, fr.FacePosition, fr.FaceTemplate);
            if (res != FSDK.FSDKE_OK) {
                System.out.println("Error retrieving face template.");

                FSDK.FreeImage(fr.image);
                FSDK.FreeImage(fr.faceImage);
                return;
            }

            FaceList.add(fr);

            // Adding item to jList1 via its Model
            HImage tempImage = new HImage();
            FSDK.CreateEmptyImage(tempImage);
            int widthByReference[] = new int[1];
            FSDK.GetImageWidth(fr.faceImage, widthByReference);
            double ratio = 100.0 / widthByReference[0];
            FSDK.ResizeImage(fr.faceImage, ratio, tempImage);
            Image thumbnail[] = new Image[1];
            FSDK.SaveImageToAWTImage(tempImage, thumbnail, FSDK_IMAGEMODE.FSDK_IMAGE_COLOR_24BIT);
            FSDK.FreeImage(tempImage);
            listImages.add(new ImageIcon(thumbnail[0]));
            listStrings.add(fr.ImageFileName);
            listModel.addElement(FaceList.size() - 1);

            System.out.println("File '" + sample + "' enrolled\n");
        }


    }


        public void menuMatchFace() {
        if (FaceList.isEmpty()) {
            System.out.println("Please enroll faces first");
            return;
        }

//			FileDialog dlg = new FileDialog((java.awt.Frame)null, "Open File", FileDialog.LOAD);
//			dlg.setFile("*.jpg");
//			dlg.setVisible(true);
//			String fileName = dlg.getDirectory() + dlg.getFile();

        File sample2 = new File("C:\\image\\will2.jpg");
        TFaceRecord fr = new TFaceRecord();
        fr.ImageFileName = String.valueOf(sample2.getAbsoluteFile());
        fr.image = new HImage();

        int res = FSDK.LoadImageFromFile(fr.image, String.valueOf(sample2));
        if (res != FSDK.FSDKE_OK) {
            System.out.println("Cannot load ");
            return;
        }

        fr.FacePosition = new TFacePosition.ByReference();
        res = FSDK.DetectFace(fr.image, fr.FacePosition);
        if (res != FSDK.FSDKE_OK) {
            System.out.println("No faces found. Try to lower the Minimal Face Quality parameter in the Options dialog box.");
            FSDK.FreeImage(fr.image);
            return;
        }

        fr.faceImage = new HImage();
        FSDK.CreateEmptyImage(fr.faceImage);
        FSDK.CopyRect(fr.image,
                (int) (fr.FacePosition.xc - fr.FacePosition.w * 0.5),
                (int) (fr.FacePosition.yc - fr.FacePosition.w * 0.5),
                (int) (fr.FacePosition.xc + fr.FacePosition.w * 0.5),
                (int) (fr.FacePosition.yc + fr.FacePosition.w * 0.5), fr.faceImage);

        fr.FaceTemplate = new FSDK_FaceTemplate.ByReference();
        res = FSDK.GetFaceTemplateInRegion(fr.image, fr.FacePosition, fr.FaceTemplate);

        if (res != FSDK.FSDKE_OK) {
            System.out.println("Error retrieving face template.");
            FSDK.FreeImage(fr.image);
            FSDK.FreeImage(fr.faceImage);
            return;
        }

//		if (resultsDialog == null) resultsDialog = new ResultsDialog(this.getFrame(), true);
//		resultsDialog.DrawFaceImage(fr);
//		resultsDialog.listModel.clear();
//		resultsDialog.listImages.clear();
//		resultsDialog.listStrings.clear();

        // Matching
        float ThresholdByReference[] = new float[1];
//			float ThresholdByReference = 50;
        FSDK.GetMatchingThresholdAtFAR((float) 0.70, ThresholdByReference);
        float Threshold = ThresholdByReference[0];
        int MatchedCount = 0;
        float SimilarityByReference[] = new float[1];

        java.util.List<Sortable> sim_ind = new ArrayList<Sortable>();
        for (int i = 0; i < FaceList.size(); ++i) {
//				System.out.println("test");
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
            TFaceRecord cur_fr = FaceList.get(sim_ind.get(i).index);
            HImage tempImage = new HImage();
            FSDK.CreateEmptyImage(tempImage);
            int widthByReference[] = new int[1];
            FSDK.GetImageWidth(cur_fr.faceImage, widthByReference);
            double ratio = 100.0 / widthByReference[0];
            FSDK.ResizeImage(cur_fr.faceImage, ratio, tempImage);
            Image thumbnail[] = new Image[1];
            FSDK.SaveImageToAWTImage(tempImage, thumbnail, FSDK_IMAGEMODE.FSDK_IMAGE_COLOR_24BIT);
            FSDK.FreeImage(tempImage);
            listImages.add(new ImageIcon(thumbnail[0]));
            listStrings.add(cur_fr.ImageFileName + "; similarity " + sim_ind.get(i).similarity * 100.0f + "%");
            listModel.addElement(i);
            System.out.println(listStrings);
            System.out.println(listImages);
            System.out.println(listModel);
            System.out.println(SimilarityByReference);
        }
    }

        public static void imagefile() throws IOException {
        String base64String = "";
        try (FileWriter fileWriter = new FileWriter(new File("C:\\Me'\\Intellij\\projek\\rest api - Copy (2)\\output.txt"))) {
            base64String = encode("C:\\image\\will2.jpg");
            fileWriter.write(base64String);
        }

        try (FileOutputStream stream = new FileOutputStream(new File("C:\\Me'\\Intellij\\projek\\rest api - Copy (2)\\output.jpg"))) {
            stream.write(decode(base64String));
        }
    }

        public static String encode(String imagePath) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(data);
    }

        public static byte[] decode(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

        //	public void convertion(){
//		ClassLoader classLoader = getClass().getClassLoader();
//		File inputFile = new File(classLoader
//				.getResource(inputFilePath)
//				.getFile());
//
//		byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
//		String encodedString = Base64
//				.getEncoder()
//				.encodeToString(fileContent);
//
//		// create output file
//		File outputFile = new File(inputFile
//				.getParentFile()
//				.getAbsolutePath() + File.pathSeparator + outputFilePath);
//
//		// decode the string and write to file
//		byte[] decodedBytes = Base64
//				.getDecoder()
//				.decode(encodedString);
//		FileUtils.writeByteArrayToFile(outputFile, decodedBytes);
//
//		assertTrue(FileUtils.contentEquals(inputFile, outputFile));
//		}


}
