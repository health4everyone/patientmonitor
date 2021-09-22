package org.health4everyone.patientmonitoring.controllers;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.health4everyone.patientmonitoring.datatables.AppUtil;
import org.health4everyone.patientmonitoring.models.ERole;
import org.health4everyone.patientmonitoring.repositories.RoleRepository;
import org.health4everyone.patientmonitoring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
public class MainController {
	
	@Autowired
	UserRepository userRepo;
	@Autowired
	RoleRepository roleRepo;

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;
 
    //@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
    }
 
    //@RequestMapping(value = { "/index", "/index.html", "index.htm" }, method = RequestMethod.GET)
    public String indexPage(Model model) {
        return "index";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
    	User loggedinUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = loggedinUser.getUsername();
        
        model.addAttribute("userInfo", userInfo);
         
        return "adminPage";
    }
 
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) { 
        return "pages-login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(Model model) {
        return "pages-register";
    }

    @RequestMapping(value = "/recover-password", method = RequestMethod.GET)
    public String recoverPasswordPage(Model model) {
        return "pages-recoverpw";
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }
 
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
 
        // After user login successfully.
        String userName = principal.getName();
 
        System.out.println("User Name: " + userName);
 
        User loggedinUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = loggedinUser.getUsername();
        model.addAttribute("userInfo", userInfo);
 
        return "userInfoPage";
    }


    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {
 
        if (principal != null) {
        	User loggedinUser = (User) ((Authentication) principal).getPrincipal();
 
            String userInfo = loggedinUser.getUsername();
 
            model.addAttribute("userInfo", userInfo);
 
            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page 403!";
            model.addAttribute("message", message);
 
        }
 
        return "403Page";
    }
 
    @RequestMapping(value = {"","/dashboard","/dashboard/","/index","/index.html"}, method = {RequestMethod.GET})
    public String dashboard(Model model, Principal principal) {
    	System.out.println("Entering dashboard");
        if (principal != null) {
        	User loggedinUser = (User) ((Authentication) principal).getPrincipal();
 
            String userInfo = loggedinUser.getUsername();
             
            model.addAttribute("userInfo", userInfo);
                        
            buildUserSessionAttributes(model, principal);
            
            if(model.getAttribute("pamuser")!=null) {
            	System.out.println("We have dashboard user");
            	org.health4everyone.patientmonitoring.models.User us = (org.health4everyone.patientmonitoring.models.User) model.getAttribute("pamuser");
            	System.out.println("User to save:"+AppUtil.getBeanToJsonString(us));
            	us.setLastSeen(new Timestamp((new Date()).getTime()));
            	userRepo.save(us);
            	if ( us !=null && us.getRoles().contains(roleRepo.findByName(ERole.ROLE_ADMIN))) {
                	System.out.println("We have admin dashboard user");
            		return "dashboard-analytics";
            	}
            	if ( us!=null && us.getRoles().contains(roleRepo.findByName(ERole.ROLE_PHYSICIAN))) {
                	System.out.println("We have physician dashboard user");
            		return "patients";
            	}
            	if ( us!=null && us.getRoles().contains(roleRepo.findByName(ERole.ROLE_PHYSICIAN))) {
                	System.out.println("We have patient dashboard user");
            		return "devices";
            	}
            }
            
        }
 
        return "403Page";
    }

    @RequestMapping(value = {"/devices","/devices/"}, method = {RequestMethod.GET})
    public String devices(Model model, Principal principal) {
    	System.out.println("Entering devices");
        if (principal != null) {
        	User loggedinUser = (User) ((Authentication) principal).getPrincipal();
 
            String userInfo = loggedinUser.getUsername();
             
            model.addAttribute("userInfo", userInfo);
                        
            buildUserSessionAttributes(model, principal);
            
            if(model.getAttribute("pamuser")!=null) {
            	System.out.println("We have dashboard user");
            	org.health4everyone.patientmonitoring.models.User us = (org.health4everyone.patientmonitoring.models.User) model.getAttribute("pamuser");
            	System.out.println("User to save:"+AppUtil.getBeanToJsonString(us));
            	us.setLastSeen(new Timestamp((new Date()).getTime()));
            	userRepo.save(us);
            	if ( us !=null && us.getRoles().contains(roleRepo.findByName(ERole.ROLE_ADMIN))) {
                	System.out.println("We have admin devices user");
            		return "devices";
            	}
            }
            
        }
 
        return "403Page";
    }
    
    @RequestMapping(value = {"/device-details"}, method = {RequestMethod.GET})
    public String deviceDetails(Model model, Principal principal) {
    	System.out.println("Entering devices");
        if (principal != null) {
        	User loggedinUser = (User) ((Authentication) principal).getPrincipal();
 
            String userInfo = loggedinUser.getUsername();
             
            model.addAttribute("userInfo", userInfo);
                        
            buildUserSessionAttributes(model, principal);
            
            if(model.getAttribute("pamuser")!=null) {
            	System.out.println("We have dashboard user");
            	org.health4everyone.patientmonitoring.models.User us = (org.health4everyone.patientmonitoring.models.User) model.getAttribute("pamuser");
            	System.out.println("User to save:"+AppUtil.getBeanToJsonString(us));
            	us.setLastSeen(new Timestamp((new Date()).getTime()));
            	userRepo.save(us);
            	if ( us !=null && us.getRoles().contains(roleRepo.findByName(ERole.ROLE_ADMIN))) {
                	System.out.println("We have admin devices user");
            		return "device-details";
            	}
            }
            
        }
 
        return "403Page";
    }

    public void buildUserSessionAttributes(Model model, Principal principal) {
	  System.out.println("Build attributes");
      Optional<org.health4everyone.patientmonitoring.models.User> us = userRepo.findByEmail(((User) ((Authentication) principal).getPrincipal()).getUsername());
      String fullName="Guest User";
      String email = "";
      if(us!=null && us.get()!=null) {
    	  model.addAttribute("pamuser", us.get());
    	  fullName = us.get().getFirstName()+" "+us.get().getLastName();
    	  email = us.get().getEmail();
      } 
	  model.addAttribute("userFullName", fullName);
	  System.out.println("User full name:"+fullName);
	  model.addAttribute("userEmail", email);
	  System.out.println("User email:"+email);
	  
  }
    

}