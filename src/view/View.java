package view;


import controller.GameEngine;
import model.Player;
import model.Settings.GAME_DIFFICULTY;
import model.Settings.SERVER_CLIENT_ROLE;
import network.Client;
import network.Server;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration used for giving the pages a unique identifier.
 */
enum PAGENAME{  MENU,
                TOPSCORES,
                TOPSCORESINPUT,
                SINGLEPLAYER,
                MULTIPLAYER ,
                MULTIPLAYERSETTINGS}

/**
 * <h1>Graphical User Interface</h1>
 * This class implements the gui for the game.
 * @author  Gergő Fehér
 * @version 1.0
 * @since   2018-04-25
 */
public class View implements Observer {
    private JFrame window;
    private GameEngine controller;
    private JPanel toplistHandle;
    private Map<PAGENAME, JPanel> pages;
    private PAGENAME currentPage;

//  Constants
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 900;
    public static final int WINDOW_X_POS = 10;
    public static final int WINDOW_Y_POS = 10;
    /**
     * The constructor of the GUI. It creates all the pages, sets the layout as well as all the listeners for the
     * buttons. At last it opens the main page.
     */
    public View() {
        window = new JFrame();

        window.setBounds(WINDOW_X_POS, WINDOW_Y_POS, WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setTitle("Alkesz");
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setLayout(new GridBagLayout());

        this.controller = new GameEngine();
        this.controller.addObserver(this);
        currentPage=PAGENAME.MENU;
        pages = createPages();
        showPage(PAGENAME.MENU);
    }


    @Override
    public void update(Subject o) {

        controller.ResetGame();
        controller.setPlay(true);
    }

    /**
     * This method invokes the creatator methods for the different pages and adds them to the application window.
     * @see Map
     * @see PAGENAME
     * @return Map<PAGENAME, JPanel> This returns a PAGENAME to JPanel Map, which is later used for opening the pages.
     */
    private Map<PAGENAME, JPanel> createPages(){
        Map<PAGENAME, JPanel> pages = new HashMap<PAGENAME,JPanel>();

        pages.put(PAGENAME.MENU,createMenuPage());
        pages.put(PAGENAME.TOPSCORES,createTopScoresPage());
        pages.put(PAGENAME.SINGLEPLAYER,createSingleModePage());
        pages.put(PAGENAME.MULTIPLAYER,createMultiModePage());
        pages.put(PAGENAME.MULTIPLAYERSETTINGS,createMultiSettingsPage());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.fill=GridBagConstraints.BOTH;
        c.anchor=GridBagConstraints.NORTH;
        c.weightx=1;
        c.weighty=1;
        for(JPanel page : pages.values()){
            this.window.add(page,c);
        }
        return pages;
    }
    /**
     * This method opens the page given as parameter.
     * @param pageToShow The PAGENAME of the page to show.
     * @see PAGENAME
     */
    private void showPage(PAGENAME pageToShow){
        pages.get(pageToShow).setVisible(true);
        if (pageToShow!=currentPage){
            pages.get(currentPage).setVisible(false);
        }
        currentPage=pageToShow;
    }
    /**
     * This method creates the menu page.
     * @return JPanel The JPanel object of the menu page.
     */
    private JPanel createMenuPage(){
        JPanel page = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        page.setVisible(false);
        page.setLayout(new GridBagLayout ());

        JPanel headerPanel = new JPanel();
        JPanel navigationPanel = new JPanel();
        JPanel difficultyPanel= new JPanel();


        headerPanel.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.insets = new Insets(2, 2, 2, 2);
        JLabel headerLabel = new JLabel("BLACKOUT",JLabel.CENTER );
        headerLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 24));
        headerPanel.add(headerLabel,gbc);

