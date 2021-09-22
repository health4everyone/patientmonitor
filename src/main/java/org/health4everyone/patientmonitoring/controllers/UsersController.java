package org.health4everyone.patientmonitoring.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.health4everyone.patientmonitoring.datatables.AppUtil;
import org.health4everyone.patientmonitoring.datatables.DataTableRequest;
import org.health4everyone.patientmonitoring.datatables.DataTableResults;
import org.health4everyone.patientmonitoring.datatables.PaginationCriteria;
import org.health4everyone.patientmonitoring.models.User;
import org.health4everyone.patientmonitoring.repositories.RoleRepository;
import org.health4everyone.patientmonitoring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
 
@Controller
public class UsersController {
	
	@Autowired
	UserRepository userRepo;
	@Autowired
	RoleRepository roleRepo;

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;
 

	@RequestMapping(value="/users", method= {RequestMethod.GET, RequestMethod.POST})
	public String listUsers(Model model) {
		return "users";
	}
	
	@RequestMapping(value="/users/paginated", method={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String listUsersPaginated(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		DataTableRequest<User> dataTableInRQ = new DataTableRequest<User>(request);
		PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		
		String baseQuery = "SELECT id as id, email as email, username as username, password as password, created_at as created_at, last_seen as last_seen, enabled as enabled FROM USERS";
		String paginatedQuery = AppUtil.buildPaginatedQuery(baseQuery, pagination);
		
		System.out.println(paginatedQuery);
		Query query = entityManager.createNativeQuery(paginatedQuery, User.class);
		
		@SuppressWarnings("unchecked")
		List<User> userList = query.getResultList();
		
		DataTableResults<User> dataTableResult = new DataTableResults<User>();
		dataTableResult.setDraw(dataTableInRQ.getDraw());
		dataTableResult.setListOfDataObjects(userList);
		if (!AppUtil.isObjectEmpty(userList)) {
			dataTableResult.setRecordsTotal(""+userList.size());
			if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
				dataTableResult.setRecordsFiltered(""+userList.size()
						);
			} else {
				dataTableResult.setRecordsFiltered(Integer.toString(userList.size()));
			}
		}
		return new Gson().toJson(dataTableResult);
	}
	
	@RequestMapping(value="/adduser", method=RequestMethod.POST)
	public String addUser(@ModelAttribute User userModel, Model model) {
		if(null != userModel) {
			if(!AppUtil.isObjectEmpty(userModel.getId()) && 
					!AppUtil.isObjectEmpty(userModel.getEmail()) && 
					!AppUtil.isObjectEmpty(userModel.getPassword())) {
				
				User u = new User();
				u.setId(userModel.getId());
				u.setEmail(userModel.getEmail());
				u.setUsername(userModel.getEmail());
				u.setEnabled(false);
				u.setPassword(userModel.getPassword());
				userRepo.save(u);
			}
		}
		return "redirect:/";
	}
}