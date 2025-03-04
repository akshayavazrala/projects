import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieListingApp {

    private static final String[][] MOVIES = {
        {"Devara - Part 1", "8.2 (243.4K votes)", "devara_part1.png",
                "Set in the 1980-90s, Devara: Part 1 revolves around Devara who sets out on a dangerous journey to ensure the safety of his loved ones. " +
                        "Jr NTR is in a dual-role of Devara and Varadha. The film also features Janhvi Kapoor, Saif Ali Khan, Prakash Raj, " +
                        "Shruti Marathe, and Shine Tom Chacko in significant roles."},
        {"Joker: Folie a Deux", "4.8 (5.6K votes)", "joker_folie_a_deux.png",
                "The film begins with a cartoon featuring Arthur Fleck as the Joker, whose shadow acts independently. " +
                        "In a dressing room, the shadow attacks Fleck, steals his costume, locks him in a dresser, and applies the Joker makeup. " +
                        "As he makes his way to the stage, the shadow forces a woman to kiss him."},
        {"Sathyam Sundaram", "8.8 (12.7K votes)", "sathyam_sundaram.png",
                "In 1996, Sathyam and his family are forced to leave their home in Guntur and start afresh in Vizag. Two decades later, Sathyam returns to Guntur for a wedding, " +
                        "where he encounters Sundaram, a spirited individual unusually attentive to him. As events unfold, Sathyam discovers hidden emotions."},
        {"SWAG", "7.5 (7.9K votes)", "swag.png",
                "SWAG is a Telugu romantic comedy entertainer movie directed by Hasith Goli. The movie casts Sree Vishnu, Ritu Varma, Meera Jasmine, " +
                        "and Daksha Nagarkar in the main lead roles."},
        {"Mathu Vadalara 2", "7.9 (8.2K votes)", "mathu_vadalara_2.png",
                "Babu and Yesudas are special officers in the HE Team. They are assigned a kidnapping case that " +
                        "reveals a deeper mystery involving the murder of a young girl named Riya."},
        {"Ramnagar Bunny", "6.9 (4.5K votes)", "ramnagar_bunny.png",
                "Reckless 21-year-old Bunny squanders his father's savings and juggles relationships. After a wake-up call, he faces a tough choice " +
                        "between love and responsibility in this light-hearted, emotional journey."}
    };

    private int currentPage = 0;
    private final int moviesPerPage = 3;
    private Map<String, String> users = new HashMap<>();
    private Map<String, List<String>> bookings = new HashMap<>();
    private String currentUser;
    private List<JCheckBox> allSeats = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieListingApp().showLoginPage());
    }

    private void showLoginPage() {
        JFrame frame = new JFrame("Movie App - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set the frame to full screen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        
        if (gd.isFullScreenSupported()) {
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        // Create a custom panel with background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("front.png");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Create a semi-transparent panel for login components
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(0, 0, 0, 180));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Customize components
        JLabel titleLabel = new JLabel("Movie Booking App", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel usernameLabel = new JLabel("User ID:");
        usernameLabel.setForeground(Color.WHITE);
        JTextField usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        
        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createStyledButton("Register");
        
        // Add components to login panel
        loginPanel.add(titleLabel, gbc);
        gbc.insets = new Insets(20, 5, 5, 5);
        loginPanel.add(usernameLabel, gbc);
        loginPanel.add(usernameField, gbc);
        loginPanel.add(passwordLabel, gbc);
        loginPanel.add(passwordField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.insets = new Insets(15, 5, 5, 5);
        loginPanel.add(buttonPanel, gbc);
        
        // Add login panel to main panel
        mainPanel.add(loginPanel);
        
        // Add action listeners
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (users.containsKey(username) && users.get(username).equals(password)) {
                currentUser = username;
                showProfilePage();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again.");
            }
        });
        
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (!users.containsKey(username)) {
                users.put(username, password);
                currentUser = username;
                showProfilePage();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Username already exists. Please choose another one.");
            }
        });
        
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 35));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }

    private void showProfilePage() {
        JFrame frame = new JFrame("Movie App - Profile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set the frame to full screen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        
        if (gd.isFullScreenSupported()) {
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    
        // Create a custom panel with background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("view.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null); // Use null layout for absolute positioning
        
        // Create a semi-transparent panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(255, 255, 255, 180));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton viewMoviesButton = createStyledButton("View Movies");
        JButton viewHistoryButton = createStyledButton("View History");
        JButton logoutButton = createStyledButton("LogOut");
        
        buttonPanel.add(viewMoviesButton);
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(logoutButton);
        
        // Set size and position of button panel
        buttonPanel.setBounds(frame.getWidth() - 300, (frame.getHeight() - 250) / 2, 250, 250);
        mainPanel.add(buttonPanel);
        
        // Add action listeners
        viewMoviesButton.addActionListener(e -> {
            frame.dispose();
            createAndShowGUI();
        });
    
        viewHistoryButton.addActionListener(e -> {
            frame.dispose();
            showBookingHistory();
        });
    
        logoutButton.addActionListener(e -> {
            currentUser = null;
            frame.dispose();
            showLoginPage();
        });
    
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    
        // Adjust button panel position after frame is visible
        SwingUtilities.invokeLater(() -> {
            buttonPanel.setBounds(frame.getWidth() - 300, (frame.getHeight() - 250) / 2, 250, 250);
            frame.revalidate();
            frame.repaint();
        });
    }    

    private void showBookingHistory() {
        JFrame frame = new JFrame("Movie App - Booking History");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // Set the frame to full screen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        
        if (gd.isFullScreenSupported()) {
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea bookingHistoryArea = new JTextArea();
        bookingHistoryArea.setEditable(false);
        bookingHistoryArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(bookingHistoryArea);

        List<String> userBookings = bookings.get(currentUser);
        if (userBookings == null || userBookings.isEmpty()) {
            bookingHistoryArea.setText("No bookings found.");
        } else {
            StringBuilder history = new StringBuilder("Booking History:\n\n");
            for (String booking : userBookings) {
                history.append(booking).append("\n\n");
            }
            bookingHistoryArea.setText(history.toString());
        }

        JButton backButton = createStyledButton("Back to Profile");
        backButton.addActionListener(e -> {
            frame.dispose();
            showProfilePage();
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Movie Listing App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // Set the frame to full screen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        
        if (gd.isFullScreenSupported()) {
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.Y_AXIS));
        loadMovies(moviePanel, frame);

        JScrollPane scrollPane = new JScrollPane(moviePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel navigationPanel = new JPanel();
        JButton prevButton = createStyledButton("<");
        JButton nextButton = createStyledButton(">");
        JButton backButton = createStyledButton("Back to Profile");

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                loadMovies(moviePanel, frame);
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * moviesPerPage < MOVIES.length) {
                currentPage++;
                loadMovies(moviePanel, frame);
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            showProfilePage();
        });

        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        navigationPanel.add(backButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(navigationPanel, BorderLayout.SOUTH);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void loadMovies(JPanel moviePanel, JFrame frame) {
        moviePanel.removeAll();

        int start = currentPage * moviesPerPage;
        int end = Math.min(start + moviesPerPage, MOVIES.length);

        for (int i = start; i < end; i++) {
            JPanel movieBox = createMovieBox(MOVIES[i], frame);
            moviePanel.add(movieBox);
            moviePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        moviePanel.revalidate();
        moviePanel.repaint();
    }

    private JPanel createMovieBox(String[] movieData, JFrame frame) {
        JPanel movieBox = new JPanel(new BorderLayout());
        movieBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    
        // Image panel
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(150, 200));
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(movieData[2]);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImg));
        imagePanel.add(imageLabel);
    
        // Details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Header panel for title and rating
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        JLabel titleLabel = new JLabel(movieData[0]);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel ratingLabel = new JLabel("Rating : " + movieData[1]);
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(ratingLabel);
    
        // Description panel
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        
        JTextArea descriptionArea = new JTextArea(movieData[3]);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        descriptionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        descriptionPanel.add(descriptionArea);
    
        // Book Now button
        JButton bookButton = new JButton("Book Now");
        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookButton.addActionListener(e -> showTheaterAndTimings(frame, movieData[0]));
    
        // Assemble all panels
        detailsPanel.add(headerPanel);
        detailsPanel.add(descriptionPanel);
        detailsPanel.add(Box.createVerticalGlue());
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(bookButton);
    
        movieBox.add(imagePanel, BorderLayout.WEST);
        movieBox.add(detailsPanel, BorderLayout.CENTER);
    
        return movieBox;
    }

    private int currentTheaterPage = 0;
    private final int theatersPerPage = 3;

    private void showTheaterAndTimings(JFrame frame, String movieTitle) {
        frame.getContentPane().removeAll();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(movieTitle, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel theaterListPanel = new JPanel();
        theaterListPanel.setLayout(new BoxLayout(theaterListPanel, BoxLayout.Y_AXIS));

        // Initialize theaters and timings data with prices
        List<String[]> allTheaters = new ArrayList<>();
        allTheaters.add(new String[]{"AMB Cinemas", "09:00 AM, 12:35 PM, 03:30 PM, 07:05 PM, 10:40 PM", "₹250"});
        allTheaters.add(new String[]{"Prasads Multiplex", "10:05 AM, 01:00 PM, 04:10 PM, 07:25 PM, 11:00 PM", "₹200"});
        allTheaters.add(new String[]{"PVR Nexus Mall", "09:00 AM, 01:40 PM, 04:10 PM, 07:45 PM, 11:20 PM", "₹300"});
        allTheaters.add(new String[]{"AAA Cinemas", "09:00 AM, 12:35 PM, 03:30 PM, 07:25 PM, 10:40 PM", "₹180"});
        allTheaters.add(new String[]{"Cinepolis", "09:00 AM, 12:35 PM, 04:10 PM, 07:25 PM, 10:40 PM", "₹220"});
        allTheaters.add(new String[]{"INOX", "10:05 AM, 12:35 PM, 03:30 PM, 07:25 PM, 11:20 PM", "₹280"});
        allTheaters.add(new String[]{"GPR Multiplex", "09:00 AM, 12:35 PM, 04:10 PM, 07:45 PM, 11:20 PM", "₹190"});

        int startIndex = currentTheaterPage * theatersPerPage;
        int endIndex = Math.min(startIndex + theatersPerPage, allTheaters.size());

        for (int i = startIndex; i < endIndex; i++) {
            String[] theater = allTheaters.get(i);
            JPanel theaterPanel = createTheaterPanel(theater, frame, movieTitle);
            theaterListPanel.add(theaterPanel);
            theaterListPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        JScrollPane scrollPane = new JScrollPane(theaterListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Navigation panel
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JButton backButton = new JButton("Back to Movies");

        prevButton.addActionListener(e -> {
            if (currentTheaterPage > 0) {
                currentTheaterPage--;
                showTheaterAndTimings(frame, movieTitle);
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentTheaterPage + 1) * theatersPerPage < allTheaters.size()) {
                currentTheaterPage++;
                showTheaterAndTimings(frame, movieTitle);
            }
        });

        backButton.addActionListener(e -> {
            currentTheaterPage = 0;
            frame.getContentPane().removeAll();
            loadMovies(new JPanel(), frame);
            frame.revalidate();
            frame.repaint();
        });

        navigationPanel.add(prevButton);
        navigationPanel.add(backButton);
        navigationPanel.add(nextButton);

        mainPanel.add(navigationPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel createTheaterPanel(String[] theater, JFrame frame, String movieTitle) {
        JPanel theaterPanel = new JPanel();
        theaterPanel.setLayout(new BoxLayout(theaterPanel, BoxLayout.Y_AXIS));
        theaterPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel theaterLabel = new JLabel(theater[0]);
        theaterLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel priceLabel = new JLabel(theater[2]);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(theaterLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(priceLabel);
        
        theaterPanel.add(headerPanel);
        theaterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    
        JPanel timingsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] timings = theater[1].split(", ");
        for (String timing : timings) {
            JButton timingButton = new JButton(timing);
            timingButton.setPreferredSize(new Dimension(100, 35));
            timingButton.addActionListener(e -> showSeatSelection(frame, movieTitle, theater[0], timing, theater[2]));
            timingsPanel.add(timingButton);
        }
    
        theaterPanel.add(timingsPanel);
    
        return theaterPanel;
    }

    private void showSeatSelection(JFrame parentFrame, String movieTitle, String theater, String timing, String price) {
        parentFrame.getContentPane().removeAll();
        parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Movie information panel
        JPanel infoPanel = new JPanel();
        JLabel titleLabel = new JLabel(movieTitle + " - " + theater + " - " + timing + " - Price: " + price);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(titleLabel);
    
        // Create the main content panel that will be scrollable
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
    
        // Screen panel
        JPanel screenPanel = new JPanel();
        screenPanel.setPreferredSize(new Dimension(900, 30));
        screenPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel screenLabel = new JLabel("SCREEN", JLabel.CENTER);
        screenPanel.add(screenLabel);
    
        // Seat selection panel - now with 3 sections
        JPanel seatSelectionPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        allSeats.clear(); // Clear existing seats
    
        // Create three seat sections
        String[] sectionNames = {"Left Section", "Middle Section", "Right Section"};
        for (int section = 0; section < 3; section++) {
            JPanel seatSection = new JPanel(new GridLayout(8, 8, 2, 2));
            seatSection.setBorder(BorderFactory.createTitledBorder(sectionNames[section]));
    
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    String seatNumber = String.format("%c%d", (char)('A' + row), col + 1);
                    JCheckBox seatCheckbox = new JCheckBox();
                    seatCheckbox.setToolTipText(seatNumber);
                    
                    // Randomly mark some seats as sold
                    if (Math.random() < 0.2) {
                        seatCheckbox.setEnabled(false);
                        seatCheckbox.setBackground(Color.GRAY);
                        seatCheckbox.setOpaque(true);
                    }
                    
                    allSeats.add(seatCheckbox);
                    seatSection.add(seatCheckbox);
                }
            }
            seatSelectionPanel.add(seatSection);
        }
    
        // Legend panel
        JPanel legendPanel = new JPanel();
        legendPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        legendPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
    
        // Available seats
        JPanel availablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JCheckBox availableBox = new JCheckBox();
        availableBox.setEnabled(true);
        availablePanel.add(availableBox);
        availablePanel.add(new JLabel("Available Seats"));
    
        // Sold seats
        JPanel soldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JCheckBox soldBox = new JCheckBox();
        soldBox.setEnabled(false);
        soldBox.setBackground(Color.GRAY);
        soldBox.setOpaque(true);
        soldPanel.add(soldBox);
        soldPanel.add(new JLabel("Sold Seats"));
    
        // Selected seats
        JPanel selectedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JCheckBox selectedBox = new JCheckBox();
        selectedBox.setSelected(true);
        selectedPanel.add(selectedBox);
        selectedPanel.add(new JLabel("Selected Seats"));
    
        legendPanel.add(availablePanel);
        legendPanel.add(soldPanel);
        legendPanel.add(selectedPanel);
    
        // Assemble the content panel
        contentPanel.add(screenPanel, BorderLayout.NORTH);
        contentPanel.add(seatSelectionPanel, BorderLayout.CENTER);
        contentPanel.add(legendPanel, BorderLayout.SOUTH);
    
        // Create a scroll pane for the content
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton confirmButton = createStyledButton("Confirm Booking");
        JButton backButton = createStyledButton("Back to Theaters");
    
        confirmButton.addActionListener(e -> {
            List<String> selectedSeats = new ArrayList<>();
            for (int i = 0; i < allSeats.size(); i++) {
                if (allSeats.get(i).isSelected()) {
                    int sectionIndex = i / 64;
                    int seatInSection = i % 64;
                    String sectionName = sectionNames[sectionIndex].split(" ")[0];
                    String seatNumber = String.format("%s %c%d", 
                        sectionName, (char)('A' + (seatInSection / 8)), (seatInSection % 8) + 1);
                    selectedSeats.add(seatNumber);
                }
            }
    
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Please select at least one seat.");
            } else {
                int ticketPrice = Integer.parseInt(price.substring(1)); // Remove the '₹' symbol and parse
                int totalCost = ticketPrice * selectedSeats.size();
                
                String bookingDetails = String.format("Movie: %s\nTheater: %s\nTime: %s\nSeats: %s\nTotal Cost: ₹%d",
                        movieTitle, theater, timing, String.join(", ", selectedSeats), totalCost);
                
                showConfirmationDialog(parentFrame, bookingDetails);
            }
        });

        backButton.addActionListener(e -> showTheaterAndTimings(parentFrame, movieTitle));
    
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);
    
        // Assemble the main panel
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        parentFrame.add(mainPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void showConfirmationDialog(JFrame parentFrame, String bookingDetails) {
        JDialog dialog = new JDialog(parentFrame, "Booking Confirmation", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);
    
        JTextArea detailsArea = new JTextArea(bookingDetails);
        detailsArea.setEditable(false);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setLineWrap(true);
        detailsArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(detailsArea);
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton proceedToPayButton = createStyledButton("Proceed to Pay");
        JButton cancelButton = createStyledButton("Cancel");
    
        proceedToPayButton.addActionListener(e -> {
            bookings.computeIfAbsent(currentUser, k -> new ArrayList<>()).add(bookingDetails);
            JOptionPane.showMessageDialog(dialog, "Payment successful! Your booking is confirmed.");
            dialog.dispose();
            parentFrame.dispose();
            showProfilePage();
        });
    
        cancelButton.addActionListener(e -> dialog.dispose());
    
        buttonPanel.add(proceedToPayButton);
        buttonPanel.add(cancelButton);
    
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    
        dialog.setVisible(true);
    }
}
