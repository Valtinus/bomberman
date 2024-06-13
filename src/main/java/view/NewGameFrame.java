package view;

import logic.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NewGameFrame class - visual representation of the new game settings
 */
public class NewGameFrame extends JFrame {
    DesignModel designModel = new DesignModel();

    private ImageIcon[] characterIcons = {new ImageIcon("./src/assets/characters/char1_menu.png"), new ImageIcon("./src/assets/characters/char2_menu.png"), new ImageIcon("./src/assets/characters/char3_menu.png"), new ImageIcon("./src/assets/characters/char4_menu.png")};
    private ImageIcon[] mapIcons = {new ImageIcon("./src/assets/maps/map1.png"), new ImageIcon("./src/assets/maps/map2.png"), new ImageIcon("./src/assets/maps/map3.png"), new ImageIcon("./src/assets/maps/map4.png")};
    private String[] characterTexts = {"Character 1\n\n", "Character 2\n\n", "Character 3\n\n", "Character 4\n\n"};
    private int player1Character = 0;
    private int player2Character = 0;

    private String player1Name = "";
    private String player2Name = "";
    private int roundsNum = 0;
    private int mapNum = 0;

    private JTextArea errorText = new JTextArea();

    /**
     * Constructor for the NewGameFrame class
     */
    public NewGameFrame() {
        super("BomberBlitz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(designModel.bgcolor);

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bgImage = new ImageIcon("./src/assets/bg2.png");
                g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(new BorderLayout());
        this.setContentPane(bgPanel);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        JLabel title = new JLabel("BomberBlitz");
        title.setFont(designModel.font.deriveFont(Font.BOLD, 100f));
        title.setForeground(designModel.textColor);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(title, BorderLayout.CENTER);

        JButton backButton = createActionButton("Back");
        backButton.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        backButton.addActionListener(e -> {
            new MenuFrame();
            this.dispose();
        });
        titlePanel.add(backButton, BorderLayout.WEST);

        Dimension backButtonSize = backButton.getPreferredSize();
        Box.Filler filler = new Box.Filler(backButtonSize, backButtonSize, backButtonSize);
        titlePanel.add(filler, BorderLayout.EAST);

        bgPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel playerPanels = new JPanel();
        playerPanels.setLayout(new BorderLayout(0,20));
        playerPanels.setOpaque(false);
        playerPanels.setBackground(designModel.primaryColor);
        playerPanels.setAlignmentY(Component.CENTER_ALIGNMENT);
        playerPanels.setPreferredSize(new Dimension(800, 800));
        bgPanel.add(playerPanels, BorderLayout.LINE_START);

        JPanel player1Container = new JPanel(new FlowLayout());
        player1Container.setOpaque(false);
        JPanel player1Panel = createPlayerPanel(1);
        player1Container.add(player1Panel);
        playerPanels.add(player1Container, BorderLayout.NORTH);

        JPanel player2Container = new JPanel(new FlowLayout());
        player2Container.setOpaque(false);
        JPanel player2Panel = createPlayerPanel(2);
        player2Container.add(player2Panel);
        playerPanels.add(player2Container, BorderLayout.SOUTH);

        JPanel mapContainer = new JPanel(new FlowLayout());
        mapContainer.setOpaque(false);
        JPanel mapPanel = createMapPanel();
        mapContainer.add(mapPanel);
        bgPanel.add(mapContainer, BorderLayout.LINE_END);

        JPanel startButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        startButtonPanel.setOpaque(false);
        startButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,30, 30));
        JButton startButton = createActionButton("Start Game");
        startButtonPanel.add(startButton);
        bgPanel.add(startButtonPanel, BorderLayout.SOUTH);
        startButton.addActionListener(e -> {
            //start game
            if(player1Name.isEmpty() || player2Name.isEmpty() || roundsNum == 0){
                errorText.setText("Please fill in all fields");
            }
            else if(player1Name.equals(player2Name)){
                errorText.setText("Player names must be different");
            }
            else {
                GameSettings settings = new GameSettings(roundsNum, getKeyCodes(), player1Name, player2Name, player1Character + 1, player2Character + 1, mapNum + 1);
                this.dispose();
                new GameFrame(settings);
            }
        });


        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Create a button with the given text (used for back to menu and start game)
     * @param txt text on the button
     * @return the button
     */
    public JButton createActionButton(String txt){
        JButton button = new JButton(txt);
        button.setFont(designModel.font.deriveFont(Font.BOLD, 50f));
        button.setForeground(designModel.textColor);
        button.setBackground(designModel.primaryColor);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 7, 10));
        return button;
    }

    /**
     * Create an arrow button with the given text
     * @param txt < or >
     * @return the button for switching characters and maps
     */
    public JButton createArrowButton(String txt){
        JButton button = new JButton(txt);
        button.setFont(designModel.font.deriveFont(Font.BOLD, 50f));
        button.setForeground(designModel.textColor);
        button.setBackground(new Color(0,0,0,0));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        return button;
    }

    /**
     * Update the character description text
     * @param textArea the object that needs to be updated
     * @param playerID the player that needs to have updated description (ID: 1 or 2)
     */
    public void updateCharacterText(JTextArea textArea, int playerID){
        if(playerID == 1){
            textArea.setText(characterTexts[player1Character]);
        } else if (playerID == 2){
            textArea.setText(characterTexts[player2Character]);
        }
    }

    /**
     * Create a text field with the given text (used for player names and round number)
     * @param txt placeholder text
     * @return the text field
     */
    public JTextField createTextField(String txt){
        JTextField textField = new JTextField(){
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(new Color(59, 24, 95, 200));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        textField.setText(txt);
        textField.setFont(designModel.font.deriveFont(30f));
        textField.setForeground(designModel.textColor);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 4, 4, designModel.primaryColor),
                        BorderFactory.createMatteBorder(4, 4, 0, 0, designModel.darkAccentColor)
                ),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.setAlignmentY(Component.CENTER_ALIGNMENT);
        textField.setOpaque(false);
        textField.setFocusable(false);
        textField.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                textField.setFocusable(true);
                textField.requestFocus();
            }
        });
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(txt)) {
                    textField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(txt);
                }
            }
        });
        return textField;
    }

    /**
     * Create a panel with the character image and the arrows for switching characters
     * @param playerID the player whose character panel we are creating
     * @param playerDataPanel the panel that contains the player's name and character description
     * @return the finished character panel
     */

    public JPanel createCharacterPanel( int playerID, JPanel playerDataPanel){
        JPanel characterPanel = new JPanel();
        characterPanel.setLayout(new BorderLayout(20,0));
        characterPanel.setOpaque(false);

        JLabel characterImage = new JLabel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = null;
                if(playerID == 1){
                    img = characterIcons[player1Character].getImage();;
                } else if (playerID == 2){
                    img = characterIcons[player2Character].getImage();
                }

                double aspectRatio = (double) img.getWidth(null) / img.getHeight(null);

                int height = getHeight();
                int width = (int) (height * aspectRatio);

                if (width > getWidth()) {
                    width = getWidth();
                    height = (int) (width / aspectRatio);
                }

                g.drawImage(img, 0, 0, width, height, this);
            }
        };
        characterImage.setPreferredSize(new Dimension(256, 256));
        characterImage.setHorizontalAlignment(SwingConstants.CENTER);
        characterImage.setVerticalAlignment(SwingConstants.CENTER);
        characterImage.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, designModel.darkAccentColor));
        characterPanel.add(characterImage, BorderLayout.CENTER);

        JButton leftArrow = createArrowButton("<");
        leftArrow.addActionListener(e -> {
            if (playerID == 1) {
                if(player1Character == 0){
                    player1Character = characterIcons.length - 1;
                } else {
                    player1Character--;
                }
            } else if (playerID == 2){
                if(player2Character == 0){
                    player2Character = characterIcons.length - 1;
                } else {
                    player2Character--;
                }
            }
            characterImage.repaint();
            updateCharacterText((JTextArea) playerDataPanel.getComponent(1), playerID);
            playerDataPanel.repaint();
        });
        characterPanel.add(leftArrow, BorderLayout.LINE_START);


        JButton rightArrow = createArrowButton(">");
        rightArrow.addActionListener(e -> {
            if (playerID == 1) {
                if(player1Character == characterIcons.length - 1){
                    player1Character = 0;
                } else {
                    player1Character++;
                }
            } else if (playerID == 2){
                if(player2Character == characterIcons.length - 1){
                    player2Character = 0;
                } else {
                    player2Character++;
                }
            }
            characterImage.repaint();
            updateCharacterText((JTextArea) playerDataPanel.getComponent(1), playerID);
            playerDataPanel.repaint();
        });
        characterPanel.add(rightArrow, BorderLayout.LINE_END);

        return characterPanel;
    }

    /**
     * Read from file
     * @param file the file to read from
     * @return the data from the file in String format
     */
    public String readFile(File file){
        try{
            Scanner scanner = new Scanner(file);
            String data = "";
            while (scanner.hasNextLine()) {
                data += scanner.nextLine() + "\n";
            }
            scanner.close();
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            return "";
        }
    }

    /**
     * Get the controls for one player
     * @param playerId the player whose controls we want to get
     * @return the controls for the player in String format
     */
    public String getOnePlayerControls(int playerId){
        File file = new File("./src/assets/controls.txt");
        String controls = readFile(file);
        String playerControls="";
        if(playerId == 1){
            playerControls = controls.substring(0, controls.indexOf("\n"));
        } else{
            playerControls = controls.substring(controls.indexOf("\n") + 1).replace("\n", "");
        }
        return playerControls;
    }

    /**
     * Extract the key codes for the controls from the file
     * @return the key codes in an array
     */
    public int[] getKeyCodes(){
        int[] keyCodes = new int[12];
        String data = readFile(new File("./src/assets/controls.txt"));
        String[] keys = data.split(",");
        for (int i = 0; i < keys.length; i++){
            String keySubstring = "";
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(keys[i]);
            if (matcher.find()) {
                keySubstring = matcher.group();
            }
            if (!keySubstring.isEmpty()) {
                keyCodes[i] = Integer.parseInt(keySubstring);
            }
        }
        return keyCodes;
    }

    /**
     * Handle the changes in the controls file
     * @param txt the control that is being changed (Up, Down, Left, Right, Bomb, Barrier)
     * @param playerID the player whose controls are being changed
     * @param keyCode the new key code for the control
     */

    public void handleControlsFileChanges(String txt, int playerID, int keyCode){
        try {
            File file = new File("./src/assets/controls.txt");
            String data = readFile(file);
            String usableData=getOnePlayerControls(playerID);
            usableData = usableData.replace(usableData.substring(usableData.indexOf(txt)+txt.length(),usableData.indexOf(",",usableData.indexOf(txt))), keyCode + " ");

            PrintWriter writer = new PrintWriter(file);
            if (playerID == 1){
                writer.print(usableData + "\n" + data.substring(data.indexOf("\n") + 1));
            } else{
                writer.print(data.substring(0, data.indexOf("\n")) + "\n" + usableData);
            }
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    /**
     * Get the text for the controls of a player
     * @param controls the controls for the player
     * @param txt the control that we want to get the text for
     * @return
     */
    public String controlText(String controls, String txt){
        String control = "";
        String[] keys = controls.split(",");
        for(int i = 0; i < keys.length; i++){
            if(keys[i].contains(txt)){
                String keySubstring = "";
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(keys[i]);
                if (matcher.find()) {
                    keySubstring = matcher.group();
                }
                if (!keySubstring.isEmpty()) {
                    control = KeyEvent.getKeyText(Integer.parseInt(keySubstring));
                }
            }
        }
        return control;
    }

    /**
     * Create a panel with one functions control settings (Up, Down, Left, Right, Bomb, Barrier)
     * @param txt the control that the panel is for
     * @param playerId the player whose controls are being set
     * @return the panel with one control settings
     */
    public JPanel createButtonLabel(String txt, int playerId){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.setPreferredSize(new Dimension(80, 80));

        JLabel label = new JLabel(txt);
        label.setFont(designModel.font.deriveFont(20f));
        label.setForeground(designModel.textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        File file = new File("./src/assets/controls.txt");
        String playerControls= getOnePlayerControls(playerId);

        JLabel keyText = new JLabel();
        keyText.setMaximumSize(new Dimension(90, 40));
        keyText.setMinimumSize(new Dimension(80, 40));
        keyText.setPreferredSize(new Dimension(80, 40));
        keyText.setFont(designModel.font.deriveFont(22f));
        keyText.setForeground(designModel.textColor);
        keyText.setBackground(designModel.secondaryColor);
        keyText.setAlignmentX(Component.CENTER_ALIGNMENT);
        keyText.setAlignmentY(Component.CENTER_ALIGNMENT);
        keyText.setHorizontalAlignment(SwingConstants.CENTER);
        keyText.setText(controlText(playerControls, txt));
        keyText.setOpaque(false);
        keyText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 4, 4, designModel.primaryColor),
                        BorderFactory.createMatteBorder(4, 4, 0, 0, designModel.darkAccentColor)
                ),
                BorderFactory.createEmptyBorder(10, 15, 0, 15)
        ));
        panel.add(keyText);
        keyText.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                keyText.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 4, 4, designModel.primaryColor),
                                BorderFactory.createMatteBorder(4, 4, 0, 0, designModel.primaryColor)
                        ),
                        BorderFactory.createEmptyBorder(10, 15, 0, 15)
                ));
                KeyAdapter keyAdapter = new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if(!readFile(file).contains(""+e.getExtendedKeyCode())){
                            handleControlsFileChanges(txt, playerId,e.getExtendedKeyCode());
                            keyText.setText(controlText(getOnePlayerControls(playerId), txt));
                            keyText.repaint();
                        }
                        else{
                            System.out.println("Key already in use");
                        }
                        keyText.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createMatteBorder(0, 0, 4, 4, designModel.primaryColor),
                                        BorderFactory.createMatteBorder(4, 4, 0, 0, designModel.darkAccentColor)
                                ),
                                BorderFactory.createEmptyBorder(10, 15, 0, 15)
                        ));
                        keyText.getParent().removeKeyListener(this);
                    }
                };
                keyText.getParent().addKeyListener(keyAdapter);
                keyText.getParent().requestFocusInWindow();
            }
        });


        return panel;
    }

    /**
     * Create a popup menu for the control settings
     * @param playerID the player whose controls are being set
     * @return the popup menu as a JDialog
     */
    public JDialog popupSettingsMenu(int playerID){
        JDialog settingsDialog = new JDialog(this, "Control Settings", true);
        settingsDialog.setLayout(new BorderLayout());
        settingsDialog.setPreferredSize(new Dimension(500, 300));
        settingsDialog.setUndecorated(true);
        settingsDialog.setResizable(false);
        settingsDialog.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(designModel.darkAccentColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(designModel.bgcolor);
                g.fillRect(5, 5, getWidth() - 10, getHeight() - 10);
            }
        });

        JLabel settingsLabel = new JLabel("Control Settings");
        settingsLabel.setFont(designModel.font.deriveFont(30f));
        settingsLabel.setForeground(designModel.textColor);
        settingsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        settingsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        settingsDialog.add(settingsLabel, BorderLayout.NORTH);

        JPanel firstRow = new JPanel();
        firstRow.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        firstRow.setOpaque(false);
        settingsDialog.add(firstRow, BorderLayout.CENTER);

        JPanel secondRow = new JPanel();
        secondRow.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        secondRow.setOpaque(false);
        settingsDialog.add(secondRow, BorderLayout.CENTER);

        JPanel up = createButtonLabel("Up", playerID);
        JPanel down = createButtonLabel("Down", playerID);
        JPanel left = createButtonLabel("Left", playerID);
        JPanel right = createButtonLabel("Right", playerID);
        JPanel bomb = createButtonLabel("Bomb", playerID);
        JPanel barrier = createButtonLabel("Barrier", playerID);
        firstRow.add(up);
        firstRow.add(down);
        firstRow.add(left);
        firstRow.add(right);
        secondRow.add(bomb);
        secondRow.add(barrier);

        JButton saveButton = new JButton("OK");
        saveButton.setFont(designModel.font.deriveFont(30f));
        saveButton.setForeground(designModel.textColor);
        saveButton.setBackground(designModel.secondaryColor);
        saveButton.setOpaque(false);
        saveButton.setBorderPainted(false);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        saveButton.addActionListener(e -> {
            settingsDialog.dispose();
        });
        settingsDialog.add(saveButton, BorderLayout.SOUTH);



        return settingsDialog;
    }

    /**
     * Create a panel with the player's name and character description
     * @param playerID the player whose data panel we are creating
     * @return the finished data panel for the player
     */
    public JPanel dataPanel(int playerID){
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BorderLayout(0, 20));
        dataPanel.setOpaque(false);

        JPanel firstLineWrapper = new JPanel();
        firstLineWrapper.setLayout(new BoxLayout(firstLineWrapper, BoxLayout.X_AXIS));
        firstLineWrapper.setOpaque(false);
        dataPanel.add(firstLineWrapper, BorderLayout.NORTH);

        JTextField name = createTextField("Player Name");
        name.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (playerID == 1) {
                    player1Name = name.getText();
                } else if (playerID == 2) {
                    player2Name = name.getText();
                }
            }
        });
        firstLineWrapper.add(name);


        ImageIcon gear = new ImageIcon("././src/assets/icons/gear_dark.png");
        gear = new ImageIcon(gear.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JButton settingsButton = new JButton();
        settingsButton.setIcon(gear);
        settingsButton.setFont(designModel.font.deriveFont(30f));
        settingsButton.setForeground(designModel.bgcolor);
        settingsButton.setBackground(designModel.secondaryColor);
        settingsButton.setOpaque(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        settingsButton.addActionListener(e -> {
            JDialog popup = popupSettingsMenu(playerID);
            popup.pack();
            Point location = settingsButton.getLocationOnScreen();
            popup.setLocation(location);
            popup.setVisible(true);
        });
        firstLineWrapper.add(settingsButton);

        JTextArea characterText = new JTextArea();
        updateCharacterText(characterText, playerID);
        characterText.setFont(designModel.font.deriveFont(Font.BOLD,30f));
        characterText.setForeground(designModel.bgcolor);
        characterText.setAlignmentX(SwingConstants.CENTER);
        characterText.setOpaque(false);
        characterText.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        characterText.setLineWrap(true);
        characterText.setWrapStyleWord(true);
        characterText.setEditable(false);
        dataPanel.add(characterText, BorderLayout.CENTER);


        return dataPanel;
    }

    /**
     * Create a panel with the map image and the arrows for switching maps
     * @return the finished map select panel
     */
    public JPanel createMapSelectPanel(){
        JPanel mapSelectPanel = new JPanel();
        mapSelectPanel.setLayout(new BorderLayout(20,0));
        mapSelectPanel.setOpaque(false);
        mapSelectPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        JLabel mapImage = new JLabel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = null;
                img = mapIcons[mapNum].getImage();

                double aspectRatio = (double) img.getWidth(null) / img.getHeight(null);

                int height = getHeight();
                int width = (int) (height * aspectRatio);

                if (width > getWidth()) {
                    width = getWidth();
                    height = (int) (width / aspectRatio);
                }

                g.drawImage(img, 0, 0, width, height, this);
            }
        };
        mapImage.setPreferredSize(new Dimension(256, 256));
        mapImage.setHorizontalAlignment(SwingConstants.CENTER);
        mapImage.setVerticalAlignment(SwingConstants.CENTER);
        mapImage.setBorder(BorderFactory.createMatteBorder(0, 0, 7, 7, designModel.darkAccentColor));
        mapSelectPanel.add(mapImage, BorderLayout.CENTER);

        JButton leftArrow = createArrowButton("<");
        leftArrow.addActionListener(e -> {
            if(mapNum == 0){
                mapNum = mapIcons.length - 1;
            } else {
                mapNum--;
            }
            mapImage.repaint();
        });
        mapSelectPanel.add(leftArrow, BorderLayout.LINE_START);


        JButton rightArrow = createArrowButton(">");
        rightArrow.addActionListener(e -> {
            if(mapNum == characterIcons.length - 1){
                mapNum = 0;
            } else {
                mapNum++;
            }
            mapImage.repaint();
        });
        mapSelectPanel.add(rightArrow, BorderLayout.LINE_END);

        return mapSelectPanel;
    }

    /**
     * Create a panel with the player's data and character panel
     * @param playerID the player whose panel we are creating
     * @return the finished player panel
     */
    public JPanel createPlayerPanel(int playerID){
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BorderLayout(30,30));
        playerPanel.setOpaque(false);
        playerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel playerDataPanel = dataPanel(playerID);
        playerPanel.add(playerDataPanel, BorderLayout.LINE_END);

        JPanel playerCharacterPanel = createCharacterPanel(playerID, playerDataPanel);
        playerPanel.add(playerCharacterPanel, BorderLayout.LINE_START);


        return playerPanel;
    }

    /**
     * Create a panel with the map selection and the round number input
     * @return the finished map panel
     */
    public JPanel createMapPanel(){
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new BorderLayout());
        mapPanel.setOpaque(false);
        mapPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JTextField roundsText = createTextField("Rounds");
        roundsText.setAlignmentX(Component.CENTER_ALIGNMENT);
        roundsText.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c)) {
                    roundsNum = Integer.parseInt(roundsText.getText() + c);
                } else {
                    e.consume();  // Ignore non-digit input
                }
            }
        });
        centerPanel.add(roundsText);

        JPanel mapSelectPanel = createMapSelectPanel();
        centerPanel.add(mapSelectPanel);

        mapPanel.add(centerPanel, BorderLayout.NORTH);


        // Create a vertical filler and add it to the mapPanel
        Dimension minSize = new Dimension(0, 150);
        Dimension prefSize = new Dimension(0, 150);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
        mapPanel.add(new Box.Filler(minSize, prefSize, maxSize), BorderLayout.CENTER);

        errorText.setText("");
        errorText.setFont(designModel.font.deriveFont(Font.BOLD,30f));
        errorText.setForeground(designModel.errorColor);
        errorText.setBackground(designModel.bgcolor);
        errorText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        errorText.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorText.setAlignmentY(Component.CENTER_ALIGNMENT);
        errorText.setOpaque(false);
        errorText.setLineWrap(true);
        errorText.setWrapStyleWord(true);
        errorText.setEditable(false);
        mapPanel.add(errorText, BorderLayout.SOUTH);

        return mapPanel;
    }
}
