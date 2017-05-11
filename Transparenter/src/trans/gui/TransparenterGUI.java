package trans.gui;

import static javax.swing.JComponent.setDefaultLocale;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by
 * Daniel Roesch
 * as "Nils Darmstrong".
 * -----------------
 * For "Transparenter",
 * on 25.04.2017, 20:40.
 */
public class TransparenterGUI extends JFrame implements ActionListener {

  private static final int GAP = 15;

  private JPanel mainPanel, infoPanel, buttonPanel, thresholdPanel, colorPanel, imagePanel,
      loadPanel, savePanel, fieldPanel;
  private JTextField saveField, loadField;
  private JLabel infoLabel, saveLabel, loadLabel, imageLabel;
  private JButton loadBrowseButton, saveBrowseButton, goButton, abortButton;
  private JSlider colorSlider;
  private JFileChooser fileChooser;
  private FileNameExtensionFilter filter;
  private BufferedImage displayImage;

  /**
   * constructor method to create a new TransparenterGUI object.
   */
  private TransparenterGUI() {
    super("Transparenter");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLayout(new BorderLayout(GAP, GAP));
    setResizable(true);

    initComponents();
    addComponentsTogether();
    addActionListeners();

    setSize(600, 550);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * method to initialize all the components that are defined in the scope.
   */
  private void initComponents() {
    mainPanel = new JPanel(new BorderLayout(GAP, GAP));
    infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP, GAP));
    buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP, GAP));
    thresholdPanel = new JPanel(new BorderLayout(GAP, GAP));
    colorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP, GAP));
    colorPanel.setBorder(new EtchedBorder(Color.BLACK, Color.LIGHT_GRAY));
    colorPanel.setBackground(Color.WHITE);
    imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP, GAP));
    imagePanel.setPreferredSize(new Dimension(200, 150));
    loadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    fieldPanel = new JPanel(new GridLayout(2, 1));
    saveField = new JTextField(25);
    loadField = new JTextField(25);
    infoLabel = new JLabel("<html>Select an image and a corresponding threshold<br>" +
        "to make the pixels beneath this point transparent.</html>");
    saveLabel = new JLabel("Select a save path for the result:");
    loadLabel = new JLabel("Select an image to process:");
    loadBrowseButton = new JButton("Browse");
    saveBrowseButton = new JButton("Browse");
    goButton = new JButton("Go");
    abortButton = new JButton("Abort");
    colorSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
    fileChooser = new JFileChooser(System.getProperty("user.home") +
        File.separatorChar + "Pictures");
    filter = new FileNameExtensionFilter("Only image files",
        "png", "bmp", "gif", "jpg", "jpeg");
    fileChooser.setFileFilter(filter);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    try {
      displayImage = loadImage(
          "src" + File.separatorChar + "img" + File.separatorChar + "preview.png");
      displayImage = changeImageType(displayImage);
    } catch (IOException e) {
      e.printStackTrace();
    }
    imageLabel = new JLabel(new ImageIcon(displayImage));
  }

  /**
   * method to add all the components together.
   */
  private void addComponentsTogether() {
    this.add(mainPanel, "Center");
    this.add(buttonPanel, "South");
    buttonPanel.add(goButton);
    buttonPanel.add(abortButton);
    mainPanel.add(infoPanel, "North");
    infoPanel.add(infoLabel);
    mainPanel.add(fieldPanel, "South");
    fieldPanel.add(loadPanel, 0);
    loadPanel.add(loadLabel);
    loadPanel.add(loadField);
    loadPanel.add(loadBrowseButton);
    fieldPanel.add(savePanel, 1);
    savePanel.add(saveLabel);
    savePanel.add(saveField);
    savePanel.add(saveBrowseButton);
    mainPanel.add(thresholdPanel, "East");
    thresholdPanel.add(colorSlider, "Center");
    thresholdPanel.add(colorPanel, "South");
    mainPanel.add(imagePanel, "Center");
    imagePanel.add(imageLabel);
  }

  /**
   * method to add all necessary action listeners.
   */
  private void addActionListeners() {
    abortButton.setActionCommand("abort");
    abortButton.addActionListener(this);
    goButton.setActionCommand("go");
    goButton.addActionListener(this);
    saveBrowseButton.setActionCommand("save");
    saveBrowseButton.addActionListener(this);
    loadBrowseButton.setActionCommand("load");
    loadBrowseButton.addActionListener(this);
    colorSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        int userSelection = (int) (255 - (2.55D * colorSlider.getValue()));
        Color userColor = new Color(userSelection, userSelection, userSelection);
        colorPanel.setBackground(userColor);
        displayImage = makeImageTransparent(displayImage, colorSlider.getValue());
        imageLabel = new JLabel(new ImageIcon(displayImage));
        imageLabel.repaint();
        imagePanel.repaint();
      }
    });
  }

  /**
   * method that overrides the actionPerformed method of the ActionListener class.
   * the action commands will be the same as the captions used to create the buttons
   *
   * @param event ActionEvent that lead to the execution of this method.
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    switch (event.getActionCommand()) {
      case "load":
        fileChooser.setDialogTitle("Select an image to make transparent");
        fileChooser.showOpenDialog(null);
        loadField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        imageLabel = new JLabel(new ImageIcon(loadField.getText()));
        imagePanel.revalidate();
        break;
      case "save":
        fileChooser.setDialogTitle("Select a save path for your image");
        fileChooser.showSaveDialog(null);
        saveField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        break;
      case "go":
        try {
          BufferedImage image = loadImage(loadField.getText());
          image = makeImageTransparent(image, colorSlider.getValue());
          if (saveImage(saveField.getText(), image)) {
            JOptionPane.showMessageDialog(null, "Image saved as planned to\n\n"
                + saveField.getText(), "Success!", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(null, "Image could not be saved!",
                "Error!", JOptionPane.ERROR_MESSAGE);
          }
        } catch (IOException ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(null, "Your image could not be loaded!",
              "Error!", JOptionPane.ERROR_MESSAGE);
        }
        break;
      case "abort":
        saveField.setText("");
        loadField.setText("");
        colorSlider.setValue(0);
        colorPanel.setBackground(Color.WHITE);
        imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP, GAP));
        break;
      default:
        break;
    }
  }

  /**
   * method to load an image from a given file path as a buffered image.
   *
   * @param filePath the String file path of the image.
   * @return the BufferedImage.
   * @throws IOException if the image cannot be read.
   */
  private BufferedImage loadImage(String filePath) throws IOException {
    return ImageIO.read(new File(filePath));
  }

  /**
   * method to change an image to the needed INT_ARGB type.
   *
   * @param image the buffered image that needs to be changed.
   * @return the correctly changed image.
   */
  private BufferedImage changeImageType(BufferedImage image) {
    BufferedImage changedImage = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    for (int y = 0; y < changedImage.getHeight(); y++) {
      for (int x = 0; x < changedImage.getWidth(); x++) {
        changedImage.setRGB(x, y, image.getRGB(x, y));
      }
    }
    return changedImage;
  }

  /**
   * method to save a buffered image to a given file path.
   *
   * @param savePath the String save path for the image.
   * @param image the buffered image.
   * @return boolean true if the image was saved correctly, else false.
   * @throws IOException if the image could not be saved.
   */
  private boolean saveImage(String savePath, BufferedImage image) throws IOException {
    if (ImageIO.write(image, "png", new File(savePath))) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * method to create an image that has transparent pixels for every value over a threshold.
   *
   * @param imagePath String file path for the image.
   * @param colorToMakeTransparent the Color that is going to be transparent.
   * @return the partially transparent image.
   */
  private Image makeImageTransparent(String imagePath, Color colorToMakeTransparent) {
    ImageIcon originalIcon = new ImageIcon("src/img/preview.png");

    ImageFilter filter = new RGBImageFilter() {
      int transparentColor = colorToMakeTransparent.getRGB() | 0xFF000000;

      public final int filterRGB(int x, int y, int rgb) {
        if ((rgb | 0xFF000000) >= transparentColor) {
          return 0x00FFFFFF & rgb;
        } else {
          return rgb;
        }
      }
    };

    ImageProducer filteredImgProd = new FilteredImageSource(originalIcon.getImage().getSource(),
        filter);
    return Toolkit.getDefaultToolkit().createImage(filteredImgProd);
  }

  /**
   * method to change the appearance of an image so that every pixel beneath a threshold becomes
   * transparent.
   *
   * @param image the image that needs to be altered.
   * @param threshold the color threshold.
   * @return the transparent buffered image.
   */
  private BufferedImage makeImageTransparent(BufferedImage image, Color threshold) {
    Color pixel;
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        pixel = new Color(image.getRGB(x, y));
        if (isUnderThreshold(pixel, threshold)) {
          pixel = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue(), 0);
          image.setRGB(x, y, pixel.getRGB());
        }
      }
    }
    return image;
  }

  /**
   * method to make the pixels beneath a given threshold value transparent.
   *
   * @param image the image that needs to be transparent.
   * @param threshold the threshold in the range from 0 - 100.
   * @return the transparent buffered image.
   */
  private BufferedImage makeImageTransparent(BufferedImage image, int threshold) {
    float alpha = threshold / 100;
    BufferedImage transparentImg = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) transparentImg.getGraphics();
    g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
    // set the transparency level in range 0.0f - 1.0f
    g2d.drawImage(image, 0, 0, null);
    return transparentImg;
  }

  /**
   * method to check if a given color is beneath another color's threshold.
   *
   * @param color the color that needs to be checked.
   * @param threshold the threshold color.
   * @return boolean true if color <= threshold, false if not.
   */
  private boolean isUnderThreshold(Color color, Color threshold) {
    if (color.getGreen() >= threshold.getGreen() &&
        color.getBlue() >= threshold.getBlue() &&
        color.getRed() >= threshold.getRed()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * main method to get everything going. starts the gui within the designated thread.
   *
   * @param args .
   */
  public static void main(String[] args) {
    try {
      setLookAndFeel(getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException |
        UnsupportedLookAndFeelException | IllegalAccessException e) {
      e.printStackTrace();
    }
    setDefaultLocale(Locale.ENGLISH);
    invokeLater(new Runnable() {
      @Override
      public void run() {
        new TransparenterGUI();
      }
    });
  }

}
