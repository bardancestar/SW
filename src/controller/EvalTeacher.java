package controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class EvalTeacher extends JFrame {
    private JPanel panel;
    private JButton submitButton;
    private ArrayList<JComboBox<String>> ratingBoxes = new ArrayList<>();
    private Map<String, JComboBox<String>> teacherRatings = new HashMap<>();
    private Map<String, Double> previousRatings = new HashMap<>();

    public EvalTeacher(){
        super("教师评分");
        setSize(300, 340);
        setLocation(600, 400);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        submitButton = new JButton("提交");
        String file = System.getProperty("user.dir")+"/data/teacher.txt";
        String address = System.getProperty("user.dir")+"/data/rating.txt";

        // 生成评分界面
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String teacherName = parts[2];
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.add(new JLabel(teacherName));

                JComboBox<String> box = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"}); //评分从1-5
                row.add(box);
                ratingBoxes.add(box);
                teacherRatings.put(teacherName, box);

                panel.add(row);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 读取名字和评分
        try {

            BufferedReader reader = new BufferedReader(new FileReader(address));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String teacherName = parts[0];
                double rating = Double.parseDouble(parts[1]);
                previousRatings.put(teacherName, rating);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 提交按钮
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JComboBox<String> box : ratingBoxes) {
                    if (box.getSelectedItem() == null) {
                        JOptionPane.showMessageDialog(EvalTeacher.this, "请为所有的教师评分");
                        return;
                    }
                }
                // 计算平均分并保存为rating文件
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(address));
                    DecimalFormat df = new DecimalFormat("#.#");
                    for (Map.Entry<String, JComboBox<String>> entry : teacherRatings.entrySet()) {
                        String teacherName = entry.getKey();
                        double newRating = Double.parseDouble((String) entry.getValue().getSelectedItem());
                        if (previousRatings.containsKey(teacherName)) {
                            newRating = (newRating + previousRatings.get(teacherName)) / 2;
                        }
                        writer.write(teacherName + " " + df.format(newRating) + "\n");
                    }
                    writer.close();
                    dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // 滚动条（教师名字一页显示不完时）
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
        pack();
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
