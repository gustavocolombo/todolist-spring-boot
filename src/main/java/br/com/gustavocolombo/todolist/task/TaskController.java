package br.com.gustavocolombo.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskrepository;
  
  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel){
    var task = this.taskrepository.save(taskModel);
    return ResponseEntity.status(201).body(task);
  }
}
