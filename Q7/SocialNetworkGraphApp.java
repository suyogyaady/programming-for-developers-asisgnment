package Question_Number7;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class SocialNetworkGraphApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Social Network Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            SocialNetworkGraphPanel graphPanel = new SocialNetworkGraphPanel();
            frame.add(graphPanel, BorderLayout.CENTER);

            JToolBar toolBar = new JToolBar();
            JButton addNodeButton = new JButton("Add Node");
            JButton addEdgeButton = new JButton("Add Edge");

            addNodeButton.addActionListener(e -> graphPanel.addNewNode());
            addEdgeButton.addActionListener(e -> graphPanel.addNewEdge());

            toolBar.add(addNodeButton);
            toolBar.add(addEdgeButton);
            frame.add(toolBar, BorderLayout.NORTH);

            JTextField searchField = new JTextField();
            searchField.setColumns(20);
            JButton searchButton = new JButton("Search");

            searchButton.addActionListener(e -> {
                String searchQuery = searchField.getText();
                graphPanel.searchAndHighlightNode(searchQuery);
                graphPanel.repaint();
            });

            JPanel searchPanel = new JPanel();
            searchPanel.add(new JLabel("Search User: "));
            searchPanel.add(searchField);
            searchPanel.add(searchButton);

            frame.add(searchPanel, BorderLayout.SOUTH);

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
            graphPanel.requestFocusInWindow();
        });
    }
}

class SocialNetworkGraphPanel extends JPanel {
    private static String directory= System.getProperty("user.dir")+ "/Question_Number7/";
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    private Map<String, Node> nodeMap = new HashMap<>();

    private Node selectedNode=null;

    private Node selectedEdge=null;
    private Node draggingNode = null;
    private Point mouseOffset = new Point();

