package bl00dy_c0d3_.echovr_installer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class Helpers {
    @Contract("_, _ -> new")
    public static @NotNull SpecialLabel createSpecialLabel(String text, int fontSize) {
        return new SpecialLabel(text, fontSize);
    }

    public static @NotNull SpecialLabel createSpecialLabel(@NotNull String text, int fontSize, int x, int y) {
        SpecialLabel label = createSpecialLabel(text, fontSize);
        label.setLocation(x, y);
        return label;
    }

    public static @NotNull SpecialLabel createSpecialLabel(String text, int fontSize, int x, int y, Dimension size, Color foreground, @NotNull Color background) {
        SpecialLabel label = createSpecialLabel(text, fontSize, x, y);
        label.setSize(size);
        label.setForeground(foreground);
        label.setBackground(new Color(background.getRed(), background.getGreen(), background.getBlue(), 200));
        return label;
    }

    public static @Nullable Image loadGUI(String imageName) {
        URL imageURL = Helpers.class.getClassLoader().getResource(imageName);
        return imageURL == null ? null : new ImageIcon(imageURL, imageName).getImage();
    }

    public static void centerFrame(@NotNull Window frame, int width, int height) {
        frame.setSize(width, height);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - frame.getWidth()) / 2;
        int y = (d.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }

    public static void jsonFileChooser(SpecialLabel labelPcDownloadPath) {
        FileDialog fd = new FileDialog((Frame) null, "Select a JSON file", FileDialog.LOAD);
        fd.setFile("*.json");
        fd.setVisible(true);

        String directory = fd.getDirectory();
        String filename = fd.getFile();

        if (filename != null && filename.endsWith(".json")) {
            String configPath = new File(directory, filename).getPath();
            labelPcDownloadPath.setText(configPath);
        }
    }

    public static void pathFolderChooser(SpecialLabel labelPcDownloadPath) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String directory = chooser.getSelectedFile().getPath();
            labelPcDownloadPath.setText(directory);
        }
    }
}