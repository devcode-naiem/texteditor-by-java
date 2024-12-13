import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class TextEditor implements ActionListener {

    // Declaration of Class Variables
    JFrame f;
    JMenuBar menuBar;
    JMenu file, edit, font, themes, help;
    JTextArea textArea;
    JScrollPane scroll;
    JMenuItem darkTheme, moonLightTheme, defaultTheme, save, open, close, cut, copy, paste, New, selectAll, fontColor, fontSize;
    JLabel fontLabel, fontColorLabel, fontfamily;
    JSpinner fontSizeSpinner;
    JButton ColorPick;
    JComboBox<String> fontBox;

    // Constructor
    TextEditor() {
        initializeFrame();
        initializeMenuBar();
        initializeSubMenus();
        initializeTextAreaAndScrollPane();
        addEventListeners();
        addWindowListeners();
        setDefaultFrameOperations();
    }

    // Main Method
    public static void main(String[] args) {
        new TextEditor();
    }

    private void initializeFrame() {
        f = new JFrame("Naiem's Text Editor");
        Image img = Toolkit.getDefaultToolkit().getImage("src\\ruet.png");
        f.setIconImage(img);
    }
      

    private void initializeMenuBar() {
        menuBar = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        themes = new JMenu("Themes");
        
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(themes);
      
        f.setJMenuBar(menuBar);
        initializeFontSizeSpinnerAndFontBox();
        
    }

    private void initializeFontSizeSpinnerAndFontBox() {
        fontLabel = new JLabel("Font Size: ");
        menuBar.add(fontLabel);
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 20));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(e -> textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue())));
        menuBar.add(fontSizeSpinner);

        fontColorLabel = new JLabel("Font Color: ");
        menuBar.add(fontColorLabel);
        ColorPick = new JButton("Color");
        menuBar.add(ColorPick);

        fontfamily = new JLabel("Font Family: ");
        menuBar.add(fontfamily);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox<>(fonts);
        SwingUtilities.invokeLater(() -> fontBox.setSelectedItem("Arial"));
        menuBar.add(fontBox);
    }

   

    private void initializeSubMenus() {
        open = new JMenuItem("Open");
        New = new JMenuItem("New");
        save = new JMenuItem("Save");
        close = new JMenuItem("Exit");
        file.add(open);
        file.add(New);
        file.add(save);
        file.add(close);

        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        selectAll = new JMenuItem("Select all");
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(selectAll);

        darkTheme = new JMenuItem("Dark Theme");
        moonLightTheme = new JMenuItem("Moonlight Theme");
        defaultTheme = new JMenuItem("Default Theme");
        themes.add(darkTheme);
        themes.add(moonLightTheme);
        themes.add(defaultTheme);
    }

    private void initializeTextAreaAndScrollPane() {
        textArea = new JTextArea(32, 88);
        textArea.setLineWrap(true);  // Enable line wrapping
        textArea.setWrapStyleWord(true);  // Set wrap style to word
        f.add(textArea);
        scroll = new JScrollPane(textArea);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        f.add(scroll);
    }

    private void addEventListeners() {
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectAll.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);
        New.addActionListener(this);
        darkTheme.addActionListener(this);
        moonLightTheme.addActionListener(this);
        defaultTheme.addActionListener(this);
        close.addActionListener(this);
        ColorPick.addActionListener(this);
        fontBox.addActionListener(this);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) saveTheFile();
            }
        });
    }

    private void addWindowListeners() {
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmExit = JOptionPane.showConfirmDialog(f, "Do you want to exit?", "Confirm Before Saving...", JOptionPane.YES_NO_OPTION);
                if (confirmExit == JOptionPane.YES_OPTION) f.dispose();
                else if (confirmExit == JOptionPane.NO_OPTION)
                    f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });
    }

    private void setDefaultFrameOperations() {
        f.setSize(1000, 596);
        f.setResizable(false);
        f.setLocation(250, 100);
        f.setLayout(new FlowLayout());
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleActions(e);
    }

    private void handleActions(ActionEvent e) {
        if (e.getSource() == cut) textArea.cut();
        if (e.getSource() == copy) textArea.copy();
        if (e.getSource() == paste) textArea.paste();
        if (e.getSource() == selectAll) textArea.selectAll();

        if (e.getSource() == fontSize) changeFontSize();
        if (e.getSource() == ColorPick) changeFontColor();
        if (e.getSource() == fontBox) changeFontFamily();

        if (e.getSource() == open) openFile();
        if (e.getSource() == save) saveTheFile();
        if (e.getSource() == New) textArea.setText("");
        if (e.getSource() == close) System.exit(1);

        changeTheme(e);
    }

    private void changeFontSize() {
        String sizeOfFont = JOptionPane.showInputDialog(f, "Enter Font Size");
        if (sizeOfFont != null) {
            int convertSizeOfFont = Integer.parseInt(sizeOfFont);
            textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, convertSizeOfFont));
        }
    }

    private void changeFontColor() {
        Color color = JColorChooser.showDialog(null, "Choose a color", Color.black);
        textArea.setForeground(color);
    }

    private void changeFontFamily() {
    	
    
        textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
    }

    private void openFile() {
        JFileChooser chooseFile = new JFileChooser();
        int i = chooseFile.showOpenDialog(f);
        if (i == JFileChooser.APPROVE_OPTION) {
            File file = chooseFile.getSelectedFile();
            String filePath = file.getPath();
            f.setTitle(file.getName());
            try {
                BufferedReader readFile = new BufferedReader(new FileReader(filePath));
                StringBuilder tempString2 = new StringBuilder();
                String tempString1;
                while ((tempString1 = readFile.readLine()) != null) tempString2.append(tempString1).append("\n");
                textArea.setText(tempString2.toString());
                readFile.close();
            } catch (Exception ae) {
                ae.printStackTrace();
            }
        }
    }

    private void changeTheme(ActionEvent e) {
        if (e.getSource() == darkTheme) {
            textArea.setBackground(Color.DARK_GRAY);
            textArea.setForeground(Color.WHITE);
        }
        if (e.getSource() == moonLightTheme) {
            textArea.setBackground(new Color(107, 169, 255));
            textArea.setForeground(Color.black);
        }
        if (e.getSource() == defaultTheme) {
            textArea.setBackground(new Color(255, 255, 255));
            textArea.setForeground(Color.black);
        }
    }

    private void saveTheFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int response = fileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file;
            PrintWriter fileOut = null;
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                fileOut = new PrintWriter(file);
                fileOut.println(textArea.getText());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } finally {
                if (fileOut != null) fileOut.close();
            }
        }
    }
}
