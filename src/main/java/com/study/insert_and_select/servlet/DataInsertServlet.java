package com.study.insert_and_select.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.study.insert_and_select.dao.StudentDao;
import com.study.insert_and_select.entity.Student;


@WebServlet("/data/addition")
public class DataInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public DataInsertServlet() {
        super();
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder builder = new StringBuilder();
		
		String readData = null;
		
		BufferedReader reader = request.getReader();
		
		/*
		 * "{
		 * 		"name":"김준일"
		 * 		"age":31
		 * }"
		 */
		while((readData = reader.readLine()) != null) {
			builder.append(readData);
			
		}
		
		// Json -> Map
		// Json -> 객체
		
		Gson gson = new Gson();
		
		Map<String, Object> map = gson.fromJson(builder.toString(), Map.class);
		System.out.println(map);
		System.out.println(map.get("name"));
		System.out.println(map.get("age"));
		
		// Json -> entity
		
		Student student = gson.fromJson(builder.toString(), Student.class);
		System.out.println(student);
		System.out.println(student.getName());
		System.out.println(student.getAge());
		
		StudentDao studentDao = StudentDao.getInstance();
		
		Student findStudent = studentDao.findStudentByName(student.getName());
		
		if(findStudent != null) {
			response.setStatus(400);
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("errorMessage", "이미 등록된 이름입니다.");
			response.setContentType("application/json");
			response.getWriter().println(gson.toJson(errorMap));
			return;
		}
		
		int successCount = studentDao.saveStudent(student);
		
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", 201);
		responseMap.put("data", "응답데이터");
		responseMap.put("successCount", successCount);
		
		response.setStatus(201);
		response.setContentType("application/json");
	
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(responseMap));
	
	}
}
