import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.print.PrinterException;
import java.util.HashSet;
import java.util.Set;

class TextEditor implements ActionListener {

    private JFrame f;
    private JMenuBar menuBar;
    private JMenu file, edit, themes;
    private JTextArea textArea;
    private JScrollPane scroll;
    private JMenuItem darkTheme, moonLightTheme, defaultTheme, save, open, close, print;
    private JLabel fontLabel, fontColorLabel, fontfamily;
    private JSpinner fontSizeSpinner;
    private JButton ColorPick;
    private JComboBox<String> fontBox;
    private JCheckBoxMenuItem spellCheckMenuItem;
    private final Set<String> keywords;

    TextEditor() {
        keywords = new HashSet<>();
        initializeKeywords();

        initializeFrame();
        initializeMenuBar();
        initializeSubMenus();
        initializeTextAreaAndScrollPane();
        addEventListeners();
        addWindowListeners();
        setDefaultFrameOperations();
    }a

    private void initializeKeywords() {
        keywords.add("int");
        keywords.add("void");
        keywords.add("public");
        keywords.add("private");
        keywords.add("protected");
        // Add more keywords if needed
    }

    public static void main(String[] args) {
        new TextEditor();
    }

    private void initializeFrame() {
        f = new JFrame("Enhanced Text Editor");
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
        menuBar.add(fontBox);
    }

    private void initializeSubMenus() {
        open = new JMenuItem("Open");
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        file.add(open);

        save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        file.add(save);

        print = new JMenuItem("Print");
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        file.add(print);

        close = new JMenuItem("Exit");
        file.add(close);

        JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        edit.add(cut);

        JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        edit.add(copy);

        JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        edit.add(paste);

        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(e -> textArea.selectAll());
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        edit.add(selectAll);

        spellCheckMenuItem = new JCheckBoxMenuItem("Spell Check");
        spellCheckMenuItem.addActionListener(e -> toggleSpellCheck());
        edit.add(spellCheckMenuItem);

        darkTheme = new JMenuItem("Dark Theme");
        themes.add(darkTheme);

        moonLightTheme = new JMenuItem("Moonlight Theme");
        themes.add(moonLightTheme);

        defaultTheme = new JMenuItem("Default Theme");
        themes.add(defaultTheme);
    }

    private void initializeTextAreaAndScrollPane() {
        textArea = new JTextArea(32, 88);
        textArea.setDocument(new CustomDocument());
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (spellCheckMenuItem.isSelected()) {
                    spellCheck();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (spellCheckMenuItem.isSelected()) {
                    spellCheck();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (spellCheckMenuItem.isSelected()) {
                    spellCheck();
                }
            }
        });

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scroll = new JScrollPane(textArea);
        f.add(scroll);
    }

    private void addEventListeners() {
        open.addActionListener(this);
        save.addActionListener(this);
        print.addActionListener(this);
        darkTheme.addActionListener(this);
        moonLightTheme.addActionListener(this);
        defaultTheme.addActionListener(this);
        close.addActionListener(this);
        ColorPick.addActionListener(this);
        fontBox.addActionListener(this);
    }

    private void addWindowListeners() {
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmExit = JOptionPane.showConfirmDialog(f, "Do you want to exit?", "Confirm Before Saving...", JOptionPane.YES_NO_OPTION);
                if (confirmExit == JOptionPane.YES_OPTION) {
                    f.dispose();
                } else if (confirmExit == JOptionPane.NO_OPTION) {
                    f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
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

    private void toggleSpellCheck() {
        if (spellCheckMenuItem.isSelected()) {
            spellCheck();
        } else {
            // Clear all underlines
            Highlighter highlighter = textArea.getHighlighter();
            highlighter.removeAllHighlights();
        }
    }

    private void spellCheck() {
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();

        String text = textArea.getText();
        String[] words = text.split("\\s"); // Split by whitespace
        int startIndex = 0;

        for (String word : words) {
            if (word.equalsIgnoreCase("mispell")) { // Example of a misspelled word
                try {
                    highlighter.addHighlight(startIndex, startIndex + word.length(), new DefaultHighlighter.DefaultHighlightPainter(Color.RED));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
            startIndex += word.length() + 1; // +1 for the whitespace/space
        }
    }

    private void printText() {
        try {
            textArea.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == open) {
            openFile();
        } else if (e.getSource() == save) {
            saveTheFile();
        } else if (e.getSource() == print) {
            printText();
        } else if (e.getSource() == darkTheme) {
            textArea.setBackground(Color.DARK_GRAY);
            textArea.setForeground(Color.WHITE);
        } else if (e.getSource() == moonLightTheme) {
            textArea.setBackground(new Color(107, 169, 255));
            textArea.setForeground(Color.BLACK);
        } else if (e.getSource() == defaultTheme) {
            textArea.setBackground(Color.WHITE);
            textArea.setForeground(Color.BLACK);
        } else if (e.getSource() == close) {
            System.exit(0);
        } else if (e.getSource() == ColorPick) {
            Color color = JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
            textArea.setForeground(color);
        } else if (e.getSource() == fontBox) {
            textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int choice = fileChooser.showOpenDialog(f);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveTheFile() {
        JFileChooser fileChooser = new JFileChooser();
        int choice = fileChooser.showSaveDialog(f);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                textArea.write(writer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class CustomDocument extends DefaultStyledDocument {
        private final StyleContext cont = StyleContext.getDefaultStyleContext();
        private final AttributeSet attrBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
        private final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);

        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offset, str, a);
            refreshDocument();
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);
            refreshDocument();
        }

        private void refreshDocument() throws BadLocationException {
            String text = getText(0, getLength());
            final Highlighter highlighter = textArea.getHighlighter();
            highlighter.removeAllHighlights();

            for (String word : keywords) {
                int pos = 0;
                while ((pos = text.indexOf(word, pos)) >= 0) {
                    setCharacterAttributes(pos, word.length(), attrBlue, false);
                    pos += word.length();
                }
            }
        }
    }
}
