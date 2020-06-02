import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main extends JFrame {
    private JPanel mainPane;
    private JButton btnAdd;
    private JButton btnRemove;
    private JLabel msg;
    private JLabel count;
    private JComboBox comboBox1;
    private JButton btnWrite;
    private JPanel drawingPane;
    private JPanel templatePane;

    private int counter = 1;
    private int radius = 20;

    class CircleDrawer extends JPanel {
        private static final long serialVersionUID = 1L;
        private int x;
        private int y;
        private int radius;
        CircleDrawer(int x, int y, int radius) {
            this.x = x;
            this.y = y;
            this. radius = radius;
            setPreferredSize(new Dimension(radius, radius));
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.fillOval(x, y, radius, radius);
        }
    }


    public Main(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPane);
        this.pack();

        count.setText(String.valueOf(counter));

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("add");
                if(counter == 10) {
                    msg.setText("Can not increment");
                }
                else {
                    msg.setText("");
                    count.setText(String.valueOf(++counter));
                }
            }
        });
        btnRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("remove");
                if(counter == 1) {
                    msg.setText("Can not decrement");
                }
                else {
                    msg.setText("");
                    count.setText(String.valueOf(--counter));
                }
            }
        });

        Random ran = new Random();
        int radius = ran.nextInt(26);
        if(radius < 20)
            radius = 20 + radius % 20;

        this.radius = radius;
        comboBox1.addItem(25);
        comboBox1.addItem(this.radius);

        btnWrite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileWriter fileWriter = new FileWriter("counter.txt");
                    fileWriter.write(count.getText());
                    fileWriter.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("Successfully wrote to the file.");
            }
        });

        comboBox1.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setRadius((Integer) comboBox1.getSelectedItem());
            }
        });
    }

    private void setRadius(int radius) {
        this.radius = radius;
    }
    private int getRadius() {
        return this.radius;
    }


    public static void main(String[] args) {
        JFrame frame = new Main("Circles");
        frame.setVisible(true);
        frame.setSize(400, 300);
    }

    private void createUIComponents() {
        templatePane = new CircleDrawer(0, 0, radius);
    }
}


