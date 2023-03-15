package com.example.taskproject.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskproject.entity.Task;
import com.example.taskproject.entity.Users;
import com.example.taskproject.exception.APIException;
import com.example.taskproject.exception.TaskNotFound;
import com.example.taskproject.exception.UserNotFound;
import com.example.taskproject.payload.TaskDto;
import com.example.taskproject.repository.TaskRepository;
import com.example.taskproject.repository.UserRepository;
import com.example.taskproject.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public TaskDto saveTask(long userid, TaskDto taskDto) {
		Users user = userRepository.findById(userid).orElseThrow(
				() -> new UserNotFound(String.format("User id %d not found", userid))
				);
		Task task = modelMapper.map(taskDto, Task.class);
		task.setUsers(user);
		//After setting the user, we are storing the data in DB
		Task savedTask = taskRepository.save(task);
		return modelMapper.map(savedTask, TaskDto.class);
	}

	@Override
	public List<TaskDto> getAllTasks(long userid) {
		userRepository.findById(userid).orElseThrow(
				() -> new UserNotFound(String.format("User id %d not found", userid))
				);
		List<Task> tasks = taskRepository.findAllByUsersId(userid);
		
		return tasks.stream().map(
				task -> modelMapper.map(task, TaskDto.class)
				).collect(Collectors.toList());
	}

	@Override
	public TaskDto getTask(long userid, long taskid) {
		Users users = userRepository.findById(userid).orElseThrow(
				() -> new UserNotFound(String.format("User id %d not found", userid))
				);
		Task task = taskRepository.findById(taskid).orElseThrow(
				() -> new TaskNotFound(String.format("Task id %d not found", taskid))
				);
		if(users.getId() != task.getUsers().getId()) {
			throw new APIException(String.format("Task id %d is not belonbgs to User id %d", taskid, userid));
		}
		return modelMapper.map(task, TaskDto.class);
	}

	@Override
	public void deleteTask(long userid, long taskid) {
		Users users = userRepository.findById(userid).orElseThrow(
				() -> new UserNotFound(String.format("User id %d not found", userid))
				);
		Task task = taskRepository.findById(taskid).orElseThrow(
				() -> new TaskNotFound(String.format("Task id %d not found", taskid))
				);
		if(users.getId() != task.getUsers().getId()) {
			throw new APIException(String.format("Task id %d is not belonbgs to User id %d", taskid, userid));
		}
		taskRepository.deleteById(taskid);
		
	}

}
