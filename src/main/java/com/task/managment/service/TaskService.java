package com.task.managment.service;

import org.springframework.cache.annotation.*;
import com.task.managment.exciptions.RecordNotFoundException;
import com.task.managment.model.Task;
import com.task.managment.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"fpcache"})
public class TaskService {
    @Autowired
    TaskRepository repository;

    @Cacheable("fpcache")
    public List<Task> getAllTasks()
    {
        List<Task> taskList = (List<Task>) repository.findAll();

        if(taskList.size() > 0) {
            return taskList;
        } else {
            return new ArrayList<Task>();
        }
    }

    @Cacheable(value = "fpcache",key = "#id")
    public Task getTaskById(Long id) throws RecordNotFoundException
    {
        Optional<Task> task = repository.findById(id);

        if(task.isPresent()) {
            return task.get();
        } else {
            throw new RecordNotFoundException("No task record exist for given id");
        }
    }

//    @CachePut(value = "fpcache",key = "#entity.id")
    @CacheEvict(allEntries = true)
    public Task createTask(Task entity)
    {
            entity = repository.save(entity);
            return entity;
    }


//    @CachePut(value = "fpcache",key = "#id")
    @CacheEvict(allEntries = true)
    public Task updateTask(Task entity) throws RecordNotFoundException
    {
        Optional<Task> task = repository.findById(entity.getId());
        if (task.isPresent()) {
            Task updatedEntity = task.get();
            updatedEntity.setTitle(entity.getTitle());
            updatedEntity.setDescription(entity.getDescription());
            updatedEntity.setLocation(entity.getLocation());
            updatedEntity.setStart(entity.getStart());
            updatedEntity.setEnd(entity.getEnd());

            updatedEntity = repository.save(entity);

            return updatedEntity;
        } else {
            throw  new RecordNotFoundException("No task Record exist for given id");
        }

    }

    @CacheEvict(allEntries = true)
    public void deleteTaskById(Long id) throws RecordNotFoundException
    {
        Optional<Task> task = repository.findById(id);

        if(task.isPresent())
        {
            repository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No task record exist for given id");
        }
    }
}
