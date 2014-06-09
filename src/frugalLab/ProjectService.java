/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frugalLab;

import javax.persistence.*;
import java.util.*;
import java.sql.Date;

/**
 *
 * @author Hinsen Chan
 */
public class ProjectService {
    private EntityManager manager;
    
    public ProjectService(EntityManager manager) {
        this.manager = manager;
    }    
    
        // method to create a new record
    public Project createProject(String title, String status, String startDate, String endDate, String outcome, String category) {        
        Project project = new Project();
        project.setTitle(title);
        project.setStatus(status);
        project.setStartDate(Date.valueOf(startDate));
        if (!endDate.isEmpty()) {
            project.setEndDate(Date.valueOf(endDate));
        }
        project.setOutcome(outcome);
        
        Long newID = -1L;
        List<Category> catList = readCategories();
        for (int i = 0; i < catList.size(); i++) {
            if (catList.get(i).getCategory().equals(category)) {
                newID = catList.get(i).getId();
                break;
            }
        }
        
        Set<Category> newCategoryList = new TreeSet<Category>();
        Category newCategory = new Category();
        newCategory.setCategory(category);
        newCategory.setId(newID);
        newCategoryList.add(newCategory);
        project.setCategory(newCategoryList);
 	
        manager.persist(project);
 	return project;
    }
    
    // method to read a record
    public Project readProject(Long id) {
        Project project = manager.find(Project.class, id);
    	return project;   	 
    }

    // method to read all records
    public List<Project> readAll() {
        TypedQuery<Project> query = manager.createQuery("SELECT e FROM PROJECT e", Project.class);
        List<Project> result =  query.getResultList();

    	return result;   	 
    }
    
    public List<Category> readCategories() {
        TypedQuery<Category> query = manager.createQuery("SELECT e FROM CATEGORY e", Category.class);
        List<Category> result =  query.getResultList();

    	return result;           
    }    
     
    // method to update a record
    public Project updateProject(Long id, String title, String status, String startDate, String endDate, String outcome) {
        Project project = manager.find(Project.class, id);
        
    	if (project != null) {
            project.setTitle(title);
            project.setStatus(status);
            project.setStartDate(Date.valueOf(startDate));
            project.setEndDate(Date.valueOf(endDate));
            project.setOutcome(outcome);
            manager.persist(project);
    	}
        
    	return project;
    }

    // method to delete a record
    public void deleteProject(Long id) {
        Project project = manager.find(Project.class, id);
    	if (project != null) {
            manager.remove(project);
    	}
    }
    
    // method to find a record using title
    public boolean findProject(String projectName) {
        TypedQuery<Project> query = manager.createQuery("SELECT e.title FROM PROJECT e", Project.class);
        List<Project> result = query.getResultList();        

        if (result.contains(projectName)) {
            return true;
        }
        
        return false;
    }
    
    // method to find a record using title excluding specified primary key
    public boolean findProject(String projectName, long id) {
        TypedQuery<Project> query = manager.createQuery("SELECT e.title FROM PROJECT e WHERE e.id != :targetID", Project.class);
        query.setParameter("targetID", id);
        List<Project> result = query.getResultList();        

        if (result.contains(projectName)) {
            return true;
        }
        
        return false;
    }
}