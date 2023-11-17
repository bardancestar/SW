package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Course {

	private String courseId;
	private String courseName;
	private String teacherId;
	private String teacherName;
	private String credit;
	private String hour;
	private float fail, pass, good, excellent;

	public Course(String courseId, float pass, float good, float excellent) {
		super();
		this.courseId = courseId;
		this.pass = pass;
		this.good = good;
		this.excellent = excellent;
	}

	public Course(String courseId, String courseName, String teacherId,
				  String teacherName, String credit, String hour) {

		this.courseId = courseId;
		this.courseName = courseName;
		this.teacherId = teacherId;
		this.teacherName = teacherName;
		this.credit = credit;
		this.hour = hour;
	}

	public Course(String courseId, String courseName, String teacherId,
				  String teacherName, float fial, float pass, float good,
				  float excellent) {

		this.courseId = courseId;
		this.courseName = courseName;
		this.teacherId = teacherId;
		this.teacherName = teacherName;
		this.fail = fial;
		this.pass = pass;
		this.good = good;
		this.excellent = excellent;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public float getFial() {
		return fail;
	}

	public void setFial(float fial) {
		this.fail = fial;
	}

	public float getPass() {
		return pass;
	}

	public void setPass(float pass) {
		this.pass = pass;
	}

	public float getGood() {
		return good;
	}

	public void setGood(float good) {
		this.good = good;
	}

	public float getExcellent() {
		return excellent;
	}

	public void setExcellent(float excellent) {
		this.excellent = excellent;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public float getFail() {
		return fail;
	}

	public void setFail(float fail) {
		this.fail = fail;
	}



	public int hasCourse(){
		// 使用getProperty()函数获取当前用户目录，并添加"/data/course.txt"得到文件目录
		String file = System.getProperty("user.dir")+"/data/course.txt";
		// String file = "D://test//course.txt";
		try{
			// 创建指定文件的文件读写对象，并将读写对象放入缓冲区
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				String[] result = s.split(" ");
				if(result[0].equals(this.courseId)){
					return 1;
				}
			}
			// 关闭当前读写对象缓冲区
			br.close();
		}catch(Exception e){
			// 堆栈信息异常时抛出
			e.printStackTrace();
		}
		return 0;
	}


	public int[] sortGrade() {
		/*
		 * 对单科成绩不同分数段的学生人数进行统计，并进行分数的划档
		 *       fail, pass, good, excellent
		 * */
		int failCount=0, passCount=0, goodCount=0, excellentCount=0;
		// 使用getProperty()函数获取当前用户目录，并添加"/data/course.txt"得到文件目录
		String path = System.getProperty("user.dir")+"/data/grade";
		List<String> files = new ArrayList<String>(); // 目录下所有文件
		File file = new File(path);
		// 返回一个包含指定文件目录下的所有文件和文件夹的数组
		File[] tempList = file.listFiles();
		// 对目录下文件进行筛选，将属于文件类型的字符串加入到文件列表中
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				files.add(tempList[i].toString());
				// 文件名，不包含路径
				// String fileName = tempList[i].getName();
			}
			if (tempList[i].isDirectory()) {

			}
		}

		BufferedReader br = null;
		String targetFile = null;

		try {
			// 寻找指定课程的成绩文件
			for (int i = 0; i < files.size(); i++) {
				br = new BufferedReader(new FileReader(files.get(i)));
				String s = null;
				if((s=br.readLine())!=null){
					String[] result = s.split(" ");
					if(result[0].equals(this.courseId)){
						targetFile = files.get(i);
						break;
					}
				}

				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		try {
			// 根据目标成绩文件夹中的成绩进行不同成绩区间的统计
			br = new BufferedReader(new FileReader(targetFile));
			String s = null;
			while ((s = br.readLine()) != null) {
				String[] result = s.split(" ");
				if (Float.parseFloat(result[6]) < this.pass) {
					failCount += 1;
				}else if(Float.parseFloat(result[6]) < this.good) {
					passCount += 1;
				}else if(Float.parseFloat(result[6])<this.excellent){
					goodCount += 1;
				}else{
					excellentCount += 1;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new int[]{failCount, passCount, goodCount, excellentCount};
	}


	public int isValidate(){      // 输入的成绩标准是否是在正常内[0, 100],以及pass<good<excellent
		if(this.pass<0 || this.pass >100 || this.good<0 || this.good>100 || this.excellent<0 || this.excellent>100
				|| this.pass>=good || this.pass>=excellent || this.good>=excellent)
			return 1;
		return 0;
	}


}
