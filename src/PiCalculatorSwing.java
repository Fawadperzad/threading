import javax.swing.*;
import java.awt.*;

public class PiCalculatorSwing extends JFrame {
    private JButton startButton;
    private JButton stopButton;
    private JProgressBar progressBar;
    private JLabel resultLabel;
    private JLabel statusLabel;
    private JLabel timeLabel;
    private JCheckBox threadCheckBox;
    private JTextArea logArea;

    private double pi = 0.0;
    private boolean calculating = false;
    private long startTime;
    private Thread calculationThread;

    public PiCalculatorSwing() {
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("PI Calculator - Thread vs. No Thread Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel - Controls
        JPanel controlPanel = new JPanel(new FlowLayout());

        threadCheckBox = new JCheckBox("Mit Thread (GUI bleibt responsive)", true);
        threadCheckBox.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        startButton = new JButton("PI Berechnung starten");
        startButton.setBackground(new Color(0, 150, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        stopButton = new JButton("Stoppen");
        stopButton.setBackground(new Color(150, 0, 0));
        stopButton.setForeground(Color.WHITE);
        stopButton.setEnabled(false);

        controlPanel.add(threadCheckBox);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        // Center Panel - Progress and Results
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Progress Bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Bereit zum Start");
        centerPanel.add(progressBar);
        centerPanel.add(Box.createVerticalStrut(15)); //Unsichtbares Element für Space

        // Status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Status: "));
        statusLabel = new JLabel("Bereit");
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        statusPanel.add(statusLabel);
        centerPanel.add(statusPanel);

        // Time
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("Zeit: "));
        timeLabel = new JLabel("0 ms");
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        timePanel.add(timeLabel);
        centerPanel.add(timePanel);

        centerPanel.add(Box.createVerticalStrut(10));

        // Results
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultPanel.add(new JLabel("Berechnetes PI: "));
        resultLabel = new JLabel("0.0000000000");
        resultLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        resultLabel.setForeground(new Color(0, 0, 150));
        resultPanel.add(resultLabel);
        centerPanel.add(resultPanel);

        JPanel realPiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        realPiPanel.add(new JLabel("Echtes PI: "));
        JLabel realPiLabel = new JLabel(String.format("%.10f", Math.PI));
        realPiLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        realPiLabel.setForeground(new Color(150, 0, 0));
        realPiPanel.add(realPiLabel);
        centerPanel.add(realPiPanel);

        // Bottom Panel - Log Area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Ausgabe Log"));

        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        logArea.setBackground(new Color(248, 248, 248));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to frame
        add(controlPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event Listeners
        startButton.addActionListener(e -> startCalculation());
        stopButton.addActionListener(e -> stopCalculation());

        pack();
        setLocationRelativeTo(null);
    }

    private void startCalculation() {
        if(calculating) return;
        calculating = true;
        progressBar.setValue(0);
        progressBar.setString("Berechnung läuft ...");
        pi = 0;
        resultLabel.setText("0.0000");
        startTime = System.currentTimeMillis();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        boolean useThreads = threadCheckBox.isSelected();

        if(useThreads) {
            calculationThread = new Thread(() -> {
                pi = berechnePi();
                calculationFinished();
            });
            calculationThread.start();
        } else {
            pi = berechnePi();
            calculationFinished();
        }
    }

    private void calculationFinished() {
        calculating = false;
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        //Wenn eigener Thread auf GUI zugreift -> invokeLater
        SwingUtilities.invokeLater(() -> {
            resultLabel.setText(String.format("%.10f", pi));
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            progressBar.setValue(100);
            progressBar.setString("Berechnung abgeschlossen");
            logArea.append(String.format("%d ms%n", duration));
        });
    }

    private void stopCalculation() {
        if(!calculating) return;
        calculating = false;
        //join() wartet bis Thread fertig
        //stop(), suspend() veraltet
        if(calculationThread != null && calculationThread.isAlive()) {
            calculationThread.interrupt(); //Thread stoppen
        }
        calculationFinished();
    }

    /**
     * Berechnet PI mit der Leibniz-Formel und zeigt Progress
     */
    private double berechnePi() {
        double summe = 0.0;
        long iterationen = 200_000_000L; // 200 Millionen für Demo (weniger für GUI)

        for (long i = 0; i < iterationen && calculating; i++) {
            double term = Math.pow(-1, i) / (2 * i + 1);
            summe += term;

            // Progress Update alle 10 Millionen Iterationen
            if (i % 10_000_000 == 0 && i > 0) {
                final int progress = (int) (i * 100 / iterationen);
                final long currentI = i;

                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(progress);
                });
            }
        }

        return 4.0 * summe;
    }

    public static void main(String[] args) {
        //Swing ist nicht threadsicher, deshalb sollte jeder Zugriff von Threads auf GUI im
        //EDT-Thread liegen (Event-Dispatch-Thread). Das macht invokeLater.
        SwingUtilities.invokeLater(() -> {
            new PiCalculatorSwing().setVisible(true);
        });
    }
}