    public SocialNetworkGraphPanel() {
        readUserDataFromFile(directory+"users.txt");
        readConnectionsFromFile(directory+"connection.txt");
        adjustNodePositions();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deSelectAllNodes();
                highlightSelectedNode();
                repaint();
            }
        });



        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    removeSelectedNode();
                    removeSelectedEdge();
                    repaint();
                }
            }});

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deSelectAllEdges();
                highlightSelectedEdge();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                draggingNode = getSelectedNode(e.getPoint());
                if (draggingNode != null) {
                    mouseOffset.setLocation(e.getPoint().getX() - draggingNode.x, e.getPoint().getY() - draggingNode.y);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggingNode = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingNode != null) {
                    draggingNode.x = (int) (e.getPoint().getX() - mouseOffset.getX());
                    draggingNode.y = (int) (e.getPoint().getY() - mouseOffset.getY());
                    repaint();
                }
            }
        });



        setFocusable(true);
        requestFocus();
    }
    public void searchAndHighlightNode(String searchQuery) {
        deSelectAllNodes();

        if (searchQuery != null && !searchQuery.isEmpty()) {
            Node matchingNode = nodeMap.get(searchQuery);
            if (matchingNode != null) {
                matchingNode.isSelected = true;
            }
        }

        repaint();
    }

    public void addNewNode() {
        String userName = JOptionPane.showInputDialog(this, "Enter Username:");
        if (userName != null && !userName.isEmpty()) {
            String followersStr = JOptionPane.showInputDialog(this, "Enter Followers:");
            if (followersStr != null && !followersStr.isEmpty()) {
                try {
                    int followers = Integer.parseInt(followersStr);
                    Node newNode = new Node(200, 200, userName, followers); // You can adjust the initial position as needed
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(this);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        newNode.profileImagePath = selectedFile.getAbsolutePath();
                    }
                    nodes.add(newNode);
                    nodeMap.put(userName, newNode);
                    adjustNodePositions();
                    repaint();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid number format for followers.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void addNewEdge() {
        String user1Name = JOptionPane.showInputDialog(this, "Enter Username for User 1:");
        if (user1Name != null && !user1Name.isEmpty()) {
            String user2Name = JOptionPane.showInputDialog(this, "Enter Username for User 2:");
            if (user2Name != null && !user2Name.isEmpty()) {
                String strengthStr = JOptionPane.showInputDialog(this, "Enter Strength:");
                if (strengthStr != null && !strengthStr.isEmpty()) {
                    try {
                        int strength = Integer.parseInt(strengthStr);
                        Node node1 = nodeMap.get(user1Name);
                        Node node2 = nodeMap.get(user2Name);

                        if (node1 != null && node2 != null) {
                            edges.add(new Edge(node1, node2, "Strength: " + strength));
                            repaint();
                        } else {
                            JOptionPane.showMessageDialog(this, "One or both user nodes not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid number format for strength.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void readUserDataFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");

                if (parts.length == 4) {
                    String userName = parts[0];
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    int followers = Integer.parseInt(parts[3]);
                    Node node = new Node(x, y, userName, followers);

                    node.profileImagePath = System.getProperty("user.dir") +"/Question_Number7/images/" + userName + ".jpg";
                    nodes.add(node);
                    nodeMap.put(userName, node);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readConnectionsFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    String user1 = parts[0];
                    String user2 = parts[1];
                    int strength = Integer.parseInt(parts[2]);
                    Node node1 = nodeMap.get(user1);
                    Node node2 = nodeMap.get(user2);
                    if (node1 != null && node2 != null) {
                        edges.add(new Edge(node1, node2, "Strength: " + strength));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adjustNodePositions() {
        int spacing = 150;
        Set<Point> usedPositions = new HashSet<>();

        for (Node node : nodes) {
            Point position = new Point(node.x, node.y);
            while (usedPositions.contains(position)) {
                position.translate(spacing, 0);
            }
            node.x = position.x;
            node.y = position.y;
            usedPositions.add(position);

            // Load profile image
            try {
                BufferedImage image = ImageIO.read(new File( node.profileImagePath));
                if (image != null) {
                    node.profileImage = image;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void highlightSelectedEdge() {
        Point mousePosition = getMousePosition();
        if (mousePosition != null) {
            Edge selectedEdge = getSelectedEdge(mousePosition);
            if (selectedEdge != null) {
                selectedEdge.isSelected = true;
            }
        }
    }

    private void deSelectAllEdges() {
        for (Edge edge : edges) {
            edge.isSelected = false;
        }
    }

    private void removeSelectedNode() {
        Point mousePosition = getMousePosition();
        if (mousePosition != null) {
            Node selectedNode = getSelectedNode(mousePosition);
            if (selectedNode != null) {
                nodes.remove(selectedNode);
                // Also remove connected edges
                edges.removeIf(edge -> edge.startNode == selectedNode || edge.endNode == selectedNode);
            }
        }
    }

    private void removeSelectedEdge() {
        Point mousePosition = getMousePosition();
        if (mousePosition != null) {
            Edge selectedEdge = getSelectedEdge(mousePosition);
            if (selectedEdge != null) {
                edges.remove(selectedEdge);
            }
        }
    }
    private void highlightSelectedNode() {
        Point mousePosition = getMousePosition();
        if (mousePosition != null) {
            Node selectedNode = getSelectedNode(mousePosition);
            if (selectedNode != null) {
                selectedNode.isSelected = true;
            }
        }
    }

    private void deSelectAllNodes() {
        for (Node node : nodes) {
            node.isSelected = false;
        }
    }

    private Node getSelectedNode(Point point) {
        for (Node node : nodes) {
            if (node.contains(point)) {
                return node;
            }
        }
        return null;
    }

    private Edge getSelectedEdge(Point point) {
        for (Edge edge : edges) {
            if (edge.contains(point)) {
                return edge;
            }
        }
        return null;
    }

    private final int GRID_SIZE = 20;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGrid(g);

        for (Edge edge : edges) {
            edge.draw(g);
        }

        for (Node node : nodes) {
            node.draw(g);
        }
    }




    private class Node {
        public String profileImagePath;
        private BufferedImage profileImage;
        int x, y;
        String userName;
        int followers;

        boolean isSelected=false  ;

        Node(int x, int y, String userName, int followers) {
            this.x = x;
            this.y = y;
            this.userName = userName;
            this.followers = followers;
        }

        boolean contains(Point point) {
            return new Rectangle(x - 30, y - 30, 60, 60).contains(point);
        }

         void draw(Graphics g) {
             if (isSelected) {
                 g.setColor(Color.blue); // Change color for selected node
             } else {
                 g.setColor(Color.lightGray);
             }
             g.fillOval(x - 30, y - 30, 60, 60);
             g.setColor(Color.black);
             g.drawString(userName + " (" + followers + " followers)", x - 30, y + 50);
             if (profileImage != null) {
                 int imageSize = 40;
                 g.drawImage(profileImage, x - imageSize / 2, y - imageSize / 2, imageSize, imageSize, null);
             }
         }
    }

    private class Edge {
        Node startNode, endNode;
        String connectionStrength;

        boolean isSelected=false  ;

        Edge(Node startNode, Node endNode, String connectionStrength) {
            this.startNode = startNode;
            this.endNode = endNode;
            this.connectionStrength = connectionStrength;
        }

        boolean contains(Point point) {
            return new Line2D.Double(startNode.x, startNode.y, endNode.x, endNode.y).ptLineDist(point) < 5;
        }

        void draw(Graphics g) {
            if (isSelected) {
                g.setColor(Color.red);  // Change color for selected edge
            } else {
                g.setColor(Color.black);
            }

            g.drawLine(startNode.x, startNode.y, endNode.x, endNode.y);
            g.drawString(connectionStrength, (startNode.x + endNode.x) / 2 - 20, (startNode.y + endNode.y) / 2 + 20);
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.lightGray);
        for (int x = 0; x < getWidth(); x += GRID_SIZE) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += GRID_SIZE) {
            g.drawLine(0, y, getWidth(), y);
        }
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