        navigationPanel.setLayout(new GridBagLayout());
        navigationPanel.setBackground(Color.LIGHT_GRAY);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 20, 2);

        JLabel singleplayerLabel = new JLabel("Lonely alcoholic",JLabel.CENTER);
        singleplayerLabel.setFont(new Font("Comic Sans", Font.ITALIC, 16));
        singleplayerLabel.addMouseListener(makeBoldOnHoverListener());
        singleplayerLabel.addMouseListener(openPageOnClickListener(PAGENAME.SINGLEPLAYER));
        singleplayerLabel.addMouseListener(startGameOnClickListener());

        JLabel multiplayerLabel = new JLabel("Drink with buddy",JLabel.CENTER);
        multiplayerLabel.addMouseListener(makeBoldOnHoverListener());
        multiplayerLabel.addMouseListener(openPageOnClickListener(PAGENAME.MULTIPLAYERSETTINGS));

        multiplayerLabel.setFont(new Font("Comic Sans", Font.ITALIC, 16));
        JLabel topscoresLabel = new JLabel("Boozy Hall of Fame",JLabel.CENTER);
        topscoresLabel.addMouseListener(makeBoldOnHoverListener());
        topscoresLabel.addMouseListener(openPageOnClickListener(PAGENAME.TOPSCORES));

        topscoresLabel.setFont(new Font("Comic Sans", Font.ITALIC, 16));
        navigationPanel.add(singleplayerLabel,gbc);
        gbc.gridy++;
        navigationPanel.add(multiplayerLabel,gbc);
        gbc.gridy++;
        navigationPanel.add(topscoresLabel,gbc);

        Map<GAME_DIFFICULTY, JLabel> difficultyLabels = new HashMap<GAME_DIFFICULTY, JLabel>();
        difficultyPanel.setBackground(Color.LIGHT_GRAY);
        difficultyPanel.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;

        JLabel difficultyLabel = new JLabel("Difficulty:",JLabel.CENTER);
        difficultyLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 18));

        JLabel easyLabel = new JLabel("Sober",JLabel.CENTER);
        easyLabel.addMouseListener(setDifficultyListener(GAME_DIFFICULTY.EASY,difficultyLabels));
        easyLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 14));
        difficultyLabels.put(GAME_DIFFICULTY.EASY,easyLabel);

        JLabel mediumLabel = new JLabel("Tipsy",JLabel.CENTER);
        mediumLabel.addMouseListener(setDifficultyListener(GAME_DIFFICULTY.MEDIUM,difficultyLabels));
        mediumLabel.setFont(new Font("Comic Sans", Font.ITALIC, 14));
        difficultyLabels.put(GAME_DIFFICULTY.MEDIUM,mediumLabel);

        JLabel hardLabel = new JLabel("Blackout",JLabel.CENTER);
        hardLabel.addMouseListener(setDifficultyListener(GAME_DIFFICULTY.HARD,difficultyLabels));
        hardLabel.setFont(new Font("Comic Sans", Font.ITALIC, 14));
        difficultyLabels.put(GAME_DIFFICULTY.HARD,hardLabel);


        difficultyPanel.add(difficultyLabel,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 2, 10);
        gbc.gridwidth = 1;
        gbc.gridy++;
        difficultyPanel.add(easyLabel,gbc);
        gbc.gridx++;
        difficultyPanel.add(mediumLabel,gbc);
        gbc.gridx++;
        difficultyPanel.add(hardLabel,gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.weighty=1;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.BOTH;
        page.add(headerPanel,gbc);
        gbc.gridy++;
        gbc.weighty=3;
        page.add(navigationPanel,gbc);
        gbc.gridy++;
        gbc.weighty=2;
        page.add(difficultyPanel,gbc);

        return page;
    }
    /**
     * This method creates the topscores page.
     * @return JPanel The JPanel object of the topscores page.
     */
    private JPanel createTopScoresPage(){
        JPanel page = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        page.setVisible(false);
        page.setLayout(new GridBagLayout ());

//        Panels
        JPanel headerPanel = new JPanel();

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.weighty=0.5;
        gbc.insets= new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.NORTH;
        page.add(headerPanel,gbc);


        headerPanel.setLayout(new GridBagLayout());

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets= new Insets(2, 2, 2, 2);
        JLabel backLabel = new JLabel("< Back",JLabel.LEFT);
        backLabel.setFont(new Font("Comic Sans", Font.ITALIC, 16));
        backLabel.addMouseListener(makeBoldOnHoverListener());
        backLabel.addMouseListener(openPageOnClickListener(PAGENAME.MENU));
        JLabel headerLabel = new JLabel("Top scores",JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 24));
        headerPanel.add(backLabel,gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx=3;
        headerPanel.add(headerLabel,gbc);
        gbc.gridx++;
        gbc.weightx=1.5;
        headerPanel.add(new JLabel("",JLabel.CENTER),gbc);
        headerPanel.setBackground(Color.RED);
//
        JPanel toplistPanel = new JPanel();
        toplistPanel.setLayout(new GridBagLayout());
        this.toplistHandle = toplistPanel;
        refreshToplist();
        gbc.gridy=1;
        gbc.gridx=0;
        page.add(toplistPanel,gbc);

        return page;
    }
    /**
     * This method creates the multiplayer settings page.
     * @return JPanel The JPanel object of the multiplayer settings page.
     */
    private JPanel createMultiSettingsPage(){
        JPanel page = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        page.setVisible(false);
        page.setLayout(new GridBagLayout ());

        JPanel headerPanel = new JPanel();
        JPanel rolePanel = new JPanel();
        JPanel addressPanel= new JPanel();
        JPanel connectPanel= new JPanel();

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.weighty=0.5;
        gbc.insets= new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.NORTH;
        page.add(headerPanel,gbc);
        gbc.gridy++;
        gbc.weighty=1.5;
        page.add(rolePanel,gbc);
        gbc.gridy++;
        gbc.weighty=1;
        page.add(addressPanel,gbc);
        gbc.gridy++;
        gbc.weighty=2;
        page.add(connectPanel,gbc);

        headerPanel.setLayout(new GridBagLayout());

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets= new Insets(2, 2, 2, 2);
        JLabel backLabel = new JLabel("< Back",JLabel.LEFT);
        backLabel.setFont(new Font("Comic Sans", Font.ITALIC, 16));
        backLabel.addMouseListener(makeBoldOnHoverListener());
        backLabel.addMouseListener(openPageOnClickListener(PAGENAME.MENU));
        JLabel headerLabel = new JLabel("Multipayer settings",JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 24));
        headerPanel.add(backLabel,gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx=3;
        headerPanel.add(headerLabel,gbc);
        gbc.gridx++;
        gbc.weightx=1.5;
        headerPanel.add(new JLabel("",JLabel.CENTER),gbc);
        headerPanel.setBackground(Color.RED);


        Map<SERVER_CLIENT_ROLE,JLabel> roleLabels = new HashMap<SERVER_CLIENT_ROLE,JLabel>();
        rolePanel.setLayout(new GridBagLayout());
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.insets= new Insets(2, 2, 10, 2);
        JLabel multiplayerroleLable = new JLabel("Multiplayer role",JLabel.CENTER);
        multiplayerroleLable.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 18));
        rolePanel.add(multiplayerroleLable,gbc);
        JLabel serverLabel = new JLabel("Server",JLabel.CENTER);
        roleLabels.put(SERVER_CLIENT_ROLE.SERVER,serverLabel);

        serverLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 16));

        gbc.gridy++;
        gbc.insets= new Insets(2, 2, 5, 2);
        rolePanel.add(serverLabel,gbc);
        JLabel clientLabel = new JLabel("Client",JLabel.CENTER);
        roleLabels.put(SERVER_CLIENT_ROLE.CLIENT,clientLabel);

        clientLabel.setFont(new Font("Comic Sans", Font.ITALIC, 16));
        gbc.gridy++;
        rolePanel.add(clientLabel,gbc);
        rolePanel.setBackground(Color.LIGHT_GRAY);



        addressPanel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.insets= new Insets(2, 2, 2, 2);
        JLabel ipLabel = new JLabel("Your IP:",JLabel.CENTER);
        ipLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 18));
        addressPanel.add(ipLabel,gbc);
        gbc.gridy++;
        JLabel ip = new JLabel(controller.settings.getLocalIPAddress(),JLabel.CENTER);
        ip.setFont(new Font("Comic Sans", Font.ITALIC, 16));
        ip.setVisible(true);
        addressPanel.add(ip,gbc);
        addressPanel.setBackground(Color.LIGHT_GRAY);

        JFormattedTextField ipInputField= new JFormattedTextField();
        try {
            MaskFormatter mf = new MaskFormatter("###.###.###.###");
            mf.setPlaceholderCharacter('0');
            ipInputField.setFormatterFactory(new DefaultFormatterFactory(mf));
            ipInputField.setInputVerifier(new IPVerifier());
            ipInputField.setColumns(11);
            ipInputField.setFont(new Font("Comic Sans", Font.ITALIC, 16));
            ipInputField.setVisible(false);
            gbc.gridy++;
            addressPanel.add(ipInputField,gbc);
            clientLabel.addMouseListener(setServerClientRoleListener(SERVER_CLIENT_ROLE.CLIENT,roleLabels,ipLabel,ip,ipInputField));
            serverLabel.addMouseListener(setServerClientRoleListener(SERVER_CLIENT_ROLE.SERVER,roleLabels,ipLabel,ip,ipInputField));
//            TODO ehhez ebből a beírtésvalidált cuccot be kéne írni a remoe_ip settingsbe
        }catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }

        connectPanel.setLayout(new GridBagLayout());
        JButton connectBtn = new JButton("Connect");
        connectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ipInputField.getInputVerifier().verify(ipInputField)){
                    controller.settings.setRemoteIPAddress(ipInputField.getText());
                    System.out.println("Server ip to connect: "+ipInputField.getText());

                    if(controller.settings.getRole() == SERVER_CLIENT_ROLE.CLIENT){

// TODO gombnyomásra indulás

//                        controller.getClient().ConnectToServer(ipInputField.getText());
//                        Thread clientthread = new Thread(controller.getClient());
//                        clientthread.start();


                    }

                    if(controller.settings.getRole() == SERVER_CLIENT_ROLE.SERVER){

//                        controller.getServer().StartServer();
//                        Thread serverthread = new Thread(controller.getServer());
//                        serverthread.start();

                    }

                }
            }
        } );
