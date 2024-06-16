package bl00dy_c0d3_.echovr_installer;

import jdk.jfr.StackTrace;

import javax.imageio.IIOException;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//This Class will uninstall echo, install echo and copy obb
public class InstallerQuest {
    static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    String[] fileList;
    Path targetPath;
    static Path tempPath = Paths.get(System.getProperty("java.io.tmpdir"));




    public void installAPK(String pathToApkObb, String apkfileName, String obbfileName, SpecialLabel progressLabel, JDialog parrentFrame)  {
        InstallerQuest outFrame = this;

        String dir = System.getProperty("java.io.tmpdir") + "platform-tools/";
        File file = new File(dir);
        if (!file.exists()){
            file.mkdirs();
            System.out.println("I DID IT!!!!!");
        }


        if (isWindows) {
            fileList = new String[]{"adb.exe", "AdbWinApi.dll", "AdbWinUsbApi.dll", "etc1tool.exe", "fastboot.exe", "hprof-conv.exe", "libwinpthread-1.dll", "make_f2fs.exe", "make_f2fs_casefold.exe", "mke2fs.conf", "mke2fs.exe", "NOTICE.txt", "source.properties", "sqlite3.exe"};
        }
        else{
            fileList = new String[]{"lib64", "adb", "fastboot", "make_f2fs_casefold", "mke2fs.conf", "source.properties", "etc1tool", "hprof-conv", "make_f2fs", "mke2fs", "mke2fs.exe", "NOTICE.txt", "sqlite3"};
        }

        if (isWindows) {
            //TODO read filelist from the folder instead of that
            for (int a = 0; a < fileList.length; a++) {
                targetPath = Paths.get(tempPath + "/platform-tools/" + fileList[a]);
                try {
                    InputStream stream = getClass().getClassLoader().getResourceAsStream("platform-tools/" + fileList[a]);
                    Files.copy(stream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {

                }
                //TODO ^
            }
        }
        else{
            //TODO read filelist from the folder instead of that
            for (int a = 0; a < fileList.length; a++) {
                targetPath = Paths.get(tempPath + "/platform-tools-linux/" + fileList[a]);
                System.out.println(tempPath + "/platform-tools-linux/" + fileList[a]);

                try {
                    InputStream stream = getClass().getClassLoader().getResourceAsStream("platform-tools-linux/" + fileList[a]);
                    Files.copy(stream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {

                }
                //TODO ^
            }

        }







        // Check if any device is connected
        int deviceConnected = checkQuestStatus();
        if (deviceConnected == 0) {

            File apkFile = new File(pathToApkObb + "/" + apkfileName);
            File obbFile = new File(pathToApkObb + "/" + obbfileName);
            if(!apkFile.exists() ||  !obbFile.exists()) {
                System.out.println("APK or OBB FILE NOT FOUND");
                ErrorDialog.errorDialog(parrentFrame, "File not found", "APK or OBB FILE NOT FOUND. PLEASE DOWNLOAD IT ON STEP 4/5!");

                return;
            }


            if (progressLabel != null){
                progressLabel.setText("Installation started! Wait!");
                parrentFrame.repaint();
            }


            if(isWindows) {
                runShellCommand(tempPath + "/platform-tools/adb.exe " + "uninstall com.readyatdawn.r15", 1);
                runShellCommand(tempPath + "/platform-tools/adb.exe " + "install " + pathToApkObb + "/" + apkfileName, 2);
                runShellCommand(tempPath + "/platform-tools/adb.exe " + "shell \"mkdir /storage/self/primary/Android/obb/com.readyatdawn.r15\"", 3);
                runShellCommand(tempPath + "/platform-tools/adb.exe " + "push " + pathToApkObb + "/" + obbfileName + " \"/storage/self/primary/Android/obb/com.readyatdawn.r15/\"", 4);
            }
            else{
                runShellCommand(tempPath + "/platform-tools-linux/adb " + "uninstall com.readyatdawn.r15", 1);
                runShellCommand(tempPath + "/platform-tools-linux/adb " + "install " + pathToApkObb + "/" + apkfileName, 2);
                runShellCommand(tempPath + "/platform-tools-linux/adb " + "shell \"mkdir /storage/self/primary/Android/obb/com.readyatdawn.r15\"", 3);
                runShellCommand(tempPath + "/platform-tools-linux/adb " + "push " + pathToApkObb + "/" + obbfileName + " \"/storage/self/primary/Android/obb/com.readyatdawn.r15/\"", 4);

            }

            if (progressLabel != null){
                progressLabel.setText("Installation done!");
                parrentFrame.repaint();
            }
        }
        else if (deviceConnected == 1) {
            ErrorDialog.errorDialog(parrentFrame, "Not authorized", "<html>You need to allow this PC to use adb on your Quest. <br>Replug the cable and check inside your Quest. You should get asked if you allow the connection.</html>");
            System.out.println("Device is unauthorized!");
        }
        else if (deviceConnected == -1) {
            // Create an instance of ErrorDialog
            ErrorDialog errorDialog = new ErrorDialog();

            // Show the error dialog
            errorDialog.errorDialog(parrentFrame, "No Device detected", "<html>Either your Quest is not connected, or you don't have the Developer Mode enabled</html>");

            errorDialog.debugMode();
            System.out.println("No device is connected.");
        }

    }


    //0 = connected, 1 = unauthorized, -1 not connected
    // Method to check if any device is connected based on the adb devices output
    private static int checkQuestStatus() {

        // Start the process
        Process process = null;
        try {
            System.out.println(tempPath + "/platform-tools/adb");

            if(isWindows) {
                process = new ProcessBuilder(tempPath + "/platform-tools/adb.exe", "devices").start();
            }
            else{
                process = new ProcessBuilder(tempPath + "/platform-tools/adb", "devices").start();

            }

        } catch (IOException e) {
            //e.printStackTrace();
        }
        //TODO ^


        // StringBuilder to accumulate the output
        StringBuilder stdOutResult = new StringBuilder();

        // Read the output from the process's input stream
        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String stdout;
            while ((stdout = stdInput.readLine()) != null) {
                stdOutResult.append(stdout).append("\n"); // Append each line and a newline character
            }

        }
        catch (IOException e){}
        //TODO ^

        // Print the result
        System.out.println(stdOutResult + "");



        // Check each line for device connection status
        for (String line : (stdOutResult + "").split("\\r?\\n")) {

            System.out.println(line);
            // Check if the line ends with "device" indicating a connected device
            if (line.endsWith("device")) {
                return 0;
            }
            // Check if the line ends with "unauthorized" indicating a connected device
            if (line.endsWith("unauthorized")) {
                return 1;
            }
        }

        // If no device lines containing "device or unauthorized" were found
        return -1;
    }

    private void runShellCommand(String shellCommand, int step){


        if (step == 1){
            JOptionPane.showMessageDialog(null, "<html>Press OK to start the installation. It can take a minute to install!</html>", "Notification", JOptionPane.INFORMATION_MESSAGE);

        }

        try {
            if (isWindows) {
                Process process = Runtime.getRuntime().exec(shellCommand);
                process.waitFor();
                System.out.println("DONE");


            } else {
                Process process = Runtime.getRuntime().exec(shellCommand);
                process.waitFor();
                System.out.println("DONE");
            }

            if (step == 4){
                JOptionPane.showMessageDialog(null, "<html>Installation of Echo is done. You can start it now on your Quest.<br> DON'T CLICK ON RESTORE IF YOU WILL GET ASKED TO OR YOU NEED TO REINSTALL AGAIN!</html>", "Notification", JOptionPane.INFORMATION_MESSAGE);

            }




        }
        catch (Exception e){

        }

    }



}
