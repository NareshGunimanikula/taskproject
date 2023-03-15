package com.example.taskproject.service;

import java.util.List;

import com.example.taskproject.payload.TaskDto;

public interface TaskService {
	
	public TaskDto saveTask(long userid, TaskDto taskDto);
	
	public List<TaskDto> getAllTasks(long userid);
	
	public TaskDto getTask(long userid,long taskid);
	
	public void deleteTask(long userid, long taskid);
}