//        TODO Ide kell még egy gomb a szerver indításhoz
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        connectPanel.add(connectBtn,gbc);
        return page;
    }
    /**
     * This method creates the singleplayer page.
     * @return JPanel The JPanel object of the singleplayer page.
     */
    private JPanel createSingleModePage(){
        JPanel page = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        page.setVisible(false);
        page.setLayout(new GridBagLayout ());

//        Panels
        JPanel headerPanel = new JPanel();
//        TODO Implement the other panels

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.5;
        gbc.insets= new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.NORTH;
        page.add(headerPanel,gbc);


        headerPanel.setLayout(new GridBagLayout());

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets= new Insets(2, 2, 2, 2);
        JLabel backLabel = new JLabel("< Back",JLabel.LEFT);
        backLabel.setFont(new Font("Comic Sans", Font.ITALIC, 16));
        backLabel.addMouseListener(makeBoldOnHoverListener());
        backLabel.addMouseListener(openPageOnClickListener(PAGENAME.MENU));
        backLabel.addMouseListener(stopGameOnClickListener());
        JLabel headerLabel = new JLabel("GAME SCREEN",JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 24));
        headerPanel.add(backLabel,gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx=3;
        headerPanel.add(headerLabel,gbc);
        gbc.gridx++;
        gbc.weightx=1.5;
        headerPanel.add(new JLabel("",JLabel.CENTER),gbc);
        headerPanel.setBackground(Color.RED);

        gbc.fill=GridBagConstraints.BOTH;
        gbc.gridx=0;
        gbc.gridy++;
        gbc.weighty=1;
        gbc.weightx=1;
        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new GridBagLayout());
        page.add(scoresPanel,gbc);

        gbc.gridx=0;
        gbc.gridy++;
        gbc.weighty=23;
        ArenaRenderer arenaPanel =  new ArenaRenderer(this.controller.getArena());
        arenaPanel.addMouseListener(hoverOverArenaListener());
        this.controller.setArenaRenderer(arenaPanel);
        page.add(arenaPanel,gbc);


        JLabel alcoholLevelLabel = new JLabel("Blood alcohol:",JLabel.CENTER);
        alcoholLevelLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 16));
        JLabel missedLabel = new JLabel("Missed:", JLabel.CENTER);
        missedLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 16));
        JLabel scoreLable = new JLabel("Score",JLabel.CENTER);
        scoreLable.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 16));
        JLabel alcoholLevel = new JLabel("xx.xx",JLabel.CENTER);
        alcoholLevel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 16));
        arenaPanel.setAlcoholLevelLabel(alcoholLevel);
        JLabel missed = new JLabel("0",JLabel.CENTER);
        missed.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 16));
        arenaPanel.setMissedLabel(missed);
        JLabel score = new JLabel("0",JLabel.CENTER);
        score.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 16));
        arenaPanel.setScoreLabel(score);

        gbc.fill=GridBagConstraints.BOTH;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weighty=1;
        gbc.weightx=1;
        scoresPanel.add(alcoholLevelLabel,gbc);
        gbc.gridx++;
        scoresPanel.add(missedLabel,gbc);
        gbc.gridx++;
        scoresPanel.add(scoreLable,gbc);
        gbc.gridx=0;
        gbc.gridy++;
        scoresPanel.add(alcoholLevel,gbc);
        gbc.gridx++;
        scoresPanel.add(missed,gbc);
        gbc.gridx++;
        scoresPanel.add(score,gbc);

        return page;
    }
    /**
     * This method creates the multiplayer page.
     * @return JPanel The JPanel object of the multiplayer page.
     */
    private JPanel createMultiModePage(){
        JPanel page = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        page.setVisible(false);
        page.setLayout(new GridBagLayout ());

//        Panels
        JPanel headerPanel = new JPanel();
//        TODO Implement the other panels

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.weighty=0.5;
        gbc.insets= new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.NORTH;
        page.add(headerPanel,gbc);


        headerPanel.setLayout(new GridBagLayout());

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets= new Insets(2, 2, 2, 2);
        JLabel backLabel = new JLabel("< Back",JLabel.LEFT);
        backLabel.setFont(new Font("Comic Sans", Font.ITALIC, 16));
        backLabel.addMouseListener(makeBoldOnHoverListener());
        backLabel.addMouseListener(openPageOnClickListener(PAGENAME.MENU));
        JLabel headerLabel = new JLabel("GAME SCREENS",JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 24));
        headerPanel.add(backLabel,gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx=3;
        headerPanel.add(headerLabel,gbc);
        gbc.gridx++;
        gbc.weightx=1.5;
        headerPanel.add(new JLabel("",JLabel.CENTER),gbc);
        headerPanel.setBackground(Color.RED);
        return page;
    }

    /**
     * This method creates a listener responsible for making a label bold on hover.
     * @return MouseListener The MouseListener object.
     */
    private MouseListener makeBoldOnHoverListener(){
        MouseListener listener = new MouseAdapter() {
            Font original;

            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                e.getComponent().setFont(original.deriveFont(original.getStyle() | Font.BOLD));
                e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
                e.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            }
        };
        return listener;
    }
    /**
     * This method creates a listener that is responsible for opening the page given as a param when the object
     * it is assigned gets clicked.
     * @param pageToShow The page to show upon click
     * @return MouseListener The MouseListener object.
     */
    private MouseListener openPageOnClickListener(PAGENAME pageToShow){
        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPage(pageToShow);
            }
        };
        return listener;
    }
    /**
     * This method creates a listener that is responsible for changing the difficulty setting to the given param,
     * when the object it is assigned gets clicked. It also changes the font of the appropriate label, so it more
     * visible, which is the active setting.
     * @param difficulty The difficulty to set
     * @param difficultyLabels The GAME_DIFFICULTY to JLabel map used for setting the proper font of the labels.
     * @return MouseListener The MouseListener object.
     */
    private MouseListener setDifficultyListener(GAME_DIFFICULTY difficulty, Map<GAME_DIFFICULTY, JLabel> difficultyLabels){
        MouseListener listener = new MouseAdapter() {
            Font font;
            Font original;
            @Override
            public void mouseClicked(MouseEvent e) {
                for (JLabel label: difficultyLabels.values()){
                    font = label.getFont();
                    label.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
                }
                System.out.println("Current difficulty : " + difficulty);
                JLabel label = difficultyLabels.get(difficulty);
                font = label.getFont();
                original=font.deriveFont(font.getStyle() | Font.BOLD);
                label.setFont(original);
                controller.settings.setDifficulty(difficulty);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                e.getComponent().setFont(original.deriveFont(original.getStyle() | Font.BOLD));
                e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
                e.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        return listener;
    }
    /**
     * This method creates a listener that is responsible for changing the server-client role setting to the given param,
     * when the object it is assigned gets clicked. It also changes the font of the appropriate label, so it more
     * visible, which is the active setting as well as it toggles the visibility for the IP address input field.
     * @param role The role to set
     * @param roleLabels The SERVER_CLIENT_ROLE to JLabel map used for setting the proper font of the labels.
     * @param ipLabel
     * @param localIpLabel
     * @param ipInputField
     * @return MouseListener The MouseListener object.
     */
    private MouseListener setServerClientRoleListener(SERVER_CLIENT_ROLE role,Map<SERVER_CLIENT_ROLE, JLabel> roleLabels,JLabel ipLabel,JLabel localIpLabel,JFormattedTextField ipInputField){
        MouseListener listener = new MouseAdapter() {
            Font font;
            Font original;
            @Override
            public void mouseClicked(MouseEvent e) {
                for (JLabel label: roleLabels.values()){
                    font = label.getFont();
                    label.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
                }
                System.out.println("Current role : " + role);
                JLabel label = roleLabels.get(role);
                font = label.getFont();
                original=font.deriveFont(font.getStyle() | Font.BOLD);
                label.setFont(original);
                controller.settings.setRole(role);
                if (role==SERVER_CLIENT_ROLE.SERVER){
                    ipInputField.setVisible(false);
                    localIpLabel.setVisible(true);
                    ipLabel.setText("Your IP");
                }else{
                    ipInputField.setVisible(true);
                    localIpLabel.setVisible(false);
                    ipLabel.setText("Server IP");
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                e.getComponent().setFont(original.deriveFont(original.getStyle() | Font.BOLD));
                e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
                e.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        return listener;
    }
    /**
     * This method creates a listener that is responsible for starting the game once the object it is assigned to is clicked upon.
     * @return MouseListener The MouseListener object.
     */
    private MouseListener startGameOnClickListener(){
        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.setPlay(true);
            }
        };
        return listener;
    }
    /**
     * This method creates a listener that is responsible for stopping the game once the object it is assigned to is clicked upon.
     * @return MouseListener The MouseListener object.
     */
    private MouseListener stopGameOnClickListener(){
        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.setPlay(false);
                controller.ResetGame();
            }
        };
        return listener;
    }
    /**
     * This method creates a listener that is responsible for hiding the cursor if its above the game field.
     * * @return MouseListener The MouseListener object.
     */
    private MouseListener hoverOverArenaListener(){
        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Transparent 16 x 16 pixel cursor image.
                BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

                // Create a new blank cursor.
                Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                                        cursorImg, new Point(0, 0), "blank cursor");
                e.getComponent().setCursor(blankCursor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        return listener;
    }

    /**
     * This method refreshes the toplist page once it is invoked.
     */
    public void refreshToplist(){
        toplistHandle.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy=0;
        gbc.gridx=0;
        gbc.weightx=2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill=GridBagConstraints.HORIZONTAL;

        toplistHandle.add(new JLabel("Rank",JLabel.RIGHT),gbc);
        gbc.gridx++;
        gbc.weightx=3;
        toplistHandle.add(new JLabel("Name",JLabel.CENTER),gbc);
        gbc.gridx++;
        gbc.weightx=2;
        toplistHandle.add(new JLabel("Score",JLabel.LEFT),gbc);
        JLabel [] toplistRanks = new JLabel[5];
        JLabel [] toplistNames = new JLabel[5];
        JLabel [] toplistScores = new JLabel[5];

        gbc.gridy=1;
        gbc.gridx=0;
        gbc.weightx=2;
        gbc.anchor = GridBagConstraints.NORTH;
        int i=0;
        for (JLabel label:toplistRanks){
            i++;
            label = new JLabel(Integer.toString(i)+".",JLabel.RIGHT);
            label.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 24));
            toplistHandle.add(label,gbc);
            gbc.gridy++;
        }
        gbc.gridy=1;
        gbc.gridx=1;
        gbc.weightx=3;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        i=0;
        for (JLabel label:toplistNames){
            label= new JLabel("",JLabel.CENTER);
            if (controller.topList.getItemsInList()>i){
                label.setText(controller.topList.getEntry(i).getName());
            }else {
                label.setText("");
            }

            i++;

            label.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 24));
            toplistHandle.add(label,gbc);
            gbc.gridy++;
        }
        gbc.gridy=1;
        gbc.gridx=2;
        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        i=0;
        for (JLabel label:toplistScores){
            label= new JLabel("",JLabel.LEFT);
            if (controller.topList.getItemsInList()>i){
                label.setText(Integer.toString(controller.topList.getEntry(i).getScore()));
            }else {
                label.setText("");
            }

            i++;

            label.setFont(new Font("Comic Sans", Font.ITALIC+Font.BOLD, 24));
            toplistHandle.add(label,gbc);
            gbc.gridy++;
        }
    }
}

