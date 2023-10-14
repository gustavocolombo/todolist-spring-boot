package br.com.gustavocolombo.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskrepository;
  
  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){

    var currentDate = LocalDateTime.now();
    
    if(currentDate.isAfter(taskModel.getStartedAt()) || currentDate.isAfter(taskModel.getEndedAt())){
      return ResponseEntity.status(400).body("Start or end date need to be higher than current date");
    }

    if(taskModel.getStartedAt().isAfter(taskModel.getEndedAt())){
      return ResponseEntity.status(400).body("Start date need to be lower than end date");
    }

    taskModel.setIdUser((UUID) request.getAttribute("idUser"));
    var task = this.taskrepository.save(taskModel);
    return ResponseEntity.status(201).body(task);
  }

  @GetMapping("/")
  public List<TaskModel> getAllTasks(HttpServletRequest request){
    var idUser = (UUID) request.getAttribute("idUser");

    var tasksFromUser = this.taskrepository.findByIdUser(idUser);
    return tasksFromUser;
  }
}
