import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private int counter = 1;
    private int radius = 20;

    List<Integer> circles = new ArrayList<Integer>();

    public Main(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPane);
        this.pack();

        count.setText(String.valueOf(counter));

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (counter == 10) {
                    msg.setText("Can not increment");
                } else {
                    circles.add(radius);
                    drawCircles();
                    msg.setText("");
                    count.setText(String.valueOf(++counter));
                }
            }
        });
        btnRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (counter == 1) {
                    msg.setText("Can not decrement");
                } else {
                    drawingPane.repaint();
                    circles.remove(counter - 1);
                    drawCircles();
                    msg.setText("");
                    count.setText(String.valueOf(--counter));
                }
            }
        });

        drawingPane.setSize(600, 200);

        Random ran = new Random();
        radius = ran.nextInt(26);
        if (radius < 20 || radius > 24)
            radius = 20 + radius % 4;

        comboBox1.addItem(this.radius);
        comboBox1.addItem(25);

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

        circles.add(radius);
        drawCircles();

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radius = (int) comboBox1.getSelectedItem();
                System.out.println(radius);
            }
        });
    }

    private void setRadius(int radius) {
        this.radius = radius;
    }

    private int getRadius() {
        return this.radius;
    }

    private void drawCircles() {
        System.out.println(radius);
        for (int i = 0; i < circles.size(); i++) {
            drawingPane.getGraphics().drawOval(i * 30, 0, circles.get(i), circles.get(i));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new Main("Circles");
        frame.setVisible(true);
        frame.setSize(600, 300);
    }
}


