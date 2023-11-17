package controller;

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GradeInfo extends JFrame {
	/**
	 * 学生根据学号查询所有成绩
	 */
	private static final long serialVersionUID = 1L;
	JPanel contain;
	JTextArea list;
	String id;

	String courseid;
	String coursename;
	String teacherid;
	String teachername;
	String studentid;
	String studentname;
	String grade;
	int rank;



	public GradeInfo(String id) {
		super("课程");
		this.id = id;
		setSize(800, 500);
		contain = new JPanel();
		setLocation(800, 500);
		list = new JTextArea();
		list.setEditable(false);
		contain.add(list);

		list.append("课程号" + "\t");
		list.append("课程名" + "\t");
		list.append("教师工号" + "\t");
		list.append("教师姓名" + "\t");
		list.append("学号" + "\t");
		list.append("学生姓名" + "\t");
		list.append("成绩" + "\t");
		list.append("排名" + "\n");


		// 获取grade文件夹下的所有文件
		String path = System.getProperty("user.dir") + "/data/grade";
		List<String> files = new ArrayList<String>(); // 目录下所有文件
		File file = new File(path);
		File[] tempList = file.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				files.add(tempList[i].toString());
			}
			if (tempList[i].isDirectory()) {

			}
		}

		// 读取课程学分信息
		Map<String, Integer> courseCredits = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/data/course.txt"));
			String s = null;
			while ((s = br.readLine()) != null) {
				String[] result = s.split(" ");
				courseCredits.put(result[1], Integer.parseInt(result[2]));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 计算所有学生的加权平均分
		Map<String, Double> studentAverages = new HashMap<>();
		Map<String, Integer> studentCredits = new HashMap<>();
		try {
			for (int i = 0; i < files.size(); i++) {
				BufferedReader br = new BufferedReader(new FileReader(files.get(i)));
				String s = null;
				List<String> studentGrades = new ArrayList<>();

				while ((s = br.readLine()) != null) {
					studentGrades.add(s);
				}
				br.close();

				for (String studentGrade : studentGrades) {
					String[] result = studentGrade.split(" ");
					String studentId = result[4];
					int score = Integer.parseInt(result[6]);
					int credit = courseCredits.get(result[1]);

					// 计算每个学生的加权平均分
					if (studentAverages.containsKey(studentId)) {
						studentAverages.put(studentId, studentAverages.get(studentId) + score * credit);
						studentCredits.put(studentId, studentCredits.get(studentId) + credit);
					} else {
						studentAverages.put(studentId, (double) (score * credit));
						studentCredits.put(studentId, credit);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 计算目标学生的加权平均分和排名
		double targetAverage = studentAverages.get(id) / studentCredits.get(id);
		int rank = 1;
		for (String studentId : studentAverages.keySet()) {
			double average = studentAverages.get(studentId) / studentCredits.get(studentId);
			if (average > targetAverage) {
				rank++;
			}
		}

		list.append("加权平均分: " + targetAverage + "\n");
		list.append("加权平均分排名: " + rank + "\n");

		try {
			for (int i = 0; i < files.size(); i++) {
				BufferedReader br = new BufferedReader(new FileReader(files.get(i)));
				String s = null;
				List<String> studentGrades = new ArrayList<>();

				while ((s = br.readLine()) != null) {
					studentGrades.add(s);
				}
				br.close();

				for (String studentGrade : studentGrades) {
					String[] result = studentGrade.split(" ");
					if (result[4].equals(id)) {
						courseid = result[0];
						coursename = result[1];
						teacherid = result[2];
						teachername = result[3];
						studentid = result[4];
						studentname = result[5];
						grade = result[6];
						rank = getRank(studentGrades, Integer.parseInt(grade));

						list.append(courseid + "\t");
						list.append(coursename + "\t");
						list.append(teacherid + "\t");
						list.append(teachername + "\t");
						list.append(studentid + "\t");
						list.append(studentname + "\t");
						list.append(grade + "\t");
						list.append(rank + "\n");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		add(contain);
		setVisible(true);
	}

	private int getRank(List<String> grades, int score) {
		int rank = 1;
		for (String grade : grades) {
			String[] result = grade.split(" ");
			int studentScore = Integer.parseInt(result[6]);

			if (studentScore > score) {
				rank++;
			}
		}
		return rank;
	}
}
