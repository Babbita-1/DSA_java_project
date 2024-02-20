import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

public class ImageDownloaderApp {

    private JFrame frame;
    private JTextField urlField;
    private JButton downloadButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton cancelButton;
    private JPanel topPanel;
    private JPanel controlPanel;
    private JPanel imagePanel;
    private JScrollPane imageScrollPane;

    // Executor service for managing download tasks
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    // ReentrantLock to synchronize access to the shared imagePanel
    private final ReentrantLock panelLock = new ReentrantLock();

    // Timer for gradual progress bar updates
    private Timer progressBarTimer;

    // Progress bar for tracking the download progress
    private JProgressBar progressBar;

    // Flag to indicate whether the download is paused
    private volatile boolean isPaused = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the look and feel to the system's default
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Create an instance of the ImageDownloaderApp and initialize the GUI
            new ImageDownloaderApp().initialize();
        });
    }

    // Initialize the GUI components
    private void initialize() {
        // Create the main JFrame
        frame = new JFrame("Image Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Create text field, download button, and panels
        urlField = new JTextField(20);
        downloadButton = new JButton("Download");
        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");
        cancelButton = new JButton("Cancel");
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel = new JPanel(new GridLayout(0, 1)); // Use GridLayout with 0 rows for dynamic content
        imageScrollPane = new JScrollPane(imagePanel);

        // Add text field and button to the top panel
        topPanel.add(urlField);
        topPanel.add(downloadButton);

        // Add control buttons to the control panel
        controlPanel.add(pauseButton);
        controlPanel.add(resumeButton);
        controlPanel.add(cancelButton);

        // Add panels to the main frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(imageScrollPane, BorderLayout.CENTER);

        // Initialize control buttons
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false); // Disabled initially
        cancelButton.setEnabled(false);

        // Add action listener to the download button
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the URLs from the text field, split by space
                String[] urls = urlField.getText().split("\\s+");

                if (urls.length > 0) {
                    // Start the asynchronous image download with progress monitoring for each URL
                    for (String url : urls) {
                        if (!url.isEmpty()) {
                            downloadImageAsync(url);
                        }
                    }
                }
            }
        });

        // Add action listeners to control buttons
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseDownload();
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeDownload();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelDownload();
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    // Asynchronously download the image using CompletableFuture with progress monitoring
    private void downloadImageAsync(String imageUrl) {
        // Reset the isPaused flag and create a new progress bar
        isPaused = false;
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // Display completion percentage

        // Create a panel to contain the progress bar
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.add(new JLabel("Downloading: " + imageUrl));
        progressPanel.add(progressBar);

        // Lock access to the shared imagePanel
        panelLock.lock();
        try {
            // Add the progress panel to the main image panel
            imagePanel.add(progressPanel);
            // Ensure the imagePanel is visible in the scroll pane
            imagePanel.revalidate();
            imagePanel.repaint();
        } finally {
            // Unlock access to the shared imagePanel
            panelLock.unlock();
        }

        // Enable control buttons
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false); // Disabled initially
        cancelButton.setEnabled(true);

        // Add listener to the progress bar to update its value gradually
        progressBarTimer = new Timer(100, new ActionListener() {
            private int progress = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused && progress < 100) {
                    progress++;
                    progressBar.setValue(progress);
                }
            }
        });
        progressBarTimer.start();

        CompletableFuture.supplyAsync(() -> {
            try {
                // Download the image from the URL
                URL url = new URL(imageUrl);
                ImageIcon imageIcon = new ImageIcon(url);

                // Simulate a delay for demonstration purposes
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(50);
                }

                return imageIcon;
            } catch (IOException | InterruptedException e) {
                // Handle exceptions (e.g., invalid URL or interrupted download)
                SwingUtilities.invokeLater(() -> {
                    displayErrorMessage("Error downloading image from: " + imageUrl);
                    cancelDownload(); // Cancel download on error
                });
                return null;
            }
        }, executorService)
        .thenAcceptAsync(imageIcon -> {
            // Process the downloaded image on the Swing thread
            SwingUtilities.invokeLater(() -> {
                // Stop the progress bar timer
                progressBarTimer.stop();

                if (imageIcon != null) {
                    // Create a JLabel with the downloaded ImageIcon
                    JLabel imageLabel = new JLabel(imageIcon);

                    // Add the JLabel to the progress panel
                    progressPanel.add(imageLabel);

                    // Update the layout
                    progressPanel.revalidate();
                    progressPanel.repaint();
                }
            });
        })
        .exceptionally(throwable -> {
            // Handle exceptions occurred during the download
            throwable.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                displayErrorMessage("Error downloading image from: " + imageUrl);
                cancelDownload(); // Cancel download on error
            });
            return null;
        })
        .whenComplete((result, throwable) -> {
            // Remove the progress panel once the download is complete or an error occurs
            SwingUtilities.invokeLater(() -> {
                // Lock access to the shared imagePanel
                panelLock.lock();
                try {
                    imagePanel.remove(progressPanel);
                    // Ensure the imagePanel is visible in the scroll pane
                    imagePanel.revalidate();
                    imagePanel.repaint();
                } finally {
                    // Unlock access to the shared imagePanel
                    panelLock.unlock();
                }

                // Disable control buttons after download is complete or canceled
                pauseButton.setEnabled(false);
                resumeButton.setEnabled(false);
                cancelButton.setEnabled(false);
            });
        });
    }

    // Pause the image download
    private void pauseDownload() {
        isPaused = true;
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(true);
    }

    // Resume the paused image download
    private void resumeDownload() {
        isPaused = false;
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
    }

    // Cancel the image download
    private void cancelDownload() {
        isPaused = true;
        progressBarTimer.stop();
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }

    // Display an error message to the user
    private void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public ImageDownloaderApp() {
        // Initialize the application
        initialize();
    }
}